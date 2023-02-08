package operation;
import data.RegisterTape;

public class Back extends Operation{
    @Override
    public int make() {
        RegisterTape.setCellIndex(RegisterTape.getCellIndex() - 1);
        return 1;
    }
}
