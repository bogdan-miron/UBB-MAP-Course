package model.value;

import model.type.IType;
import model.type.RefType;

public class RefValue implements IValue {
    private int address;
    private IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddr() {
        return address;
    }

    public IType getLocationType() {
        return locationType;
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + "," + locationType.toString() + ")";
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefValue) {
            RefValue anotherRef = (RefValue) another;
            return address == anotherRef.getAddr() && locationType.equals(anotherRef.getLocationType());
        }
        return false;
    }
}