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
public class Flee extends Steering<Flee> {

    private Vector2f target;
    private Float panicDistance = 300f;

    public Flee() {
    }

    public Flee(PhysicObject target) {
        target(target);
    }

    public Flee(Vector2f target) {
        target(target);
    }

    public Flee target(PhysicObject target) {
        this.target = target.getPosition();
        return this;
    }

    public Flee target(Vector2f target) {
        this.target = target;
        return this;
    }

    public Flee panicDist(float dist) {
        this.panicDistance = dist;
        return this;
    }

    @Override
    public Vector2f _calculate() {

        if (target == null) {
            throw new IllegalStateException("Target can't be null");
        }

        if (new Vector2f(from.getPosition()).distance(target) > panicDistance) {
            return new Vector2f(0, 0);
        }

        final Vector2f desired = new Vector2f(from.getPosition()).sub(target);

        desired.normalize();
        desired.mul(from.getMaxVelocity());
        
        return desired.sub(from.getVelocity());
    }
}
