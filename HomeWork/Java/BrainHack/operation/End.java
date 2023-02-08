package operation;
import data.ExecutablePointer;
import data.RegisterTape;
import data.StackWhile;

public class End extends Operation{
    @Override
    public void run(ExecutablePointer pointer) {
        Integer mover = StackWhile.top();
        StackWhile.pop();
        if (RegisterTape.getCellValue() == 0) {
            pointer.p++;
        }
        else {
            pointer.p = mover;
        }
    }
}
