package com.tehbeard.utils.cuboid;

import com.tehbeard.utils.Vec3;
import java.util.ArrayList;

/**
 * Represents a cuboid of space
 * 
 * @author James
 * 
 */
public class Cuboid {

    Vec3 v1, v2 = null;
    String world = null;

    public String getWorld() {
        return this.world;
    }

    public Cuboid() {

    }

    public Cuboid(String line) {
        setCuboid(line);
    }

    public void setCuboid(String line) {
        String[] l = line.split(":");
        this.world = l[0];
        this.v1 = new Vec3(Math.min(Integer.parseInt(l[1]), Integer.parseInt(l[4])), Math.min(Integer.parseInt(l[2]),
                Integer.parseInt(l[5])), Math.min(Integer.parseInt(l[3]), Integer.parseInt(l[6])));
        this.v2 = new Vec3(Math.max(Integer.parseInt(l[1]), Integer.parseInt(l[4])), Math.max(Integer.parseInt(l[2]),
                Integer.parseInt(l[5])), Math.max(Integer.parseInt(l[3]), Integer.parseInt(l[6])));

    }

    public void setCuboid(Vec3 c1, Vec3 c2, String world) {
        this.v1 = c1.clone().floor();
        this.v2 = c2.clone().floor();
        this.world = world;
    }

    public void setV1(Vec3 c1) {
        this.v1 = c1.clone().floor();
    }

    public void setV2(Vec3 c2) {
        this.v2 = c2.clone().floor();
    }

    /**
     * is a location inside this cuboid
     * 
     * @param l
     * @return true if inside
     */
    public boolean isInside(Vec3 l) {
        return l.isInAABB(this.v1, this.v2);

    }

    public ArrayList<String> getChunks() {
        ArrayList<String> chunks = new ArrayList<String>();
        if ((this.v1 != null) && (this.v2 != null)) {
            int cx1 = (int)(this.v1.x / 16);
            int cz1 = (int)(this.v1.z / 16);
            int cx2 = (int)(this.v2.x / 16);
            int cz2 = (int)(this.v2.z / 16);
            int cx, cz;

            for (cx = cx1; cx <= cx2; cx++) {
                for (cz = cz1; cz <= cz2; cz++) {
                    chunks.add("" + this.world + "," + cx + "," + cz);
                }
            }
        }
        return chunks;

    }

    public Vec3[] getCorners() {
        Vec3[] c = new Vec3[2];
        c[0] = this.v1;
        c[1] = this.v2;
        return c;
    }

    @Override
    public String toString() {
        return this.world + ":" + this.v1.x + ":" + this.v1.y + ":" + this.v1.z + ":"
                + this.v2.x + ":" + this.v2.y + ":" + this.v2.z;
    }

    public int size() {
        Vec3 v3 = this.v2.sub(this.v1).add(new Vec3(1,1,1));
        return v3.areaB();
    }
}
