package com.tehbeard.utils;

/**
 * Simple Vector 
 * @author James
 */
public class Vec3 {
    public final double x,y,z;
    public final int bx,by,bz;
    
    public Vec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.bx = new Double(x).intValue();
        this.by = new Double(x).intValue();
        this.bz = new Double(x).intValue();
    }
    
    public Vec3 add(Vec3 vec){
        return new Vec3(x+vec.x,y+vec.y,z+vec.z);
    }
    
    public Vec3 sub(Vec3 vec){
        return new Vec3(x-vec.x,y-vec.y,z-vec.z);
    }
    
    public Vec3 div(Vec3 vec){
        return new Vec3(x/vec.x,y/vec.y,z/vec.z);
    }
    
    public Vec3 mul(Vec3 vec){
        return new Vec3(x*vec.x,y*vec.y,z*vec.z);
    }
    
    public Vec3 abs(){
        return new Vec3(Math.abs(x), Math.abs(y), Math.abs(z));
    }
    
    public int areaB(){
        return bx * by * bz;
    }
    
    public boolean isInAABB(Vec3 a, Vec3 b){
        Vec3 min = Vec3.getMin(a, b);
        Vec3 max = Vec3.getMax(a, b).add(new Vec3(1, 1, 1));
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
    }
    
     public static Vec3 getMin(Vec3 v1, Vec3 v2) {
        return new Vec3(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y),
                Math.min(v1.z, v2.z));
    }

    public static Vec3 getMax(Vec3 v1, Vec3 v2) {
        return new Vec3(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y),
                Math.max(v1.z, v2.z));
    }
}

