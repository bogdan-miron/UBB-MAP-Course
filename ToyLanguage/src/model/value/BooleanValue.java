package model.value;

import model.type.BoolType;
import model.type.IType;

public class BooleanValue implements IValue{
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public boolean getValue(){
        return value;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public IType getType(){
        return new BoolType();
    }

    public boolean equals(Object o){
        if (o instanceof BooleanValue){
            return value == ((BooleanValue) o).value;
        }
        return false;
    }
}
