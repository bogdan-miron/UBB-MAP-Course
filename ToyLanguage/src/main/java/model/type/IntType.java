package model.type;

import model.value.IValue;
import model.value.IntValue;

public class IntType implements IType {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return o instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }
}
