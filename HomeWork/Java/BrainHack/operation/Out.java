package operation;
import data.RegisterTape;

public class Out extends Operation{
    @Override
    public int make() {
        System.out.println(RegisterTape.getCellValue());
        return 1;
    }
}
