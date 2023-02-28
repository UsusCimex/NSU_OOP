package brainfuck.data;

import java.io.FileNotFoundException;

public class CommandContext {
    public CommandContext(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
    }
    public void ChangeFile(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
        pointer = 0;
        registerTape.resetTape();
        stackWhile.resetStack();
    }

    public int pointer = 0;
    public RegisterTape registerTape = RegisterTape.GetInstance();
    public StackWhile stackWhile = StackWhile.GetInstance();
    public CommandBuffer commandBuffer = null;
}
