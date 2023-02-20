package brainfuck.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class CommandBuffer {
    private final static Integer BUFFER_SIZE = 1000;

    public CommandBuffer(String filePath) throws FileNotFoundException {
        file = new File(filePath);
        if (!file.canRead()) {
            throw new FileNotFoundException();
        }
        buffer = new char[BUFFER_SIZE];
    }

    public Long getFileSize() {
        return file.length();
    }

    public Character getCommand(Integer index) throws IndexOutOfBoundsException{
        if (index < 0) {
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
            System.err.println(ex.getMessage());
        }
    }

    private File file;
    private char[] buffer = null;
    private Integer pointer = -1;
}
