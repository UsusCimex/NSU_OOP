package brainfuck;

import brainfuck.operation.*;

import java.io.FileNotFoundException;

import brainfuck.data.*;
import brainfuck.logic.*;

public class App {
    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println("Enter PATH to executable file!");
        } else {
            execute("/home/danil/git/21212_lanin/HomeWork/Java/BrainFuck/app/src/main/resources/code.bf"); //args[1]
        }
    }

    public static void execute(String path) {
        try {
            CommandContext context = new CommandContext(path);
            while (context.pointer < context.commandBuffer.getFileSize() && context.pointer >= 0) {
                String cmd = Character.toString(context.commandBuffer.getCommand(context.pointer));
                if (cmd.charAt(0) == ' ' || cmd.charAt(0) == '\n') {
                    context.pointer++;
                    continue;
                }
                Operation op = OperationFactory.GetInstance().create(cmd);
                op.run(context);
            }
        }
        catch(FileNotFoundException ex) {
            System.err.println("File not found ^_^");
        }
        catch(Exception ex) {
            System.err.println("BF destroyd...");
            ex.printStackTrace();
        }
    }
}