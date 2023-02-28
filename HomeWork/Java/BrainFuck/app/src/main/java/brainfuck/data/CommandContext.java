package brainfuck.data;

import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//** Все ресурсы, необходимые для работы с операциями */
public class CommandContext {
    //** Основной логер для класса CommandContext */
    private static final Logger logger = LogManager.getLogger(CommandContext.class);
    //**  Создаёт экземпляр класса CommandContext с файлом кода BrainFuck. @param path путь до файла с кодом BrainFuck. @throws FileNotFoundException если файл не существует или не может быть прочитан. */
    public CommandContext(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
        logger.info("File openned (" + path + ") " + commandBuffer.getFileSize() + " bytes");
    }
    //** Изменяет файл с кодом BrainFuck, на данный момент используется только в тестах. @param path пусть до нового файла BrainFuck. @throws FileNotFoundException если файл не существует или не может быть прочитан. */
    public void ChangeFile(String path) throws FileNotFoundException {
        commandBuffer = new CommandBuffer(path);
        pointer = 0;
        registerTape.resetTape();
        stackWhile.resetStack();
        logger.info("File changed (" + path + ") " + commandBuffer.getFileSize() + " bytes");
    }

    //** Указатель на номер команды в файле. Как правило не должен изменяться вручную. */
    public int pointer = 0;
    //** Множество ячеек, с которым работает BrainFuck. */
    public RegisterTape registerTape = RegisterTape.GetInstance();
    //** Стэк циклов */
    public StackWhile stackWhile = StackWhile.GetInstance();
    //** Буфер команд. */
    public CommandBuffer commandBuffer = null;
}
