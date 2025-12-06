package model.gc;

import model.state.IHeap;
import model.state.ISymbolTable;
import model.value.IValue;
import model.value.RefValue;

import java.util.*;
import java.util.stream.Collectors;

public class GarbageCollector {
    public Map<Integer, IValue> collect(ISymbolTable symTable, IHeap heap) {
        // get addresses directly referenced from the symbol table
        List<Integer> rootAddresses = getAddressesFromSymbolTable(symTable);

        // find all reachable addresses
        Set<Integer> reachableAddresses = markReachable(rootAddresses, heap);

        return sweep(reachableAddresses, heap);
    }

    public Map<Integer, IValue> collectConcurrent(List<ISymbolTable> symTables, IHeap heap) {
        // get addresses directly referenced from all symbol tables
        List<Integer> rootAddresses = getAddressesFromSymbolTables(symTables);

        // find all reachable addresses
        Set<Integer> reachableAddresses = markReachable(rootAddresses, heap);

        return sweep(reachableAddresses, heap);
    }

    private List<Integer> getAddressesFromSymbolTable(ISymbolTable symTable) {
        Collection<IValue> values = symTable.getContent().values();

        return values.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    private List<Integer> getAddressesFromSymbolTables(List<ISymbolTable> symTables) {
        // Collect addresses from all symbol tables
        return symTables.stream()
                .flatMap(symTable -> symTable.getContent().values().stream())
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    private Set<Integer> markReachable(List<Integer> rootAddresses, IHeap heap) {
        Set<Integer> reachableAddresses = new HashSet<>();
        Queue<Integer> toProcess = new LinkedList<>(rootAddresses);

        while (!toProcess.isEmpty()) {
            Integer address = toProcess.poll();

            // skip null references (address 0) and already marked addresses
            if (address == 0 || reachableAddresses.contains(address)) {
                continue;
            }

            // mark this address as reachable
            reachableAddresses.add(address);

            // if this address contains a RefValue, add its target to the queue
            if (heap.isDefined(address)) {
                IValue value = heap.get(address);
                if (value instanceof RefValue) {
                    RefValue refValue = (RefValue) value;
                    toProcess.add(refValue.getAddr());
                }
            }
        }

        return reachableAddresses;
    }

    private Map<Integer, IValue> sweep(Set<Integer> reachableAddresses, IHeap heap) {
        Map<Integer, IValue> heapContent = heap.getContent();

        return heapContent.entrySet().stream()
                .filter(entry -> reachableAddresses.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public GCStats getStatistics(int beforeSize, int afterSize) {
        return new GCStats(beforeSize, afterSize);
    }

    public static class GCStats {
        private final int entriesBefore;
        private final int entriesAfter;
        private final int entriesCollected;

        public GCStats(int before, int after) {
            this.entriesBefore = before;
            this.entriesAfter = after;
            this.entriesCollected = before - after;
        }

        public int getEntriesBefore() {
            return entriesBefore;
        }

        public int getEntriesAfter() {
            return entriesAfter;
        }

        public int getEntriesCollected() {
            return entriesCollected;
        }

        @Override
        public String toString() {
            return String.format("GC: %d entries → %d entries (%d collected)",
                    entriesBefore, entriesAfter, entriesCollected);
        }
    }
}
