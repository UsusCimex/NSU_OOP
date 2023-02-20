package brainfuck.operation;

import brainfuck.data.CommandContext;

public class Out implements Operation {
    @Override
    public void run(CommandContext context) {
        System.out.print(context.registerTape.getCellValue() + " ");
        context.pointer++;
    }
}
