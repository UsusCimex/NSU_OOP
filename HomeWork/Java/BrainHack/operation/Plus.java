package operation;
import data.ExecutablePointer;
import data.RegisterTape;

public class Plus extends Operation{
    @Override
    public void run(ExecutablePointer pointer) {
        RegisterTape.setCellValue(RegisterTape.getCellValue() + 1);
        pointer.p++;
    }
}
