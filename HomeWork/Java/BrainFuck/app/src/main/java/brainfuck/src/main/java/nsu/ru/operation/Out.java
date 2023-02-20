package operation;

import data.ExecutablePointer;
import data.RegisterTape;

public class Out extends Operation {
    @Override
    public void run(ExecutablePointer pointer) {
        System.out.print(RegisterTape.getCellValue() + " ");
        pointer.p++;
    }
}
