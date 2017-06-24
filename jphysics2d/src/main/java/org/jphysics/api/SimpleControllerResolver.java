/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import org.joml.Matrix3f;
import org.jphysics.Engine;
import org.jphysics.ObjectController;
import org.jphysics.math.Vector2f;

public class SimpleControllerResolver implements ControllerResolver {

    @Override
    public Vector2f calculate(Engine engine, ControllableObject obj, ObjectController controller) {
        final float angle = (float) Math.toRadians(3) * engine.getDeltaTime();
        
        Vector2f direction = obj.getDirection().normalize();

        if (controller.isLeft() && !controller.isRight()) {
            direction.rotateRad(angle);
        }

        if (!controller.isLeft() && controller.isRight()) {
            direction.rotateRad(-angle);
        }
//
//            if (act.isUp()) {
//                Vector3f scl = cal.setLength(obj.getAccel());
//                cal.add(scl);
//                cal.scl(obj.getDirection()).limit(obj.getMaxVel());
//            } else {
//                cal = new Vector3f(0, 0);
//            }
//
//            // Bombs?
//            if (act.isAction1()) {
//                final Projectile pro = obj.action1(this);
//                if (pro != null && canPlayerCreateProjectile(obj, obj.getAction1Type())) {
//                    createProjectile(obj, pro);
//                }
//            }
//            // Bombs?
//            if (act.isAction2()) {
//                final Projectile pro = obj.action2(this);
//                if (pro != null && canPlayerCreateProjectile(obj, obj.getAction2Type())) {
//                    createProjectile(obj, pro);
//                }
//            }
//            // Bombs?
//            if (act.isAction3()) {
//                final Projectile pro = obj.action3(this);
//                if (pro != null && canPlayerCreateProjectile(obj, obj.getAction3Type())) {
//                    createProjectile(obj, pro);
//                }
//            }
        return direction;
    }
}
