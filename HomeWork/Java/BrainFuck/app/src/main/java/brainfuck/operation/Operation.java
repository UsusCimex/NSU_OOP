package brainfuck.operation;

import brainfuck.data.ExecutablePointer;

public interface Operation {
    public abstract void run(ExecutablePointer pointer);
}