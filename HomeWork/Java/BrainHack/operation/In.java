package operation;
import java.util.Scanner;

import data.RegisterTape;

public class In extends Operation{
    @Override
    public int make() {
        Scanner scan = new Scanner(System.in);
        char read = (char)scan.nextByte();
        RegisterTape.setCellValue(read);
        scan.close();
        return 1;
    }
}
