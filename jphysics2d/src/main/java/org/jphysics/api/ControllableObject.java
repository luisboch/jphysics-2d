/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import org.jphysics.ObjectController;

/**
 *
 * @author luis
 */
public interface ControllableObject extends PhysicObject {
    ObjectController getController();
    void setController(ObjectController controller);
}
