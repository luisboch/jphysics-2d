/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import org.joml.Vector3f;

/**
 *
 * @author luis
 */
public interface Force extends GameObject{
    Force update(float deltaTime);
}
