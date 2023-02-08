package data;

import java.io.File;
import java.io.RandomAccessFile;

public class CommandBuffer {
    private final static int BUFFER_SIZE = 1000;
    
    public CommandBuffer(String filePath) {
        file = new File(filePath);
        if (!file.canRead()) {System.out.println("File " + filePath + " not found!");} //throw
    }

    public int getFileSize() {
        return (int)file.length();
    }

    public char getCommand(int index) {
        if (index < 0) {System.out.println("Command with index " + index + " not found!");} //throw
        if ((index < pointer) || (index >= pointer + BUFFER_SIZE) || (pointer == -1)) {
            readCommands(index);
            pointer = index;
        }
        return (char)buffer[index - pointer];
    }

    private void readCommands(int index) {
        try(RandomAccessFile reader = new RandomAccessFile(file, "r")) {
            reader.seek(index);
            reader.read(buffer, 0, BUFFER_SIZE);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private File file;
    private byte[] buffer = new byte[BUFFER_SIZE];
    private int pointer = -1;
}
