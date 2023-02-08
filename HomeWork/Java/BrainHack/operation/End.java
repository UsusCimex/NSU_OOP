package operation;
import data.ExecutablePointer;
import data.RegisterTape;
import data.StackWhile;

public class End extends Operation{
    @Override
    public void run(ExecutablePointer pointer) {
        int mover = StackWhile.top();
        StackWhile.pop();
        if ((int)RegisterTape.getCellValue() == 0) {
            pointer.p++;
        }
        else {
            pointer.p = mover;
        }
    }
}
