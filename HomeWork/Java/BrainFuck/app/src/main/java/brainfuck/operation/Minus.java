package brainfuck.operation;

import brainfuck.data.CommandContext;

public class Minus implements Operation {
    @Override
    public void run(CommandContext context) {
        context.registerTape.setCellValue(context.registerTape.getCellValue() - 1);
        context.pointer++;
    }
}
