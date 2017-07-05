/**
 * Projectile.class
 */
package org.jphysics.projectile;

import org.jphysics.api.GameObjectImpl;
import org.joml.Vector3f;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public abstract class Projectile extends GameObjectImpl {

    protected float explosionRadius = 100;
    protected float explosionForce = 100000; // The force that this Projectile cause;
    protected float lifeTime = 7f; // The life of this projectile (max will be 30 secs)
    protected float initialVelocity = 200f;
    protected GameObjectImpl from;

    public Projectile(float radius, float mass, float maxVel,
            float maxForce, float explosionRadius,
            float explosionForce, int lifeTime) {
        super(radius, mass, maxVel, maxForce);
        this.explosionRadius = explosionRadius;
        this.explosionForce = explosionForce;
        this.lifeTime = lifeTime > 30000 ? 30000 : lifeTime;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public float getInitialVelocity() {
        return initialVelocity;
    }

    public void setFrom(GameObjectImpl from) {
        this.from = from;
    }

    public void setInitialVelocity(float initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public boolean isTimedOut() {
        return this.lifeTime < 0f;
    }

    @Override
    public void update(float secs) {
        super.update(secs);
        this.lifeTime -= secs;
    }

}
