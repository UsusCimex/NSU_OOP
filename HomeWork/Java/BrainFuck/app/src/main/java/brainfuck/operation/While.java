package brainfuck.operation;

import brainfuck.data.ExecutablePointer;
import brainfuck.data.Loop;
import brainfuck.data.RegisterTape;
import brainfuck.data.StackWhile;

public class While implements Operation {
    @Override
    public void run(ExecutablePointer pointer) {
        Loop loop = StackWhile.top();
        if (RegisterTape.getCellValue() == 0) {
            pointer.p = loop.to + 1;
            StackWhile.pop();
        } else {
            pointer.p++;
        }
    }
}