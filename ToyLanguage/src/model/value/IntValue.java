package model.value;

import model.exception.TypeException;
import model.type.IType;
import model.type.IntType;

public class IntValue implements IValue {
    private final int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public IType getType(){
        return new IntType();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntValue) {
            return value == ((IntValue) o).value;
        }
        return false;
    }
}
