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
 public final class Task {
        public final String name;
        public final TaskManager.TaskType type;
        public final boolean repeating;
        public final long interval;
        public final long delay;
        public final Runnable code;

        public Task(String name, TaskManager.TaskType type, boolean repeating, long interval, long delay, Runnable code) {
            this.name = name;
            this.type = type;
            this.repeating = repeating;
            this.interval = interval;
            this.delay = delay;
            this.code = code;
        }
    }