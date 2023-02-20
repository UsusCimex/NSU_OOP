package brainfuck;

import brainfuck.operation.*;

import java.io.FileNotFoundException;

import brainfuck.data.*;
import brainfuck.logic.*;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Enter PATH to executable file!");
        } else {
            execute(args[1]); //"/home/danil/git/21212_lanin/HomeWork/Java/BrainFuck/app/src/main/resources/code.bf"
        }
    }

    public static void execute(String path) {
        try {
            CommandContext context = new CommandContext(path);
            while (context.pointer < context.commandBuffer.getFileSize() && context.pointer >= 0) {
                String cmd = Character.toString(context.commandBuffer.getCommand(context.pointer));
                if (cmd == "\n" || cmd == " ") {
                    context.pointer++;
                    continue;
                }
                Operation op = OperationFactory.GetInstance().create(cmd);
                op.run(context);
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