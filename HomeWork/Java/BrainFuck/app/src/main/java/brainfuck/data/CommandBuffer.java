package brainfuck.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandBuffer {
    private static final Logger logger = LogManager.getLogger(CommandBuffer.class);
    private final static Integer BUFFER_SIZE = 1000;

    public CommandBuffer(String filePath) throws FileNotFoundException {
        file = new File(filePath);
        if (!file.canRead()) {
            logger.error("File " + filePath + " did not open");
            throw new FileNotFoundException();
        }
        buffer = new char[BUFFER_SIZE];
    }

    public Long getFileSize() {
        return file.length();
    }

    public Character getCommand(Integer index) throws IndexOutOfBoundsException{
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

    private void readCommands(Integer index) {
        try (RandomAccessFile raFile = new RandomAccessFile(file, "r")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(raFile.getFD()), "UTF-8"));
            raFile.seek(index);
            reader.read(buffer, 0, BUFFER_SIZE);
        } catch (Exception ex) {
            logger.error("Error file reader! In \"readCommands\" function!");
            System.err.println(ex.getMessage());
        }
    }

    private File file;
    private char[] buffer = null;
    private Integer pointer = -1;
}
