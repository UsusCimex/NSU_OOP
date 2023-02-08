package operation;
import data.RegisterTape;

public class Minus extends Operation{
    @Override
    public int make() {
        RegisterTape.setCellValue((char)(RegisterTape.getCellValue() - 1));
        return 1;
    }
}
