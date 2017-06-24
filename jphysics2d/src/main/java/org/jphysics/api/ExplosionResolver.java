package org.jphysics.api;

import org.jphysics.math.Vector2f;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author luis
 */
public interface ExplosionResolver {
    Force create(PhysicObject... ref);
}
