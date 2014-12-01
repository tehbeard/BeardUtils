/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tehbeard.utils.tasks;

/**
 *
 * @author James
 */
public abstract class TaskManager {
    
    public enum TaskType{
        SYNC,
        ASYNC
    }
   
    
    private static TaskManager manager;
    public static TaskManager getManager(){
        return manager;
    }
    
    public static void setManager(TaskManager newManager){
        if(manager!=null){
            throw new IllegalArgumentException("Cannot set manager twice.");
        }
        manager = newManager;
    }
    
    public abstract Task addTask(Task task);
    public final Task addTask(TaskType type,long delay,long interval,boolean repeating,String name,Runnable runnable){
        return addTask(new Task(name, type, repeating, interval, delay, runnable));
    }
    
    public final Task addSyncTask(long delay,long interval,boolean repeating,String name,Runnable runnable){
        return addTask(TaskType.SYNC,delay,interval,repeating,name,runnable);
    }
    
    public final Task addASyncTask(long delay,long interval,boolean repeating,String name,Runnable runnable){
        return addTask(TaskType.ASYNC,delay,interval,repeating,name,runnable);
    }
    
    
    
}
