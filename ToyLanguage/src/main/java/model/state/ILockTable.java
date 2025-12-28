package model.state;

import java.util.Map;

public interface ILockTable {
    // allocate a new lock, initialized to -1 (unlocked)
    // returns the location (address) of the lock
    int allocate();

    // get the thread ID (or -1 if unlocked) at the specified location
    int get(int location);

    // set the thread ID at the specified location
    void put(int location, int threadId);

    // check if a location exists in the table
    boolean isDefined(int location);

    // get the entire lock table content
    Map<Integer, Integer> getContent();

    // set the entire lock table content
    void setContent(Map<Integer, Integer> content);

    String toString();
}
