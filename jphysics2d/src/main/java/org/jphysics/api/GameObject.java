/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import org.jphysics.math.Vector2f;


/**
 *
 * @author luis
 */
public interface GameObject extends BasicObject {

    float getMass();
    Vector2f getPosition();
    GameObject setPosition(Vector2f pos);

    boolean isAlive();
    

    
}
