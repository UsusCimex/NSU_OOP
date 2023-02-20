package brainfuck;

import brainfuck.operation.*;

import java.io.FileNotFoundException;

import brainfuck.data.*;
import brainfuck.logic.*;

public class App {
    public static void main(String[] args) {
        // if (args.length != 1) {
        //     System.out.println("Enter PATH to executable file!");
        // } else
        execute("/home/danil/git/21212_lanin/HomeWork/Java/BrainFuck/app/src/main/resources/code.bf");
    }

    public static void execute(String path) {
        ExecutablePointer pointer = new ExecutablePointer();
        CommandBuffer buffer;
        try {
            buffer = new CommandBuffer(path);
            while (pointer.p < buffer.getFileSize() && pointer.p >= 0) {
                Integer cmd = buffer.getCommand(pointer.p);
                if (cmd == '\n' || cmd == ' ') {
                    pointer.p++;
                    continue;
                }
                if (cmd == '[') {
                    StackWhile.push(pointer.p, buffer.searchEndWhile(pointer.p));
                }
                Operation op = OperationFactory.create(cmd);
                op.run(pointer);
            }
        }
        catch(FileNotFoundException ex) {
            System.err.print("File not found ^_^");
        }
        catch(Exception ex) {
            System.err.print("BF destroyd...(" + ex.getMessage() + ")");
        }
        System.err.print("\n");
    }
}