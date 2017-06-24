/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

/**
 *
 * @author luis
 */
public interface BasicObject {

    <E extends BasicObjectImpl> boolean in(E... objs);
    
}
