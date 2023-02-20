package brainfuck.operation;

import java.util.Scanner;

import brainfuck.data.ExecutablePointer;
import brainfuck.data.RegisterTape;

public class In implements Operation {
    @Override
    public void run(ExecutablePointer pointer) {
        Integer read = 0;
        try {
            Scanner scan = new Scanner(System.in);
            read = scan.nextInt();
        } catch (Exception ex) {
            System.out.println("Scanner error: " + ex.getMessage());
        }
        RegisterTape.setCellValue(read);
        pointer.p++;
    }
}
