package data;

public abstract class CommandBuffer {
    private final static int BUFFER_SIZE = 1000;
    
    public static char getCommand(int index) {
        if (index < 0) {} //throw
        if ((index < pointer) || (index >= pointer + BUFFER_SIZE)) {
            readCommands(index);
        }
        return buffer[index - pointer];
    }
    private static void readCommands(int index) {
        //use FileReader
    }

    private static char[] buffer = new char[BUFFER_SIZE];
    private static int pointer = 0;
}
