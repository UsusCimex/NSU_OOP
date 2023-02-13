package operation;

import data.ExecutablePointer;
import data.Loop;
import data.RegisterTape;
import data.StackWhile;

public class While extends Operation {
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