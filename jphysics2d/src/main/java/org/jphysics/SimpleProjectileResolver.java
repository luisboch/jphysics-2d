/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics;

import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;
import org.jphysics.projectile.Projectile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luis
 */
public abstract class SimpleProjectileResolver implements ProjectileResolver {

    private final Map<Class<? extends Projectile>, Long> timeToFire = new ConcurrentHashMap<Class<? extends Projectile>, Long>();
    private final Map<PhysicObject, Long> timeFired = new HashMap<PhysicObject, Long>();

    @Override
    public boolean canCreateProjectile(Class<? extends Projectile> type, PhysicObject creator) {
        final Long lastFire = timeFired.get(creator);

        if (lastFire != null) {
            long currenMs = System.currentTimeMillis();

            final Long reloadTime = timeToFire.get(type);

            if (reloadTime == null) {
                return true;
            } else if ((currenMs - lastFire) > reloadTime) {
                return true;
            }

            return false;
        }

        return true;
    }

    /**
     * @param creator
     * @param type
     * @return
     */
    @Override
    public Projectile create(PhysicObject creator, Class<? extends Projectile> type) {

        Vector2f dir = creator.getDirection().normalize();
        try {
            final Projectile projectile = _create(creator, type);

            projectile.setDirection(dir);
            projectile.setPosition(creator.getPosition().add(dir.mul((creator.getRadius() + projectile.getRadius()) + 5f)));
            projectile.setVelocity(dir.normalize().mul(projectile.getInitialVelocity()).add(creator.getVelocity()));
            long created = System.currentTimeMillis();
            timeFired.put(creator, created);

            return projectile;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public void setReloadTime(long reload, Class<? extends Projectile> type) {
        timeToFire.put(type, reload);
    }

    protected abstract Projectile _create(PhysicObject creator, Class<? extends Projectile> type);

}
