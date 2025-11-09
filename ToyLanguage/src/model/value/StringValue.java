package model.value;

import model.type.IType;
import model.type.StringType;

public class StringValue implements IValue {

    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        return value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StringValue) {
            return value.equals(((StringValue) o).value);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
