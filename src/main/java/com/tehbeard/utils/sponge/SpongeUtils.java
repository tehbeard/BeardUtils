package com.tehbeard.utils.sponge;

import com.tehbeard.utils.Vec3;
import org.spongepowered.api.math.Vector3d;
import org.spongepowered.api.math.Vectors;
import org.spongepowered.api.world.Location;

/**
 *
 * @author James
 */
public class SpongeUtils {
    
    public Location vec3ToLocation(Vec3 vec){
        return new Location(null, vec3ToVector(vec));
    }
    
    public Vector3d vec3ToVector(Vec3 vec){
        return Vectors.create3d(vec.x, vec.y, vec.z);
    }
    
    public Vec3 vectorToVec3(Vector3d vec){
        return new Vec3(vec.getX(), vec.getY(), vec.getZ());
    }
}
