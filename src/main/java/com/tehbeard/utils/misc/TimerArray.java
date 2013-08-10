package com.tehbeard.utils.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class to store objects with a time, to be retrieved later
 * 
 * @author James
 * 
 * @param <T>
 */
public class TimerArray<T> {

    private Map<T, Long> timings = new HashMap<T, Long>();

    /**
     * Add an object with the current system time
     * 
     * @param object
     */
    public void addNow(T object) {
        add(object, System.currentTimeMillis());
    }

    /**
     * Add object with a future time
     * 
     * @param object
     * @param millisecondsFuture
     */
    public void addFuture(T object, long millisecondsFuture) {
        add(object, System.currentTimeMillis() + millisecondsFuture);
    }

    /**
     * Add an oject with the specified time
     * 
     * @param object
     * @param time
     */
    public void add(T object, long time) {
        this.timings.put(object, time);
    }

    /**
     * Remove an object.
     * 
     * @param object
     */
    public void remove(T object) {
        this.timings.remove(object);
    }

    public void removeAll(List<T> object) {
        for (T o : object) {
            remove(o);
        }
    }

    /**
     * Find objects that were entered at least millisecondsPast ago
     * 
     * @param millisecondsPast
     * @return
     */
    public List<T> timeElapsed(Long millisecondsPast) {
        List<T> past = new ArrayList<T>();
        long t = System.currentTimeMillis();
        for (Entry<T, Long> entry : this.timings.entrySet()) {
            if ((entry.getValue() + millisecondsPast) < t) {
                past.add(entry.getKey());
            }
        }

        return past;
    }

    /**
     * Find objects that have elapsed, only usable with
     * {@link TimerArray#addFuture(T, long)}
     * 
     * @param millisecondsPast
     * @return
     */
    public List<T> timeElapsed() {
        return timeElapsed((long) 0);
    }

}
