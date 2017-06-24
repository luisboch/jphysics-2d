/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import org.jphysics.math.Vector2f;


public class SimpleTemporaryForce extends BasicObjectImpl implements TemporaryForce {
    protected boolean isAlive = true;
    protected float mass;
    protected final Vector2f position = new Vector2f();
    
    @Override
    public boolean isAlive() {
        return this.isAlive;
    }

    @Override
    public Force update(float deltaTime) {
        return this;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    @Override
    public GameObject setPosition(Vector2f pos) {
        this.position.set(pos);
        return this;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
}
