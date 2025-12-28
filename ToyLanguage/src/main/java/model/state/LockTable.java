package model.state;

import java.util.HashMap;
import java.util.Map;

public class LockTable implements ILockTable {
    private Map<Integer, Integer> lockTable;
    private int freeLockLocation;

    public LockTable() {
        this.lockTable = new HashMap<>();
        this.freeLockLocation = 1; // lock locations start from 1
    }

    @Override
    public synchronized int allocate() {
        // initialize lock as unlocked (-1)
        lockTable.put(freeLockLocation, -1);
        int allocatedLocation = freeLockLocation;
        freeLockLocation++;
        return allocatedLocation;
    }

    @Override
    public synchronized int get(int location) {
        return lockTable.get(location);
    }

    @Override
    public synchronized void put(int location, int threadId) {
        lockTable.put(location, threadId);
    }

    @Override
    public synchronized boolean isDefined(int location) {
        return lockTable.containsKey(location);
    }

    @Override
    public synchronized Map<Integer, Integer> getContent() {
        return lockTable;
    }

    @Override
    public synchronized void setContent(Map<Integer, Integer> content) {
        this.lockTable = content;
    }

    @Override
    public String toString() {
        if (lockTable.isEmpty()) {
            return "LockTable: {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LockTable: {\n");

        for (Map.Entry<Integer, Integer> entry : lockTable.entrySet()) {
            String status = entry.getValue() == -1 ? "FREE" : "Thread " + entry.getValue();
            sb.append("  ")
                .append(entry.getKey())
                .append(" -> ")
                .append(status)
                .append("\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
