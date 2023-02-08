import operation.*;
import data.*;
import logic.*;

public class BrainHack {
    public static void main(String[] args) {
        if (args.length != 1) { System.out.println("Enter PATH to executable file!"); }
        else execute(args[0]);
    }
    public static void execute(String path) {
        ExecutablePointer pointer = new ExecutablePointer();
        CommandBuffer buffer = new CommandBuffer(path);
        while (pointer.p < buffer.getFileSize() && pointer.p >= 0) {
            Integer cmd = buffer.getCommand(pointer.p);
            if (cmd == '\n' || cmd == ' ') { pointer.p++; continue; }
            Operation op = OperationFactory.create(cmd);
            op.run(pointer);
        }
    }
}