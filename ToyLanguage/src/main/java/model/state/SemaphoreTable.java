package model.state;

import java.util.HashMap;
import java.util.Map;

public class SemaphoreTable implements ISemaphoreTable {
    private Map<Integer, Integer> semaphoreTable;
    private int freeSemaphoreLocation;

    public SemaphoreTable() {
        this.semaphoreTable = new HashMap<>();
        this.freeSemaphoreLocation = 1;
    }

    @Override
    public synchronized int allocate(int permits) {
        semaphoreTable.put(freeSemaphoreLocation, permits);
        int allocatedLocation = freeSemaphoreLocation;
        freeSemaphoreLocation++;
        return allocatedLocation;
    }

    @Override
    public synchronized int get(int location) {
        return semaphoreTable.get(location);
    }

    @Override
    public synchronized void put(int location, int permits) {
        semaphoreTable.put(location, permits);
    }

    @Override
    public synchronized boolean isDefined(int location) {
        return semaphoreTable.containsKey(location);
    }

    @Override
    public synchronized Map<Integer, Integer> getContent() {
        return semaphoreTable;
    }

    @Override
    public synchronized void setContent(Map<Integer, Integer> content) {
        this.semaphoreTable = content;
    }

    @Override
    public String toString() {
        if (semaphoreTable.isEmpty()) {
            return "SemaphoreTable: {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SemaphoreTable: {\n");

        for (Map.Entry<Integer, Integer> entry : semaphoreTable.entrySet()) {
            sb.append("  ")
                    .append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue())
                    .append(" permits\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
