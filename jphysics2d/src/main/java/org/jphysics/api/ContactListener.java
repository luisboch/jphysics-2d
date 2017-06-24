/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import java.util.List;

/**
 *
 * @author luis
 */
public interface ContactListener {

    /**
     * Notify contact between two objects. 
     * Note, when object must be removed (like killed), this ojbect must be added to deadObjects.
     *
     * @param obj1
     * @param obj2
     * @param deadObjects
     */
    void contact(PhysicObject obj1, PhysicObject obj2, List<PhysicObject> deadObjects);
    
    
}
