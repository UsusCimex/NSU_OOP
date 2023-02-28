package brainfuck.operation;

import brainfuck.data.CommandContext;

//** Декремент указателя. */
public class Back implements Operation {
    @Override
    public void run(CommandContext context) {
        context.registerTape.setCellIndex(context.registerTape.getCellIndex() - 1);
        context.pointer++;
    }
}
