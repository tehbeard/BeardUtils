package com.tehbeard.utils;

/**
 * Simple Vector 
 * @author James
 */
public class Vec3 {
    public double x,y,z;
    
    public Vec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vec3 add(Vec3 vec){
        x+=vec.x;y+=vec.y;z+=vec.z;
        return this;
    }
    
    public Vec3 sub(Vec3 vec){
        x-=vec.x;y-=vec.y;z-=vec.z;
        return this;
    }
    
    public Vec3 div(Vec3 vec){
        x/=vec.x;y/=vec.y;z/=vec.z;
        return this;
    }
    
    public Vec3 mul(Vec3 vec){
        x*=vec.x;y*=vec.y;z*=vec.z;
        return this;
    }
    
    public Vec3 abs(){
        x = Math.abs(x);y=Math.abs(y);z=Math.abs(z);
        return this;
    }
    
    public Vec3 floor(){
        x = Math.floor(x);y=Math.floor(y);z=Math.floor(z);
        return this;
    }
    
    public int areaB(){

        return (int) (Math.floor(x) * Math.floor(y) * Math.floor(z));
    }

    @Override
    public String toString() {
        return "Vec3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
    
    @Override
    public Vec3 clone(){
        return new Vec3(x, y, z);
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

