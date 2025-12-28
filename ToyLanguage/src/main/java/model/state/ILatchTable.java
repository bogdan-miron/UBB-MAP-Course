package model.state;

import java.util.Map;

public interface ILatchTable {
    // allocate a new latch with the given initial count
    // returns the location (address) of the latch
    int allocate(int count);

    // get the count at the specified location
    int get(int location);

    // set the count at the specified location
    void put(int location, int count);

    // check if a location exists in the table
    boolean isDefined(int location);

    // get the entire latch table content
    Map<Integer, Integer> getContent();

    // set the entire latch table content
    void setContent(Map<Integer, Integer> content);

    String toString();
}
