package model.state;

import model.value.IValue;

import java.util.Map;

public interface IHeap {
    int allocate(IValue value);

    IValue get(int address);

    void put(int address, IValue value);

    boolean isDefined(int address);

    void remove(int address);

    Map<Integer, IValue> getContent();

    void setContent(Map<Integer, IValue> content);

    String toString();
}