package model.state;

import model.value.IValue;

import java.util.ArrayList;
import java.util.List;

public class Output implements IOutput {
    private List<IValue> out;

    public Output() {
        this.out = new ArrayList<IValue>();
    }

    @Override
    public List<IValue> getOutput() {
        return out;
    }

    @Override
    public void add(IValue value) {
        this.out.add(value);
    }
}
