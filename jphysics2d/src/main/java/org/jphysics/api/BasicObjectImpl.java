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
public class BasicObjectImpl implements BasicObject {

    @Override
    public <E extends BasicObjectImpl> boolean in(E... objs) {
        for (E e : objs) {
            if (this.equals(e)) {
                return true;
            }
        }
        return false;
    }

}
