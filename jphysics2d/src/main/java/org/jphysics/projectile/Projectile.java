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

    private static Map<Class<? extends Projectile>, Float> reloadTimeConfig = new HashMap<Class<? extends Projectile>, Float>();

    static {
        reloadTimeConfig.put(Shot.class, 500f);
        reloadTimeConfig.put(SimpleShot.class, 300f);
        reloadTimeConfig.put(MineShot.class, 1000f);
    }
    
    protected float explosionRadius = 100;
    protected float explosionForce = 100000; // The force that this Projectile cause;
    protected int lifeTime = 7000; // The life of this projectile (max will be 30 secs)
    protected boolean canExplode = false;
    protected float initialVelocity = 200f;
    protected GameObjectImpl from;
    private final String image;

    public Projectile(final String image, float radius, float mass, float maxVel,
            float maxForce, float explosionRadius,
            float explosionForce, int lifeTime) {
        super(radius, mass, maxVel, maxForce);
        this.image = image;
        this.explosionRadius = explosionRadius;
        this.explosionForce = explosionForce;
        this.lifeTime = lifeTime > 30000 ? 30000 : lifeTime;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public boolean canExplodeNow(boolean timedout) {
        return canExplode;
    }


    public float getInitialVelocity() {
        return initialVelocity;
    }

    public void setFrom(GameObjectImpl from) {
        this.from = from;
    }

    @Override
    public void contact(GameObjectImpl e) {

        super.contact(e);
        if (e != null && from != null && !from.equals(e) && (e.getParent() == null || e.getParent() != from)) {
            canExplode = true;
        }
    }

    public static Map<Class<? extends Projectile>, Float> getReloadTimeConfig() {
        return reloadTimeConfig;
    }

    public void setInitialVelocity(float initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

}
