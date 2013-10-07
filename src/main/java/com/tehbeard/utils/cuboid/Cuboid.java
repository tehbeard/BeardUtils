package com.tehbeard.utils.cuboid;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Represents a cuboid of space
 * 
 * @author James
 * 
 */
public class Cuboid {

    Vector v1, v2 = null;
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
        this.v1 = new Vector(Math.min(Integer.parseInt(l[1]), Integer.parseInt(l[4])), Math.min(Integer.parseInt(l[2]),
                Integer.parseInt(l[5])), Math.min(Integer.parseInt(l[3]), Integer.parseInt(l[6])));
        this.v2 = new Vector(Math.max(Integer.parseInt(l[1]), Integer.parseInt(l[4])), Math.max(Integer.parseInt(l[2]),
                Integer.parseInt(l[5])), Math.max(Integer.parseInt(l[3]), Integer.parseInt(l[6])));

    }

    public void setCuboid(Vector c1, Vector c2, String world) {
        this.v1 = c1;
        this.v2 = c2;
        this.world = world;
    }

    public void setV1(Vector c1) {
        this.v1 = c1;
    }

    public void setV2(Vector c2) {
        this.v2 = c2;
    }

    /**
     * is a location inside this cuboid
     * 
     * @param l
     * @return true if inside
     */
    public boolean isInside(Location l) {
        return (l.toVector().isInAABB(Vector.getMinimum(this.v1, this.v2),
                Vector.getMaximum(this.v1, this.v2).add(new Vector(1, 1, 1))) && l.getWorld().getName()
                .equals(this.world));

    }

    public ArrayList<String> getChunks() {
        ArrayList<String> chunks = new ArrayList<String>();
        if ((this.v1 != null) && (this.v2 != null)) {
            int cx1 = this.v1.getBlockX() / 16;
            int cz1 = this.v1.getBlockZ() / 16;
            int cx2 = this.v2.getBlockX() / 16;
            int cz2 = this.v2.getBlockZ() / 16;
            int cx, cz;

            for (cx = cx1; cx <= cx2; cx++) {
                for (cz = cz1; cz <= cz2; cz++) {
                    chunks.add("" + this.world + "," + cx + "," + cz);
                }
            }
        }
        return chunks;

    }

    public Vector[] getCorners() {
        Vector[] c = new Vector[2];
        c[0] = this.v1;
        c[1] = this.v2;
        return c;
    }

    @Override
    public String toString() {
        return this.world + ":" + this.v1.getBlockX() + ":" + this.v1.getBlockY() + ":" + this.v1.getBlockZ() + ":"
                + this.v2.getBlockX() + ":" + this.v2.getBlockY() + ":" + this.v2.getBlockZ();
    }

    public int size() {
        Vector v3 = this.v2.clone().subtract(this.v1.clone());
        return (v3.getBlockX() + 1) * (v3.getBlockY() + 1) * (v3.getBlockZ() + 1);
    }
}
