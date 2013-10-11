package com.tehbeard.utils.bungee;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 *
 * @author James
 */
public class ByteBackedDataOutputStream extends DataOutputStream{
    
    private ByteArrayOutputStream backing;
    private ByteBackedDataOutputStream(ByteArrayOutputStream stream){
        super(stream);
        backing = stream;
    }
    
    public byte[] toByteArray(){
        return backing.toByteArray();
    }
    
    public static ByteBackedDataOutputStream getDos(){
        return new ByteBackedDataOutputStream(new ByteArrayOutputStream());
    }
}
