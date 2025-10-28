package model.state;

import model.type.IType;
import model.value.IValue;

public interface ISymbolTable {
    boolean isDefined(String variableName);

    IType getType(String variableName);

    void update(String variableName, IValue value);

    IValue lookup(String variableName);
}
