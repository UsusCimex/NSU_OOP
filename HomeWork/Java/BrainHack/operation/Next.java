package operation;
import data.ExecutablePointer;
import data.RegisterTape;

public class Next extends Operation{
    @Override
    public void run(ExecutablePointer pointer) {
        RegisterTape.setCellIndex(RegisterTape.getCellIndex() + 1);
        pointer.p++;
    }
}
