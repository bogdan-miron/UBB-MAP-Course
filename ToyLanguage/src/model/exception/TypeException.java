package model.exception;

import model.type.IType;

public class TypeException extends InterpreterException{
    public TypeException(String msg){
        super(msg);
    }

    public TypeException(String context, IType expected, IType actual){
        super(String.format("%s: expected type %s but got %s", context, expected, actual));
    }
}
