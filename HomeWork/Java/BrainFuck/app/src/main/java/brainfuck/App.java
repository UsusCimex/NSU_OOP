package brainfuck;

import brainfuck.operation.*;

import java.io.FileNotFoundException;

import brainfuck.data.*;
import brainfuck.logic.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Главный класс приложения */
public class App {
    /** Логгер для класса App */
    private static final Logger logger = LogManager.getLogger(App.class);
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Enter PATH to executable file!");
        } else {
            logger.info("Programm started!");
            execute(args[0]);
        }
    }

    /** 
     * Запускает исполнение программы BrainFuck из файла по заданному пути. 
     * @param path путь к файлу с программой brainfuck.
     */
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
        catch(IndexOutOfBoundsException ex) {
            System.err.println("BF destroyd...");
            ex.printStackTrace();
        }
    }
}