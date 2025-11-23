package model.state;

import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class Heap implements IHeap {
    private Map<Integer, IValue> heap;
    private int freeLocation;

    public Heap() {
        this.heap = new HashMap<>();
        this.freeLocation = 1; // addresses start from 1, 0 is null
    }

    @Override
    public int allocate(IValue value) {
        heap.put(freeLocation, value);
        int allocatedAddress = freeLocation;
        freeLocation++;
        return allocatedAddress;
    }

    @Override
    public IValue get(int address) {
        return heap.get(address);
    }

    @Override
    public void put(int address, IValue value) {
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public void remove(int address) {
        heap.remove(address);
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return heap;
    }

    @Override
    public void setContent(Map<Integer, IValue> content) {
        this.heap = content;
    }

    @Override
    public String toString() {
        if (heap.isEmpty()) {
            return "Heap: {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Heap: {\n");

        for (Map.Entry<Integer, IValue> entry : heap.entrySet()) {
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