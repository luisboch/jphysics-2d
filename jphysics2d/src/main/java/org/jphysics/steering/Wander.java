/**
 * Wander.class
 */
package org.jphysics.steering;

import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 2, 2016
 */
public class Wander extends Steering<Wander> {

    private Float circleDistance = 50f;
    private Float circleRadius = 50f;
    private Float wanderAngle = 0f;
    private Float angleChange = 60f;

    public Wander circleDistance(float val) {
        circleDistance = val;
        return this;
    }

    public Wander circleRadius(float val) {
        circleRadius = val;
        return this;
    }

    public Wander wanderAngle(float val) {
        wanderAngle = val;
        return this;
    }

    public Wander angleChange(float val) {
        angleChange = val;
        return this;
    }

    @Override
    public Wander from(PhysicObject from) {
        return super.from(from);
    }

    @Override
    public Vector2f _calculate() {

        final Vector2f cirCenter = new Vector2f(from.getVelocity()).normalize();
        cirCenter.mul(circleDistance);

        final Vector2f dst = new Vector2f(1, 1);
        dst.set(circleRadius);
        dst.setAngle((float) Math.toDegrees(wanderAngle));

        wanderAngle += (float) (Math.random() * angleChange - angleChange * 0.5);

        System.out.println("Angle: " + wanderAngle);

        final Vector2f wanderForce = cirCenter.add(dst);

        System.out.println("Force:  " + wanderForce);
//        System.out.println("Wander Angle: " + wanderAngle + ", From Angle: " + from.getVelocity().angle()+ ", WanderForce" + wanderForce);
        return wanderForce.sub(from.getVelocity());
//}
    }

    @Override
    public String toString() {
        return "Wander{" + "circleDistance=" + circleDistance + ", circleRadius=" + circleRadius + ", wanderAngle=" + wanderAngle + ", angleChange=" + angleChange + '}';
    }
}
