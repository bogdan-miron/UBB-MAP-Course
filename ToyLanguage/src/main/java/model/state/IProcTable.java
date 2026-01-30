package model.state;

import model.statement.IStatement;
import model.util.Pair;

import java.util.List;
import java.util.Map;

public interface IProcTable {
    void put(String procName, Pair<List<String>, IStatement> value);

    Pair<List<String>, IStatement> get(String procName);

    boolean isDefined(String procName);

    Map<String, Pair<List<String>, IStatement>> getContent();

    String toString();
}
