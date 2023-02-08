package operation;
import data.RegisterTape;
import data.StackWhile;

public class End extends Operation{
    @Override
    public int make() {
        int mover = StackWhile.top();
        StackWhile.pop();
        if (RegisterTape.getCellValue() == 0) {
            return 1;
        }
        return mover - RegisterTape.getCellIndex();
    }
}
