package model.state;

import model.type.IType;
import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements ISymbolTable {
    private Map<String, IValue> symTable;

    public SymbolTable() {
        this.symTable = new HashMap<String, IValue>();
    }

    @Override
    public boolean isDefined(String variableName) {
        return symTable.containsKey(variableName);
    }

    @Override
    public IType getType(String variableName) {
        if (!isDefined(variableName)) {
            throw new IllegalArgumentException(variableName + " is not defined");
        }
        return symTable.get(variableName).getType();
    }

    @Override
    public void update(String variableName, IValue value) {
        symTable.put(variableName, value);
    }

    @Override
    public IValue lookup(String variableName) {
        return symTable.get(variableName);
    }

    @Override
    public Map<String, IValue> getContent() {
        return symTable;
    }

    @Override
    public ISymbolTable clone() {
        SymbolTable clonedTable = new SymbolTable();
        for (Map.Entry<String, IValue> entry : this.symTable.entrySet()) {
            clonedTable.update(entry.getKey(), entry.getValue());
        }
        return clonedTable;
    }

    @Override
    public String toString() {
        if (symTable.isEmpty()) {
            return "SymbolTable: {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SymbolTable: {\n");

        for (Map.Entry<String, IValue> entry : symTable.entrySet()) {
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
