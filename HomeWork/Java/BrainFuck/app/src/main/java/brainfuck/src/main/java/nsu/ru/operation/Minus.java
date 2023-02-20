package brainfuck.operation;

import brainfuck.data.ExecutablePointer;
import brainfuck.data.RegisterTape;

public class Minus implements Operation {
    @Override
    public void run(ExecutablePointer pointer) {
        RegisterTape.setCellValue(RegisterTape.getCellValue() - 1);
        pointer.p++;
    }
}
