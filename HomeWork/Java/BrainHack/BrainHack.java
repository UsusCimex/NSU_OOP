import operation.*;
import data.*;
import logic.*;

public class BrainHack {
    public static void main(String[] args) {
        if (args.length != 1) { System.out.println("Enter PATH to executable file!"); }
        else execute(args[0]);
    }
    public static void execute(String path) {
        int pointer = 0;
        CommandBuffer buffer = new CommandBuffer(path);
        while (pointer < buffer.getFileSize() && pointer > 0) {
            char cmd = buffer.getCommand(pointer);
            Operation op = OperationFactory.create(cmd);
            pointer += op.make();
        }
    }
}