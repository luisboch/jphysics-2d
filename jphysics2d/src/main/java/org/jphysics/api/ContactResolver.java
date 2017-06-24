/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import org.jphysics.math.Vector2f;

/**
 *
 * @author luis
 */
public interface ContactResolver {
    
    // Called after GameLoop
    void clear();
            
            
    Result resolve(PhysicObject obj1, PhysicObject obj2);
//    boolean isResolved(PhysicObject obj1, PhysicObject obj2);
    
    public class Result{
        
        final GameObject reference1;
        final GameObject reference2;
        final Vector2f result1;
        final Vector2f result2;

        public Result(GameObject reference1, GameObject reference2, Vector2f result1, Vector2f result2) {
            this.reference1 = reference1;
            this.reference2 = reference2;
            this.result1 = result1;
            this.result2 = result2;
        }
        
        public GameObject getReference1() {
            return reference1;
        }

        public GameObject getReference2() {
            return reference2;
        }

        public Vector2f getResult1() {
            return result1;
        }

        public Vector2f getResult2() {
            return result2;
        }
        
    }
}
