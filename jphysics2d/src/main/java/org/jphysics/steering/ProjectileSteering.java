/**
 * ProjectileSteering.class
 */

package org.jphysics.steering;

import org.joml.Vector3f;
import org.jphysics.math.Vector2f;


public class ProjectileSteering extends Steering<ProjectileSteering> {
    
    private float accel = 50f;
    
    @Override
    protected Vector2f _calculate() {
        return new Vector2f(from.getDirection()).normalize().mul(accel);
    }

    public ProjectileSteering accel(float accel){
        this.accel = accel;
        return this;
    }
}
