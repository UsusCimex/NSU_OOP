package operation;
import data.ExecutablePointer;
import data.RegisterTape;

public class Minus extends Operation{
    @Override
    public void run(ExecutablePointer pointer) {
        RegisterTape.setCellValue((char)(RegisterTape.getCellValue() - 1));
        pointer.p++;
    }
}
