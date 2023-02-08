package operation;
import data.RegisterTape;

public class Plus extends Operation{
    @Override
    public int make() {
        RegisterTape.setCellValue((char)(RegisterTape.getCellValue() + 1));
        return 1;
    }
}
