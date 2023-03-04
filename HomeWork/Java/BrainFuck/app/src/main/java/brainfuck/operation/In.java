package brainfuck.operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import brainfuck.data.CommandContext;

/** Считывание данных с консоли. */
public class In implements Operation {
    private static final Logger logger = LogManager.getLogger(In.class);
    @Override
    public void run(CommandContext context) {
        Integer read = context.scanner.nextInt();
        context.registerTape.setCellValue(read);
        context.pointer++;
        logger.info("Readed: " +  read);
    }
}
