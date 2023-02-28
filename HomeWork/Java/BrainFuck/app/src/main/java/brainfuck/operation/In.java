package brainfuck.operation;

import java.util.Scanner;

import brainfuck.data.CommandContext;

//** Считывание данных с консоли. */
public class In implements Operation {
    private static Scanner scan = new Scanner(System.in);
    @Override
    public void run(CommandContext context) {
        Integer read = scan.nextInt();
        context.registerTape.setCellValue(read);
        context.pointer++;
    }
}
