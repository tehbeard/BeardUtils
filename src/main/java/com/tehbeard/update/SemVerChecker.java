package com.tehbeard.update;


public final class SemVerChecker implements VersionChecker{

    public int checkVersion(String ourVersion, String otherVersion) {
        String[] v1 = ourVersion.split("\\-")[0].split("\\.");
        int v1M = Integer.parseInt(v1[0]);
        int v1m = Integer.parseInt(v1[1]);
        int v1p = Integer.parseInt(v1[2]);
        
        String[] v2 = otherVersion.split("\\-")[0].split("\\.");
        int v2M = Integer.parseInt(v2[0]);
        int v2m = Integer.parseInt(v2[1]);
        int v2p = Integer.parseInt(v2[2]);
        
        if(v2M > v1M){
            return 2;
        }
        if(v2M < v1M){
            return -2;
        }
        
        if(v2m > v1m){
            return 2;
        }
        if(v2m < v1m){
            return -2;
        }
        
        if(v2p > v1p){
            return 1;
        }
        if(v2p < v1p){
            return -1;
        }
        
        return 0;
    }
    
}