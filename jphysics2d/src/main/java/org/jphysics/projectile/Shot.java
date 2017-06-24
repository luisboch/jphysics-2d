/**
 * TrackMissile.class
 */
package org.jphysics.projectile;

import org.jphysics.api.GameObjectImpl;
import org.joml.Vector3f;
import org.jphysics.steering.Steering;

public class Shot extends SimpleShot {

    protected GameObjectImpl target;
    protected int minProximity;

    public Shot() {
        super(null, null);
    }

    public Shot(String image, float radius, float mass, float maxVel, float maxForce, float explosionRadius, float explosionForce, int lifeTime) {
        super(image, radius, mass, maxVel, maxForce, explosionRadius, explosionForce, lifeTime);
    }

    
    public Shot(Steering steering, GameObjectImpl from, GameObjectImpl target, int minProximity) {
        super(steering, from);
        init(target, minProximity);
    }

    
    protected void init(GameObjectImpl target, int minProximity) {
        this.target = target;
        this.minProximity = minProximity;
    }
    
}
