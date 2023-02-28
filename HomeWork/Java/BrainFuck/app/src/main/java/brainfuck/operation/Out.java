package brainfuck.operation;

import brainfuck.data.CommandContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//** Вывод содержимого ячейки. */
public class Out implements Operation {
    private static final Logger logger = LogManager.getLogger(Out.class);
    @Override
    public void run(CommandContext context) {
        int val = context.registerTape.getCellValue();
        System.out.print(val + " ");
        context.pointer++;
        logger.info("Writed: " +  val);
    }
}
