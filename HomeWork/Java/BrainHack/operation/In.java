package operation;
import java.io.InputStreamReader;

import data.ExecutablePointer;
import data.RegisterTape;

public class In extends Operation{
    @Override
    public void run(ExecutablePointer pointer) {
        char read = ' ';
        try {
            InputStreamReader scan = new InputStreamReader(System.in);
            read = (char)scan.read();
        }
        catch(Exception ex) {
            System.out.println("Scanner error: " + ex.getMessage());
        }
        RegisterTape.setCellValue(read);
        pointer.p++;
    }
}
