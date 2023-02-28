package brainfuck.data;

import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandContext {
    private static final Logger logger = LogManager.getLogger(CommandContext.class);
    public CommandContext(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
        logger.info("File openned (" + path + ") " + commandBuffer.getFileSize() + " bytes");
    }
    public void ChangeFile(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
        pointer = 0;
        registerTape.resetTape();
        stackWhile.resetStack();
        logger.info("File changed (" + path + ") " + commandBuffer.getFileSize() + " bytes");
    }

    public int pointer = 0;
    public RegisterTape registerTape = RegisterTape.GetInstance();
    public StackWhile stackWhile = StackWhile.GetInstance();
    public CommandBuffer commandBuffer = null;
}
