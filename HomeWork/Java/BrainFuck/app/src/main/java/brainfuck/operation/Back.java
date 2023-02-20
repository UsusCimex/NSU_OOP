package brainfuck.operation;

import brainfuck.data.ExecutablePointer;
import brainfuck.data.RegisterTape;

public class Back implements Operation {
    @Override
    public void run(ExecutablePointer pointer) {
        RegisterTape.setCellIndex(RegisterTape.getCellIndex() - 1);
        pointer.p++;
    }
}
