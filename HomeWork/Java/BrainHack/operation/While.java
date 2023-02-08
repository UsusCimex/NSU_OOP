package operation;
import data.ExecutablePointer;
import data.StackWhile;

public class While extends Operation{
    @Override
    public void run(ExecutablePointer pointer) {
        StackWhile.push(pointer.p);
        pointer.p++;
    }
}
