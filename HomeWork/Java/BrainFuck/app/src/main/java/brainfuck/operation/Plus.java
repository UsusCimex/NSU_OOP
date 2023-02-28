package brainfuck.operation;

import brainfuck.data.CommandContext;

//** Инкремент значения в данной ячейке. */
public class Plus implements Operation {
    @Override
    public void run(CommandContext context) {
        context.registerTape.setCellValue(context.registerTape.getCellValue() + 1);
        context.pointer++;
    }
}
