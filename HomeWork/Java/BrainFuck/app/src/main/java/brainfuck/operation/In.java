package brainfuck.operation;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import brainfuck.data.CommandContext;

/** Считывание данных с консоли. */
public class In implements Operation {
    private static final Logger logger = LogManager.getLogger(In.class);
    private static Scanner scan = new Scanner(System.in);
    @Override
    public void run(CommandContext context) {
        Integer read = scan.nextInt();
        context.registerTape.setCellValue(read);
        context.pointer++;
        logger.info("Readed: " +  read);
    }
}
