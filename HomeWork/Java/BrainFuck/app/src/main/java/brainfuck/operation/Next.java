package brainfuck.operation;

import brainfuck.data.CommandContext;

public class Next implements Operation {
    @Override
    public void run(CommandContext context) {
        context.registerTape.setCellIndex(context.registerTape.getCellIndex() + 1);
        context.pointer++;
    }
}
