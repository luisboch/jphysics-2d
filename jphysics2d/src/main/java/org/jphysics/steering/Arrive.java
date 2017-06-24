/**
 * Flee.class
 */
package org.jphysics.steering;

import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class Arrive extends Steering<Arrive> {

    private Vector2f target;
    private Float deceleration = 1f;

    public Arrive() {
    }

    public Arrive(Vector2f target) {
        target(target);
    }

    public Arrive(PhysicObject target) {
        target(target);
    }

    public final Arrive target(Vector2f target) {
        this.target = target;
        return this;
    }

    public final Arrive target(PhysicObject target) {
        this.target = target.getPosition();
        return this;
    }

    public Arrive deceleration(float d) {
        this.deceleration = d;
        return this;
    }

    @Override
    public Vector2f _calculate() {
        if (target == null) {
            throw new IllegalArgumentException("Target can't be null");
        }
        
        Vector2f toTarget = new Vector2f(target).sub(from.getPosition());
        float dist = toTarget.length();

        float speed = dist / deceleration;
        speed = Math.min(from.getMaxVelocity(), speed);
        
        Vector2f desired = toTarget.mul(speed).mul(1.0f / dist);
        desired.sub(from.getVelocity());
        
        return desired;
    }
}
