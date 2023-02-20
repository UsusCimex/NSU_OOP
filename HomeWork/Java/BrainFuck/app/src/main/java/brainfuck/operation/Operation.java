package brainfuck.operation;

import brainfuck.data.CommandContext;

public interface Operation {
    public abstract void run(CommandContext context);
}