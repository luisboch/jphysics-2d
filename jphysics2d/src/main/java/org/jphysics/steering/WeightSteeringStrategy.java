/**
 * SteeringStrategy.class
 */
package org.jphysics.steering;

import java.util.ArrayList;
import java.util.List;
import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;

/**
 * Manage Multiple Steerings. Example: <br>
 * BH 1 is [impact = 7] and it will result a value of 10;<br>
 * BH 2 is [impact = 10] and it will result a value of 20; <br>
 * This case will result ? (10*7 + * 20*10) / 17 = 81.7 <br>
 * if we add a new BH with impact = 30 and this results a value of 1 then:<br>
 * (10*7 + 20*10 + 1*30) / 47 = 6;<br>
 * <hr>
 * <b>Warning</b>: when result or impact is zero, then Steering and respective
 * impact will be ignored.<br>
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jul 31, 2016
 */
public class WeightSteeringStrategy extends Steering<WeightSteeringStrategy> {

    private final List<SteeringValue> behaviors = new ArrayList<SteeringValue>();

    /**
     *
     */
    public WeightSteeringStrategy() {
        super();
    }

    /**
     * Add an behavior to this Strategy. Remember, impact will affect all other
     * behaviors.
     *
     *
     * @param steering
     * @param impact
     * @return
     */
    public WeightSteeringStrategy add(Steering steering, Integer impact) {

        final SteeringValue val = new SteeringValue(steering, impact);

        val.strategy = this;
        val.behavior.from(from);
        behaviors.add(val);

        return this;
    }

    @Override
    public Vector2f _calculate() {
        int totalImpact = 0;
        final Vector2f result = new Vector2f(0, 0);
        final List<SteeringValue> calculated = new ArrayList<SteeringValue>();

        for (SteeringValue val : behaviors) {
            if (val.impact > 0) {
                Vector2f rs = val.behavior.calculate();
                if (rs.length() != 0f) {
                    totalImpact += val.impact;
                    calculated.add(val);
                    val.calculatedVal = rs;
                }
            }
        }

        for (SteeringValue val : calculated) {
            result.add(val.calculatedVal.mul(val.impact).mul(1f / totalImpact));
        }

        return result;
    }

    @Override
    public WeightSteeringStrategy from(PhysicObject from) {
        
        for (SteeringValue v : behaviors) {
            v.behavior.from(from);
        }
        
        return super.from(from);
    }

    public static class SteeringValue {

        private WeightSteeringStrategy strategy;
        private final Steering behavior;
        private Integer impact;
        private Vector2f calculatedVal;

        public SteeringValue(Steering behavior, Integer impact) {
            this.behavior = behavior;
            this.impact = impact;
        }

        public SteeringValue setImpact(Integer impact) {
            this.impact = impact;
            return this;
        }

        public WeightSteeringStrategy strategy() {
            return strategy;
        }

    }

}
