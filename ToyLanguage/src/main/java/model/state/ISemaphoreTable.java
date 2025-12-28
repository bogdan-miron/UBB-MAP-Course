package model.state;

import java.util.Map;

public interface ISemaphoreTable {
    // allocate a new semaphore with the given initial permit count
    // returns the location (address) of the semaphore
    int allocate(int permits);

    // get the permit count at the specified location
    int get(int location);

    // set the permit count at the specified location
    void put(int location, int permits);

    // check if a location exists in the table
    boolean isDefined(int location);

    // get the entire semaphore table content
    Map<Integer, Integer> getContent();

    // set the entire semaphore table content
    void setContent(Map<Integer, Integer> content);

    String toString();
}
