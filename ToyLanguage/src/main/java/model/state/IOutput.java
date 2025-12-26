package model.state;

import model.value.IValue;

import java.util.List;

public interface IOutput {
    List<IValue> getOutput();

    void add(IValue value);
}
