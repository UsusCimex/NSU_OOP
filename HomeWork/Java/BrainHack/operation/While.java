package operation;
import data.RegisterTape;
import data.StackWhile;

public class While extends Operation{
    @Override
    public int make() {
        StackWhile.push(RegisterTape.getCellIndex());
        return 1;
    }
}
