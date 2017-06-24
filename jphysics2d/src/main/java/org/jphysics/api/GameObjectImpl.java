/**
 * VectorObject.class
 */
package org.jphysics.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jphysics.math.Vector2f;
import org.jphysics.steering.Steering;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public abstract class GameObjectImpl implements PhysicObject {

    private final float radius;
    private final float mass;
    private final float maxVel;
    private final float accel = 300;
    private final float maxForce;
    private final Vector2f position = new Vector2f();
    private final Vector2f velocity = new Vector2f();
    private final Vector2f direction = new Vector2f();
    private final Vector2f scale = new Vector2f();
    protected Vector2f lastWorldPos = new Vector2f();
    private Vector2f pivot = new Vector2f(0f, 0f);

    private final String type;

    private Steering steering;

    protected int health = -999999;
    protected int initHealth = health;

    private final List<PhysicObject> listActorObject;
    private PhysicObject parent;

    public GameObjectImpl(float radius, float mass) {
        this.radius = radius;
        this.mass = mass;
        type = _getType();
        maxVel = 500f;
        maxForce = 350f;
        listActorObject = new ControlList(this);
    }

    public GameObjectImpl(float radius, float mass, float maxVel) {
        this.radius = radius;
        this.mass = mass;
        this.type = _getType();
        this.maxVel = maxVel;
        maxForce = 350f;
        listActorObject = new ControlList(this);
    }

    public GameObjectImpl(float radius, float mass, float maxVel, float maxForce) {
        this.radius = radius;
        this.mass = mass;
        this.type = _getType();
        this.maxVel = maxVel;
        this.maxForce = maxForce;
        listActorObject = new ControlList(this);
    }

    protected void tick() {
    }

    public Vector2f getLastWorldPos() {
        return lastWorldPos;
    }

    public void applyForce(Float force) {
        if (this.health > 0) {

            float f = force;

            if (f > 0) {
                float h = this.health;
                h = h - f;
                h = h < 0 ? 0 : h;

                this.health = (int) h;
            }
        }
    }

    public final void setHealth(Integer health) {
        this.health = health;
        this.initHealth = health;
    }

    public Vector2f getPivot() {
        return pivot;
    }

    public void setPivot(Vector2f pivot) {
        this.pivot = pivot;
    }

    private String _getType() {
        return getClass().getSimpleName().toUpperCase();
    }

    public String getType() {
        return type;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    @Override
    public GameObjectImpl setPosition(Vector2f pos) {
        position.set(pos);
        return this;
    }

    public GameObjectImpl setDirection(Vector2f pos) {
        direction.set(pos);
        return this;
    }

    @Override
    public GameObjectImpl setVelocity(Vector2f pos) {
        velocity.set(pos);
        return this;
    }

    @Override
    public Vector2f getVelocity() {
        return new Vector2f(velocity);
    }

    @Override
    public float getMaxVelocity() {
        return maxVel;
    }

    public float getMaxVel() {
        return maxVel;
    }

    public float getMaxForce() {
        return maxForce;
    }

    @Override
    public Vector2f getDirection() {
        return new Vector2f(direction);
    }

    @Override
    public Vector2f getScale() {
        return new Vector2f(scale);
    }

    public float getAccel() {
        return accel;
    }

    public void setSteering(Steering steering) {
        this.steering = steering;
        if (steering != null) {
            steering.from(this);
        }
    }

    public Steering getSteering() {
        return steering;
    }

    public void contact(GameObjectImpl e) {
    }

    public byte getAnnimationState() {
        return 0;
    }

    public void setAnnimationState(byte val) {
        // Ignored...
    }

    public final List<PhysicObject> getListActorObject() {
        return listActorObject;
    }

    @Override
    public final List<PhysicObject> getChildren() {
        return getListActorObject();
    }

    public void setListActorObject(List<GameObjectImpl> listActorObject) {
        this.listActorObject.clear();
        this.listActorObject.addAll(listActorObject);
    }

    public void setParent(GameObjectImpl parent) {
        this.parent = parent;
    }

    @Override
    public PhysicObject getParent() {
        return parent;
    }

    public float getHealthPercent() {
        return (float) health / (float) initHealth;
    }

    /**
     *
     * @param percent value from 0 to 100
     */
    public void setHealthByPercent(byte percent) {
        setHealthByPercent(((float) percent / 100f));
    }

    public void setHealthByPercent(float percent) {
        health = (int) ((float) initHealth * percent);
    }

    private static class ControlList extends ArrayList<PhysicObject> {

        private final PhysicObject _instance;

        private ControlList(PhysicObject ref) {
            _instance = ref;
        }

        @Override
        public boolean add(PhysicObject e) {
            if (e == null) {
                return false;
            }

            e.setParent(_instance);
            return super.add(e);
        }

        @Override
        public boolean addAll(Collection<? extends PhysicObject> c) {

            if (c == null) {
                return false;
            }

            c.stream().filter((a) -> (a != null)).forEach((a) -> {
                a.setParent(_instance);
            });
            return super.addAll(c);
        }

        @Override
        public void clear() {

            this.forEach((a) -> {
                a.setParent(null);
            });

            super.clear();
        }

        @Override
        public boolean remove(Object o) {

            if (o == null) {
                return false;
            }

            if (o instanceof PhysicObject) {
                ((PhysicObject) o).setParent(null);
            }

            return super.remove(o);
        }

        @Override
        public boolean removeAll(Collection<?> c) {

            if (c == null) {
                return false;
            }

            c.stream().filter((o) -> (o instanceof PhysicObject)).forEach((o) -> {
                ((PhysicObject) o).setParent(null);
            });

            return super.removeAll(c);
        }

    }

    @Override
    public boolean isAlive() {
        return health == -999999 || health > 0;
    }

    @Override
    public PhysicObject setParent(PhysicObject obj) {
        this.parent = obj;
        return this;
    }

}
