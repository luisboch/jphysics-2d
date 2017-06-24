/**
 * TrackMissile.class
 */
package org.jphysics.projectile;

import org.jphysics.api.GameObjectImpl;
import org.jphysics.steering.Steering;

public class SimpleShot extends Projectile {

    protected GameObjectImpl target;

    // Created when receive update from network (ignore steering and from, it will be controled by server)
    public SimpleShot() {
        this(null, null);
    }

    public SimpleShot(Steering steering, GameObjectImpl from) {
        super("disasteroids/fire.png", 5, 3f, 2000, 400, 20f, 1000f, 15000);
        init(steering, from);

    }

    public SimpleShot(String image, float radius, float mass, float maxVel, float maxForce, float explosionRadius, float explosionForce, int lifeTime) {
        super(image, radius, mass, maxVel, maxForce, explosionRadius, explosionForce, lifeTime);
    }
    
    

    private void init(Steering steering, GameObjectImpl from) {
        setSteering(steering);
        setFrom(from);
    }

}
