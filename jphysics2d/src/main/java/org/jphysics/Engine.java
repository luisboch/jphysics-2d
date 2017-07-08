/**
 * World.class
 */
package org.jphysics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.jphysics.api.*;
import org.jphysics.projectile.Projectile;
import org.jphysics.math.Vector2f;

/**
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class Engine implements Serializable {

    private Queue<PhysicObject> actors = new ConcurrentLinkedQueue<PhysicObject>();
    private Map<PhysicObject, ObjectController> controllRef = new HashMap<PhysicObject, ObjectController>();

    private Queue<GameObject> deadObjects = new ConcurrentLinkedQueue<GameObject>();

    private List<Force> forces = new ArrayList<Force>();

    private float width;
    private float height;
    private boolean avoidOjectsLeaveMap = false;
    private boolean mapIsLoop = false;
    private List<Action> actions = new ArrayList<Action>();

    // Updated every frame.
    private float deltaTime;

    private ExplosionResolver explosionResolver = new DefaultExplosionResolver();
    private ControllerResolver controllerResolver = new DefaultControllerResolver();
    private ContactResolver contactResolver = new SimpleContactResolver();
    private ContactListener contactListener = new SimpleContactListener();
    private ProjectileResolver projectileResolver = new SimpleProjectileResolver() {
        @Override
        protected Projectile _create(PhysicObject creator, Class<? extends Projectile> type) {
            try {
                return type.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    public Engine() {

    }

    /**
     * @param width in metters
     * @param height in metters
     */
    public Engine(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public ObjectController add(ControllableObject actor) {
        ObjectController act = new ObjectController(actor);
        actor.setController(act);
        _add(actor);
        controllRef.put(actor, act);
        return act;
    }

    public Engine add(PhysicObject actor) {
        return _add(actor);
    }

    public Engine addAction(Runnable action) {
        this.actions.add(new Action(System.currentTimeMillis(), action));
        return this;
    }

    public Engine addAction(Runnable action, long msToExecute) {
        this.actions.add(new Action(System.currentTimeMillis() + msToExecute, action));
        return this;
    }

    public Engine remove(PhysicObject actor) {
        deadObjects.add(actor);
        return this;
    }

    public Engine _add(PhysicObject actor) {
        final Vector2f pos = actor.getPosition();

        if (pos.x == 0f && pos.y == 0f) {
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
            if ((obj.getPosition().distance(center)) < (viewSize + obj.getRadius())) {
                list.add(obj);
            }
        }

        return list;

    }

    public List<Force> getVisibleForces(Vector2f center, Float viewSize) {
        if (center == null || viewSize == null) {
            throw new IllegalArgumentException("All params are required!");
        }

        viewSize = viewSize * 1.3f; //add 30% to view

        final List<Force> list = new ArrayList<Force>();

        for (Force obj : forces) {
            if ((obj.getPosition().distance(center)) < (viewSize + obj.getRadius())) {
                list.add(obj);
            }
        }

        return list;

    }

    public  <E extends PhysicObject> E getClosestActor(Vector2f center, Float viewSize, Class<E> type) {

        final List<Class<E>> allowedTypes = new ArrayList<Class<E>>(1);
        allowedTypes.add(type);

        return getClosestActor(center, viewSize, allowedTypes, new ArrayList<PhysicObject>());
    }

    public <E extends PhysicObject> E getClosestActor(Vector2f center, Float viewSize, Class<E>... allowedTypes) {
        List<Class<E>> list = Arrays.asList(allowedTypes);
        return getClosestActor(center, viewSize, list, new ArrayList<PhysicObject>());
    }

    public <E extends PhysicObject>  List<E> getActorsByType(Class<E> filter) {
        List<Class<E>> list = new ArrayList<Class<E>>();
        list.add(filter);
        return getActorsByType(list, Collections.EMPTY_LIST);

    }

    public <E extends PhysicObject> List<PhysicObject> getActorsByType(List<Class<E>> allowedTypes, List<PhysicObject> ignored) {

        if (allowedTypes == null) {
            allowedTypes = new ArrayList<Class<E>>(0);
        }

        final List<PhysicObject> result = new ArrayList<PhysicObject>();

        for (PhysicObject obj : actors) {
            if (!ignored.contains(obj)) {
                if (isClassAllowed(allowedTypes, obj.getClass())) {
                    result.add(obj);
                }
            }
        }

        return result;
    }

    public  <E extends PhysicObject> E getClosestActor(Vector2f center, Float viewSize, List<Class<E>> allowedTypes, List<PhysicObject> ignored) {
        if (center == null || viewSize == null) {
            throw new IllegalArgumentException("All params are required!");
        }

        if (allowedTypes == null) {
            allowedTypes = new ArrayList<Class<E>>(0);
        }

        E closest = null;
        viewSize = viewSize * 1.3f; //ads 30% to view
        Float closestDis = null;

        for (PhysicObject obj : actors) {
            if (!ignored.contains(obj)) {
                float dst = obj.getPosition().distance(center);
                if (isClassAllowed(allowedTypes, obj.getClass()) && dst < viewSize) {
                    if (closest == null || dst < closestDis) {
                        closest = (E) obj;
                        closestDis = dst;
                    }
                }
            }
        }

        return closest;
    }

    private <E extends PhysicObject> boolean isClassAllowed(List<Class<E>> allowedTypes, Class targetClass) {

        for (Class<E> ori : allowedTypes) {
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

        executeActions();

        final List<PhysicObject> fullList = new ArrayList<PhysicObject>();

        discoverActors(fullList, actors);

        for (PhysicObject obj : fullList) {

            obj.update(deltaTime);

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
                if (!pro.isAlive()) {
                    createExplosion(pro);
                } else if (pro.isTimedOut()) {
                    createExplosion(pro);
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

            if (avoidOjectsLeaveMap) {
                if (!mapIsLoop) {
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
                } else {
                    Vector2f pos = obj.getPosition();

                    if (pos.x < 0) {
                        pos.x = width;
                    } else if (pos.x > width) {
                        pos.x = width;
                    }

                    if (pos.y < 0) {
                        pos.y = height;
                    } else if (pos.y > height) {
                        pos.y = height;
                    }

                }
            }
        }

        IterableUtils.forEach(forces, new Closure<Force>() {
            @Override
            public void execute(Force input) {
                input.update(deltaTime);
            }
        });

        final List<Force> filteredForces = new ArrayList<Force>(forces);
        CollectionUtils.filter(filteredForces, new Predicate<Force>() {
            @Override
            public boolean evaluate(Force object) {
                return !object.isAlive();
            }
        });

        final List<PhysicObject> filteredActors = new ArrayList<PhysicObject>(fullList);
        CollectionUtils.filter(filteredActors, new Predicate<PhysicObject>() {
            @Override
            public boolean evaluate(PhysicObject object) {
                return !object.isAlive();
            }
        });

        deadObjects.addAll(filteredForces);
        deadObjects.addAll(filteredActors);

    }

    private void executeActions() {
        long now = System.currentTimeMillis();
        List<Action> toRemove = new ArrayList<Action>();
        for (Action ac : actions) {
            if (!ac.isExecuted()) {
                if (ac.getWhenExecute() < now) {
                    ac.getAction().run();
                    ac.setExecuted(true);
                    toRemove.add(ac);
                }
            } else {
                toRemove.add(ac);
            }
        }

        actions.removeAll(toRemove);

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

            if (forceField.length() > 0f) { // avoid NAN
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
        }

        return result;

    }

    private Vector2f calculateControl(ControllableObject obj) {

        if (controllRef.containsKey(obj)) {
            ObjectController act = controllRef.get(obj);
            return controllerResolver.calculate(this, obj, act);
        }

        return new Vector2f();
    }

    private void removeDeadObjects() {

        GameObject obj = null;

        while ((obj = deadObjects.poll()) != null) {

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
        deadObjects.add(p);

        if (exp != null) {
            forces.add(exp);
        }
    }

    public void setContactListener(ContactListener contactListener) {
        this.contactListener = contactListener;
    }

    public void setContactResolver(ContactResolver contactResolver) {
        this.contactResolver = contactResolver;
    }

    public void setProjectileResolver(ProjectileResolver projectileResolver) {
        this.projectileResolver = projectileResolver;
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
                contactListener.contact(ob, act, actors);
            }
        }
    }

    public ProjectileResolver getProjectileResolver() {
        return projectileResolver;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
