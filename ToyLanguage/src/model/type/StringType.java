package model.type;

import model.value.IValue;
import model.value.StringValue;

public class StringType implements IType {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return o instanceof StringType;
    }

    @Override
    public String toString(){
        return "String";
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }
}
