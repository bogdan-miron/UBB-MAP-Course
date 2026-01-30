package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.type.IType;
import model.util.Pair;
import model.value.IValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CallStatement implements IStatement {
    private final String procedureName;
    private final List<IExpression> arguments;

    public CallStatement(String procedureName, List<IExpression> arguments) {
        this.procedureName = procedureName;
        this.arguments = arguments;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check procedure exists in ProcTable
        if (!state.getProcTable().isDefined(procedureName)) {
            throw new TypeException("Procedure " + procedureName + " is not defined");
        }

        // get procedure definition
        Pair<List<String>, IStatement> proc = state.getProcTable().get(procedureName);
        List<String> formalParams = proc.getFirst();
        IStatement body = proc.getSecond();

        // check argument count matches
        if (formalParams.size() != arguments.size()) {
            throw new TypeException("Argument count mismatch for procedure " + procedureName +
                    ": expected " + formalParams.size() + ", got " + arguments.size());
        }

        // evaluate arguments using CURRENT SymTable
        List<IValue> evaluatedArgs = new ArrayList<>();
        for (IExpression arg : arguments) {
            evaluatedArgs.add(arg.evaluate(state.getSymTable(), state.getHeap()));
        }

        // create new SymTable with formal params -> evaluated values
        ISymbolTable newSymTable = new SymbolTable();
        for (int i = 0; i < formalParams.size(); i++) {
            newSymTable.update(formalParams.get(i), evaluatedArgs.get(i));
        }

        // push new SymTable on stack
        state.pushSymTable(newSymTable);

        // push ReturnStatement (executed after body completes)
        state.getExeStack().push(new ReturnStatement());

        // push procedure body
        state.getExeStack().push(body.deepCopy());

        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // type checking for procedures would require procedure type signatures
        // just return typeEnv unchanged for now
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        List<IExpression> copiedArgs = arguments.stream()
                .map(IExpression::deepCopy)
                .collect(Collectors.toList());
        return new CallStatement(procedureName, copiedArgs);
    }

    @Override
    public String toString() {
        String argsStr = arguments.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        return "call " + procedureName + "(" + argsStr + ")";
    }
}
