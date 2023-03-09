package brainfuck.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Основное хранили для кода написанных на BrainFuck */
public class CommandBuffer {
    /** Логгер для класса CommandBuffer */
    private static final Logger logger = LogManager.getLogger(CommandBuffer.class);
    /** Размер буффера */
    private final static Integer BUFFER_SIZE = 1000;

    /** 
     * Создаёт экземпляр класса CommandBuffer на основе пути к файлу с кодом. 
     * @param filePath путь к файлу с кодом. 
     * @throws FileNotFoundException если файл не существует или не может быть прочитан. 
     */
    public CommandBuffer(String filePath) throws FileNotFoundException {
        file = new File(filePath);
        if (!file.canRead()) {
            logger.error("File " + filePath + " did not open");
            throw new FileNotFoundException();
        }
    }

    /** 
     * Возвращает размер файла с кодом. 
     * @return размер файла с кодом в байтах. 
     */
    public Long getFileSize() {
        return file.length();
    }

    /** 
     * Возвращает считанную команду с буфера по указаному индексу. 
     * @param index индекс команды в коде BrainFuck. 
     * @return команда BrainFuck. 
     * @throws IndexOutOfBoundsException если произошло обращение по не существующему индексу. 
     */
    public Character getCommand(Integer index) throws IndexOutOfBoundsException {
        if (index < 0 || index > getFileSize()) {
            logger.error("Attempt to index = " + index + ", max index = " + getFileSize());
            throw new IndexOutOfBoundsException();
        }
        if ((index < pointer) || (index >= pointer + BUFFER_SIZE) || (pointer == -1)) {
            readCommands(index);
            pointer = index;
        }
        return buffer[index - pointer];
    }

    /** 
     * Считывание с файла команд от указанного индекса в размере вместимости буфера. 
     * @param index индекс, с которого стоит считывать информацию. 
     */
    private void readCommands(Integer index) {
        try (RandomAccessFile raFile = new RandomAccessFile(file, "r")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(raFile.getFD()), "UTF-8"));
            raFile.seek(index);
            reader.read(buffer, 0, BUFFER_SIZE);
            reader.close();
        } catch (IOException ex) {
            logger.error("Error file reader! In \"readCommands\" function!");
            System.err.println(ex.getMessage());
        }
    }

    /** Файл */
    private File file = null;
    /** Буфер  */
    private char[] buffer = new char[BUFFER_SIZE];
    /** Индекс с которого произошло последнее считывание с файла */
    private Integer pointer = -1;
}
