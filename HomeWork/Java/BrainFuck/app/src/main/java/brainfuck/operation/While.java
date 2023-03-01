package brainfuck.operation;

import brainfuck.data.CommandContext;
import brainfuck.data.Loop;

/** Начало цикла. Не зайдёт внутрь цикла, если значение в данной ячейке 0. */
public class While implements Operation {
    @Override
    public void run(CommandContext context) {
        context.stackWhile.push(context.pointer, searchEndWhile(context));
        Loop loop = context.stackWhile.top();
        if (context.registerTape.getCellValue() == 0) {
            context.pointer = loop.to + 1;
            context.stackWhile.pop();
        } else {
            context.pointer++;
        }
    }

    /** Поиск конца цикла, с учётом вложенных циклов. */
    private Integer searchEndWhile(CommandContext context) {
        int searcher = 1;
        int index = context.pointer;
        while (searcher != 0) {
            index++;
            int readed = context.commandBuffer.getCommand(index);
            if (readed == ']')
                searcher--;
            else if (readed == '[')
                searcher++;
        }
        return index;
    }
}