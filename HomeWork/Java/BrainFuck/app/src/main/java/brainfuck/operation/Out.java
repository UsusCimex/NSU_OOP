package brainfuck.operation;

import brainfuck.data.ExecutablePointer;
import brainfuck.data.RegisterTape;

public class Out implements Operation {
    @Override
    public void run(ExecutablePointer pointer) {
        System.out.print(RegisterTape.getCellValue() + " ");
        pointer.p++;
    }
}
