package model.state;

import model.value.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class FileTable implements IFileTable {
    private final Map<StringValue, BufferedReader> fileTable;

    public FileTable() {
        fileTable = new HashMap<StringValue, BufferedReader>();
    }

    @Override
    public synchronized boolean isDefined(StringValue path) {
        return fileTable.containsKey(path);
    }

    @Override
    public synchronized BufferedReader lookup(StringValue path) {
        return fileTable.get(path);
    }

    @Override
    public synchronized void add(StringValue path, BufferedReader fd) {
        fileTable.put(path, fd);
    }

    @Override
    public synchronized void remove(StringValue filename) {
        fileTable.remove(filename);
    }

    @Override
    public String toString() {
        if (fileTable.isEmpty()) {
            return "FileTable: {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("FileTable: {\n");

        for (Map.Entry<StringValue, BufferedReader> entry : fileTable.entrySet()) {
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
