package brainfuck.data;

import java.io.FileNotFoundException;

public class CommandContext {
    public CommandContext(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
    }

    public int pointer = 0;
    public RegisterTape registerTape = RegisterTape.GetInstance();
    public StackWhile stackWhile = StackWhile.GetInstance();
    public CommandBuffer commandBuffer = null;
}
