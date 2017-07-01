/**
 * ProjectileSteering.class
 */
package org.jphysics.steering;

import org.joml.Vector3f;
import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;

public class ProjectileSteering extends Steering<ProjectileSteering> {

    private float accel = 50f;
    private final Vector2f direction;

    public ProjectileSteering(Vector2f direction) {
        this.direction = new Vector2f(direction);
    }

    public ProjectileSteering(PhysicObject from) {
        this.from = from;
        this.direction = new Vector2f(from.getDirection());
    }

    @Override
    protected Vector2f _calculate() {
        return direction.normalize().mul(accel);
    }

    public ProjectileSteering accel(float accel) {
        this.accel = accel;
        return this;
    }
}
