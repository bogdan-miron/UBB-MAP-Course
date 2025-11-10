package model.type;

import model.value.BooleanValue;
import model.value.IValue;

public class BoolType implements IType {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return o instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public IValue defaultValue() {
        return new BooleanValue(false);
    }
}
