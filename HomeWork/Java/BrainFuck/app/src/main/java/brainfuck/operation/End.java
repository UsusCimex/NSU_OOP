package brainfuck.operation;

import brainfuck.data.CommandContext;
import brainfuck.data.Loop;

/** Завершение цикла. Если значение в данной ячейке 0 то перейдёт по следующему адресу, иначе вернётся в начало цикла. */
public class End implements Operation {
    @Override
    public void run(CommandContext context) {
        Loop mover = context.stackWhile.top();
        context.stackWhile.pop();
        if (context.registerTape.getCellValue() == 0) {
            context.pointer++;
        } else {
            context.pointer = mover.from;
        }
    }
}