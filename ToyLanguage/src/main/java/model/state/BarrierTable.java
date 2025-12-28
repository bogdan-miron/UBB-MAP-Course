package model.state;

import model.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarrierTable implements IBarrierTable {
    private Map<Integer, Pair<Integer, List<Integer>>> barrierTable;
    private int freeBarrierLocation;

    public BarrierTable() {
        this.barrierTable = new HashMap<>();
        this.freeBarrierLocation = 1; // barrier locations start from 1
    }

    @Override
    public synchronized int allocate(int capacity) {
        // create new barrier with capacity and empty waiting list
        List<Integer> emptyList = new ArrayList<>();
        Pair<Integer, List<Integer>> barrierEntry = new Pair<>(capacity, emptyList);

        barrierTable.put(freeBarrierLocation, barrierEntry);
        int allocatedLocation = freeBarrierLocation;
        freeBarrierLocation++;
        return allocatedLocation;
    }

    @Override
    public synchronized Pair<Integer, List<Integer>> get(int location) {
        return barrierTable.get(location);
    }

    @Override
    public synchronized void put(int location, Pair<Integer, List<Integer>> value) {
        barrierTable.put(location, value);
    }

    @Override
    public synchronized boolean isDefined(int location) {
        return barrierTable.containsKey(location);
    }

    @Override
    public synchronized Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return barrierTable;
    }

    @Override
    public synchronized void setContent(Map<Integer, Pair<Integer, List<Integer>>> content) {
        this.barrierTable = content;
    }

    @Override
    public String toString() {
        if (barrierTable.isEmpty()) {
            return "BarrierTable: {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("BarrierTable: {\n");

        for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : barrierTable.entrySet()) {
            sb.append("  ")
                    .append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue())
                    .append("\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
