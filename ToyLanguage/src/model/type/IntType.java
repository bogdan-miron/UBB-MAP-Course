package model.type;

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
}
