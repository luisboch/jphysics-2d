/**
 * World.class
 */
package org.jphysics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.jphysics.api.ContactListener;
import org.jphysics.api.SimpleContactListener;
import org.jphysics.api.ContactResolver;
import org.jphysics.projectile.Projectile;
import org.jphysics.api.ControllableObject;
import org.jphysics.api.ControllerResolver;
import org.jphysics.api.DefaultControllerResolver;
import org.jphysics.api.DefaultExplosionResolver;
import org.jphysics.api.GameObject;
import org.jphysics.api.SelfOperatedObject;
import org.jphysics.api.PhysicObject;
import org.jphysics.api.ExplosionResolver;
import org.jphysics.api.Force;
import org.jphysics.api.SimpleContactResolver;
import org.jphysics.math.Vector2f;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class Engine {

    private final Queue<PhysicObject> actors = new ConcurrentLinkedQueue<PhysicObject>();
    private final Map<PhysicObject, ObjectController> controllRef = new HashMap<PhysicObject, ObjectController>();
    private final Map<PhysicObject, ProjectileInfo> projectileRef = new HashMap<PhysicObject, ProjectileInfo>();

    private final Map<Projectile, Long> projectiles = new ConcurrentHashMap<Projectile, Long>();
    private final Queue<GameObject> deadObjects = new ConcurrentLinkedQueue<GameObject>();

    private final List<Force> forces = new ArrayList<Force>();

    private final float width;
    private final float height;
    private final float depth;
    private boolean avoidOjectsLeaveMap = false;

    // Updated every frame.
    private float deltaTime;

    private ExplosionResolver explosionResolver = new DefaultExplosionResolver();
    private ControllerResolver controllerResolver = new DefaultControllerResolver();
    private ContactResolver contactResolver = new SimpleContactResolver();
    private ContactListener contactListener = new SimpleContactListener();

    /**
     *
     * @param width in metters
     * @param height in metters
     * @param depth in metters (z)
     */
    public Engine(float width, float height, float depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public Engine add(PhysicObject actor) {
            final Vector2f pos = actor.getPosition();
        
            if(pos.x == 0f && pos.y == 0f){
            pos.x = width * 0.5f;
            pos.y = height * 0.5f;
        }
        
        if (avoidOjectsLeaveMap) {


            if (pos.y < 0) {
                pos.y = 0;
            } else if (pos.y > height) {
                pos.y = height;
            }

            if (pos.x < 0) {
                pos.x = 0;
            } else if (pos.x > width) {
                pos.y = width;
            }

            actor.setPosition(pos);
        }

        actors.add(actor);

        return this;
    }

    public Engine addForce(Force force) {
        forces.add(force);
        return this;
    }

    public List<PhysicObject> getActors() {
        return new ArrayList<PhysicObject>(actors);
    }

    public List<PhysicObject> getVisibleActors(Vector2f center, Float viewSize) {
        if (center == null || viewSize == null) {
            throw new IllegalArgumentException("All params are required!");
        }

        viewSize = viewSize * 1.3f; //add 30% to view

        final List<PhysicObject> list = new ArrayList<PhysicObject>();

        for (PhysicObject obj : actors) {
            if (obj.getPosition().distance(center) < viewSize) {
                list.add(obj);
            }
        }

        return list;

    }

    public PhysicObject getClosestActor(Vector2f center, Float viewSize, Class<? extends PhysicObject> type) {

        final List<Class<? extends PhysicObject>> allowedTypes = new ArrayList<Class<? extends PhysicObject>>(2);
        allowedTypes.add(type);

        return getClosestActor(center, viewSize, allowedTypes, new ArrayList<PhysicObject>());
    }

    public PhysicObject getClosestActor(Vector2f center, Float viewSize, Class<? extends PhysicObject>... allowedTypes) {
        List<Class<? extends PhysicObject>> list = Arrays.asList(allowedTypes);
        return getClosestActor(center, viewSize, list, new ArrayList<PhysicObject>());
    }

    public PhysicObject getClosestActor(Vector2f center, Float viewSize, List<Class<? extends PhysicObject>> allowedTypes, List<PhysicObject> ignored) {
        if (center == null || viewSize == null) {
            throw new IllegalArgumentException("All params are required!");
        }

        if (allowedTypes == null) {
            allowedTypes = new ArrayList<Class<? extends PhysicObject>>(0);
        }

        PhysicObject closest = null;
        viewSize = viewSize * 1.3f; //ads 30% to view
        Float closestDis = null;

        for (PhysicObject obj : actors) {
            if (!ignored.contains(obj)) {
                float dst = obj.getPosition().distance(center);
                if (isClassAllowed(allowedTypes, obj.getClass()) && dst < viewSize) {
                    if (closest == null || dst < closestDis) {
                        closest = obj;
                        closestDis = dst;
                    }
                }
            }
        }
        return closest;
    }

    private boolean isClassAllowed(List<Class<? extends PhysicObject>> allowedTypes, Class<? extends PhysicObject> targetClass) {

        for (Class<? extends PhysicObject> ori : allowedTypes) {
            if (ori.isAssignableFrom(targetClass)) {
                return true;
            }
        }

        return false;
    }

    private void discoverActors(List<PhysicObject> currentList, Collection<PhysicObject> source) {
        for (PhysicObject obj : source) {
            currentList.add(obj);
            discoverActors(currentList, obj.getChildren());
        }
    }

    public void calculate(final float deltaTime) {
        this.deltaTime = deltaTime;
        removeDeadObjects();

        final List<PhysicObject> fullList = new ArrayList<PhysicObject>();

        discoverActors(fullList, actors);

        for (PhysicObject obj : fullList) {

            resolveImpact(obj, fullList);

            /**
             * Primeiro calcula as forças.<br>
             * <ul>
             * <li>1 Sterring; </li>
             * <li>2 Gravidade; </li>
             * <li>3 Outras forças quaisquer (vendo, magnetismo, etc); </li>
             * </ul>
             * Depois multiplica a soma das forças pelo tempo gasto no loop
             * (secs) e limita pela força máxima do objeto (questionável). <br>
             * <br>
             * Aplica a variação da massa (força divida pela massa). <br>
             * <br>
             * Seta velocidade no objeto, considerando a soma da força.<br>
             * Move o objeto, de acordo com a velocidade atual (já com a força
             * aplicada) multiplicado pelo tempo gasto no loop (secs).
             */
            if (obj instanceof Projectile) {
                Projectile pro = (Projectile) obj;
                if (pro.canExplodeNow(false)) {
                    createExplosion(pro);
                } else if (System.currentTimeMillis() - projectiles.get(pro) > pro.getLifeTime()) {
                    if (pro.canExplodeNow(true)) {
                        createExplosion(pro);
                    } else {
                        deadObjects.add(obj);
                    }
                    continue;
                }
            } else if (obj instanceof Force) {
                continue;
            }

            final Vector2f steeringCalc = (obj instanceof SelfOperatedObject) ? calculateSteering((SelfOperatedObject) obj) : new Vector2f();

            final Vector2f control;
            final Vector2f forces;

            if (!(obj instanceof Force)) {
                if (obj instanceof ControllableObject) {
                    control = calculateControl((ControllableObject) obj);
                } else {
                    control = new Vector2f();
                }

                forces = calculateForceInfluence(obj);
            } else {
                control = new Vector2f();
                forces = new Vector2f();
            }

            steeringCalc.add(control).add(forces).mul(deltaTime);
            // Divide by mass
            steeringCalc.mul(1f / obj.getMass());

            final Vector2f newVelocity = new Vector2f(obj.getVelocity()).add(steeringCalc);
            if (newVelocity.length() > obj.getMaxVelocity()) {
                newVelocity.normalize().mul(obj.getMaxVelocity());
            }

            obj.setVelocity(newVelocity);

            if (obj.getVelocity().length() > 0f) {
                final Vector2f velSec = new Vector2f(obj.getVelocity()).mul(deltaTime);
                obj.setPosition(obj.getPosition().add(velSec));
            }

            if (obj instanceof ControllableObject && avoidOjectsLeaveMap) {

                final Vector2f vel = obj.getVelocity();

                if (obj.getPosition().x > width && obj.getVelocity().x > 0) {
                    vel.x = -vel.x;
                }

                if (obj.getPosition().x < 0 && obj.getVelocity().x < 0) {
                    vel.x = -vel.x;
                }

                if (obj.getPosition().y > height && obj.getVelocity().y > 0) {
                    vel.y = -vel.y;
                }

                if (obj.getPosition().y < 0 && obj.getVelocity().y < 0) {
                    vel.y = -vel.y;
                }

                obj.setVelocity(vel);
            }
        }
        
        IterableUtils.forEach(forces, new Closure<Force>() {
            @Override
            public void execute(Force input) {
                input.update(deltaTime);
            }
        });
        
        final List<Force> filteredForces = new ArrayList<Force>();
        CollectionUtils.filter(filteredForces, new Predicate<Force>() {
            @Override
            public boolean evaluate(Force object) {
                return !object.isAlive();
            }
        });
        
        deadObjects.addAll(filteredForces);

    }

    private Vector2f calculateSteering(SelfOperatedObject obj) {
        return obj.getSteering() != null ? obj.getSteering().calculate() : new Vector2f();
    }

    private Vector2f calculateForceInfluence(PhysicObject objA) {
        final Vector2f result = new Vector2f();

        // Used to calculate how much we will hit obj.
        final Vector2f expForce = new Vector2f();

        for (Force force : forces) {

            final Vector2f forceField = new Vector2f(objA.getPosition()).sub(force.getPosition());
            final float dist = forceField.length();

            if (dist > 3000) { // 3 KM 
                continue; // Ignore objects that is too far away.
            }

            final float intensity = objA.getMass() * force.getMass();

            final float distSqr = dist * dist;

            forceField.normalize();
            forceField.mul(intensity / distSqr);
            result.sub(forceField);
        }

        return result;

    }

    public ObjectController create(ControllableObject actor) {
        ObjectController act = new ObjectController(actor);
        add(actor);
        bind(actor, act);
        return act;
    }

    private void bind(ControllableObject actor, ObjectController act) {
        controllRef.put(actor, act);
    }

    private Vector2f calculateControl(ControllableObject obj) {

        if (controllRef.containsKey(obj)) {
            ObjectController act = controllRef.get(obj);
            return controllerResolver.calculate(this, obj, act);
        }

        return new Vector2f();
    }

    public <E extends Projectile> void createProjectile(PhysicObject from, E projectile) {

        Vector2f pos = from.getPosition();
        pos.add(from.getDirection().normalize().mul(from.getRadius() + projectile.getRadius() + 1));
        projectile.setPosition(pos);
        projectile.setDirection(from.getDirection());
        projectile.setVelocity(from.getDirection().normalize().mul(projectile.getInitialVelocity()));
        projectiles.put(projectile, System.currentTimeMillis());

        actors.add(projectile);
    }

    private boolean canPlayerCreateProjectile(ControllableObject from, Class<? extends Projectile> type) {
        long now = System.currentTimeMillis();
        final ProjectileInfo nfo;

        if (!projectileRef.containsKey(from)) {
            nfo = new ProjectileInfo();
            projectileRef.put(from, nfo);
        } else {
            nfo = projectileRef.get(from);
        }

        if (nfo.usedTypes.containsKey(type)) {
            final Long lastShot = nfo.usedTypes.get(type);

            if (now - lastShot <= Projectile.getReloadTimeConfig().get(type)) {
                return false; // User must wait for reload time before add new Projectile...
            }
        }

        nfo.usedTypes.put(type, now);

        return true;
    }

    private void removeDeadObjects() {

        GameObject obj = null;

        while ((obj = deadObjects.poll()) != null) {

            if (obj instanceof Projectile) {
                projectiles.remove((Projectile) obj);
            }
            
            if (obj instanceof Force) {
                forces.remove((Force) obj);
            }
            
            if (obj instanceof PhysicObject) {
                actors.remove((PhysicObject) obj);
            }

        }
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setAvoidOjectsLeaveMap(boolean avoidOjectsLeaveMap) {
        this.avoidOjectsLeaveMap = avoidOjectsLeaveMap;
    }

    public void setControllerResolver(ControllerResolver controllerResolver) {
        this.controllerResolver = controllerResolver;
    }

    public void setExplosionResolver(ExplosionResolver explosionResolver) {
        this.explosionResolver = explosionResolver;
    }

    private void createExplosion(Projectile p) {
        final Force exp = explosionResolver.create(p);
        forces.add(exp);
        deadObjects.add(p);
    }

    private void resolveImpact(PhysicObject ob, List<PhysicObject> actors) {

        if (!ob.isAlive()) {
            return;
        }

        for (PhysicObject act : actors) {

            if (!ob.equals(act) && act.isAlive()
                    && ob.getPosition().distance(act.getPosition()) <= (ob.getRadius() + act.getRadius())) {
                ContactResolver.Result result = contactResolver.resolve(ob, act);
                result.getReference1().getPosition().add(result.getResult1());
                result.getReference2().getPosition().add(result.getResult2());
            }
        }
    }

}
