package com.tehbeard.utils.sponge;

import com.flowpowered.math.vector.Vector3d;
import com.tehbeard.utils.Vec3;
import org.spongepowered.api.world.Location;

/**
 *
 * @author James
 */
public class SpongeUtils {
    
    public static Location vec3ToLocation(Vec3 vec){
        return new Location(null, vec3ToVector(vec));
    }
    
    public static Vector3d vec3ToVector(Vec3 vec){
        return new Vector3d(vec.x, vec.y, vec.z);
    }
    
    public static Vec3 vectorToVec3(Vector3d vec){
        return new Vec3(vec.getX(), vec.getY(), vec.getZ());
    }
}
