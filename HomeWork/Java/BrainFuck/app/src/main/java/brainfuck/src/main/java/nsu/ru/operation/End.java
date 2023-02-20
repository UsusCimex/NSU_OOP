package brainfuck.operation;

import brainfuck.data.ExecutablePointer;
import brainfuck.data.Loop;
import brainfuck.data.RegisterTape;
import brainfuck.data.StackWhile;

public class End implements Operation {
    @Override
    public void run(ExecutablePointer pointer) {
        Loop mover = StackWhile.top();
        StackWhile.pop();
        if (RegisterTape.getCellValue() == 0) {
            pointer.p++;
        } else {
            pointer.p = mover.from;
        }
    }
}
