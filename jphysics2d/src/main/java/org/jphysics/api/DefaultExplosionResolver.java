/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import org.jphysics.math.Vector2f;

public class DefaultExplosionResolver implements ExplosionResolver {

    @Override
    public Force create(PhysicObject... ref) {
        return new SimpleTemporaryForce();
    }

}
