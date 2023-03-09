package brainfuck.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Все ресурсы, необходимые для работы с операциями */
public class CommandContext {
    /** Основной логер для класса CommandContext */
    private static final Logger logger = LogManager.getLogger(CommandContext.class);
    /**  
     * Создаёт экземпляр класса CommandContext с файлом кода BrainFuck. 
     * @param path путь до файла с кодом BrainFuck. 
     * @throws FileNotFoundException если файл не существует или не может быть прочитан. 
     */
    public CommandContext(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
        logger.info("File openned (" + path + ") " + commandBuffer.getFileSize() + " bytes");
    }
    /** 
     * Изменяет файл с кодом BrainFuck, на данный момент используется только в тестах. 
     * @param path путь до нового файла BrainFuck. 
     * @throws FileNotFoundException если файл не существует или не может быть прочитан. 
     */
    public void ChangeFileForTest(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
        pointer = 0;
        registerTape.resetTape();
        stackWhile.resetStack();
        logger.info("File changed (" + path + ") " + commandBuffer.getFileSize() + " bytes");
    }
    /** 
     * Изменяет поток ввода для записи в ячейки BrainFuck, на данный момент используется только в тестах. 
     * @param path путь до файла. 
     * @throws FileNotFoundException если файл не существует или не может быть прочитан. 
     */
    public void ChangeInputStreamFileForTest(String path) throws FileNotFoundException {
        if (path == "CONSOLE") {
            scanner = new Scanner(System.in);
        }
        else {
            File file = new File(path);
            if (!file.canRead()) {
                logger.error("File " + path + " did not open");
                throw new FileNotFoundException();
            }
            scanner = new Scanner(file);
        }
    }

    /** Указатель на номер команды в файле. Как правило не должен изменяться вручную. */
    public int pointer = 0;
    /** Множество ячеек, с которым работает BrainFuck. */
    public RegisterTape registerTape = new RegisterTape();
    /** Стэк циклов */
    public StackWhile stackWhile = new StackWhile();
    /** Буфер команд. */
    public CommandBuffer commandBuffer = null;
    /** Поток ввода(По умолчанию консольный ввод) */
    public Scanner scanner = new Scanner(System.in);
}
