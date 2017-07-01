/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import org.jphysics.Engine;
import org.jphysics.ObjectController;
import org.jphysics.math.Vector2f;

/**
 *
 * @author luis
 */
public interface ControllerResolver {
    Vector2f calculate(Engine engine, ControllableObject obj, ObjectController controller); 
}