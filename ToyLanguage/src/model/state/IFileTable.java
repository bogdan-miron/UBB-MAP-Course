package model.state;

import model.value.StringValue;

import java.io.BufferedReader;

public interface IFileTable {

    boolean isDefined(StringValue path);

    BufferedReader lookup(StringValue path);

    void add(StringValue fileName, BufferedReader fileDescriptor);

    void remove(StringValue filename);

    String toString();
}
