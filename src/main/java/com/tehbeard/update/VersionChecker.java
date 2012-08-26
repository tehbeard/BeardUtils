package com.tehbeard.update;

public interface VersionChecker {

    /**
     * compares two version strings
     * @param ourVersion version string for the plugin
     * @param otherVersion version string read from update site
     * @return integer determing value of comparison
     *  -2 : plugin is ahead of update (major version)
     *  -1 : plugin is ahead of update (minor version)
     *   0 : plugin and site are equal
     *   1 : plugin is behind update (minor version)
     *   2 : plugin is behind update (major version)
     */
    public int checkVersion(String ourVersion,String otherVersion);
}