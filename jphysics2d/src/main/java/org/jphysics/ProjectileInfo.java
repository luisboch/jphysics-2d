/**
 * ProjectileInfo.class
 */
package org.jphysics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jphysics.projectile.Projectile;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 8, 2016
 */
class ProjectileInfo {
    final Map<Class<? extends Projectile>, Long> usedTypes = new ConcurrentHashMap<Class<? extends Projectile>, Long>();
}
