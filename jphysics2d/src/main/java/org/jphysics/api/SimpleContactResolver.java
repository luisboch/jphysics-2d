/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.api;

import java.util.ArrayList;
import java.util.List;
import org.jphysics.math.Vector2f;

public class SimpleContactResolver implements ContactResolver {

    private final List<Result> resolved = new ArrayList<Result>();

    @Override
    public void clear() {
        resolved.clear();
    }

    public boolean isResolved(PhysicObject obj1, PhysicObject obj2) {
        for(Result r:resolved ){
            if( (r.reference1.equals(obj1) && r.reference2.equals(obj2)) || (r.reference1.equals(obj2) && r.reference2.equals(obj1))){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public Result resolve(PhysicObject obj1, PhysicObject obj2) {
        
        if(!isResolved(obj1, obj2)){
//            obj1.get
        }
        
        return new Result(obj1, obj2, new Vector2f(), new Vector2f());
    }

}
