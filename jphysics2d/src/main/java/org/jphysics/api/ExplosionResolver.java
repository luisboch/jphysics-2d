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

    public static ExplosionResolver getDefault() {
        return new ExplosionResolver() {
            @Override
            public Force create(PhysicObject... ref) {
                return new TemporaryForce() {
                    @Override
                    public Vector2f getPosition() {
                        return new Vector2f();
                    }

                    @Override
                    public String toString() {
                        return "Empty Explosion";
                    }

                    @Override
                    public boolean isAlive() {
                        return false;
                    }

                    @Override
                    public Force update(float deltaTime) {
                        return this;
                    }


                    @Override
                    public float getMass() {
                        return 0f;
                    }

                    @Override
                    public GameObject setPosition(Vector2f pos) {
                        return this;
                    }
                };
            }
        };
    }
}
