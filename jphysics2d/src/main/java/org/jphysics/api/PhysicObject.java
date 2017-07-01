package org.jphysics.api;

import java.util.List;
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
public interface PhysicObject extends GameObject {
    
    Vector2f getVelocity();
    Vector2f getDirection();
    Vector2f getScale();
    
    float getMaxVelocity();
    float getRadius();
    List<PhysicObject> getChildren();
    PhysicObject getParent();
    PhysicObject setParent(PhysicObject obj);
    PhysicObject setVelocity(Vector2f newVelocity);
    PhysicObject setDirection(Vector2f newDirection);
    PhysicObject decreaseLife();
}
