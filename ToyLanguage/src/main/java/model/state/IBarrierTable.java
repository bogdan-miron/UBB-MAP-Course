package model.state;

import model.util.Pair;

import java.util.List;
import java.util.Map;

public interface IBarrierTable {
    // allocate a new barrier with the given capacity
    // returns the location (address) of the barrier
    int allocate(int capacity);

    // get the barrier entry (capacity, waiting list) at the specified location
    Pair<Integer, List<Integer>> get(int location);

    // update the barrier entry at the specified location
    void put(int location, Pair<Integer, List<Integer>> value);

    // check if a location exists in the table
    boolean isDefined(int location);

    // get the entire barrier table content
    Map<Integer, Pair<Integer, List<Integer>>> getContent();

    // set the entire barrier table content
    void setContent(Map<Integer, Pair<Integer, List<Integer>>> content);

    String toString();
}
