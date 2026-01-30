package model.state;

import model.statement.IStatement;
import model.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcTable implements IProcTable {
    private Map<String, Pair<List<String>, IStatement>> procTable;

    public ProcTable() {
        this.procTable = new HashMap<>();
    }

    @Override
    public synchronized void put(String procName, Pair<List<String>, IStatement> value) {
        procTable.put(procName, value);
    }

    @Override
    public synchronized Pair<List<String>, IStatement> get(String procName) {
        return procTable.get(procName);
    }

    @Override
    public synchronized boolean isDefined(String procName) {
        return procTable.containsKey(procName);
    }

    @Override
    public synchronized Map<String, Pair<List<String>, IStatement>> getContent() {
        return procTable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProcTable{");
        for (Map.Entry<String, Pair<List<String>, IStatement>> entry : procTable.entrySet()) {
            sb.append(entry.getKey());
            sb.append("(");
            sb.append(String.join(", ", entry.getValue().getFirst()));
            sb.append(") -> ");
            sb.append(entry.getValue().getSecond().toString());
            sb.append("; ");
        }
        sb.append("}");
        return sb.toString();
    }
}
