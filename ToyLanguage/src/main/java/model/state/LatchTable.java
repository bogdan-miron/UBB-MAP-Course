package model.state;

import java.util.HashMap;
import java.util.Map;

public class LatchTable implements ILatchTable {
    private Map<Integer, Integer> latchTable;
    private int freeLatchLocation;

    public LatchTable() {
        this.latchTable = new HashMap<>();
        this.freeLatchLocation = 1; // latch locations start from 1
    }

    @Override
    public synchronized int allocate(int count) {
        latchTable.put(freeLatchLocation, count);
        int allocatedLocation = freeLatchLocation;
        freeLatchLocation++;
        return allocatedLocation;
    }

    @Override
    public synchronized int get(int location) {
        return latchTable.get(location);
    }

    @Override
    public synchronized void put(int location, int count) {
        latchTable.put(location, count);
    }

    @Override
    public synchronized boolean isDefined(int location) {
        return latchTable.containsKey(location);
    }

    @Override
    public synchronized Map<Integer, Integer> getContent() {
        return latchTable;
    }

    @Override
    public synchronized void setContent(Map<Integer, Integer> content) {
        this.latchTable = content;
    }

    @Override
    public String toString() {
        if (latchTable.isEmpty()) {
            return "LatchTable: {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LatchTable: {\n");

        for (Map.Entry<Integer, Integer> entry : latchTable.entrySet()) {
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
