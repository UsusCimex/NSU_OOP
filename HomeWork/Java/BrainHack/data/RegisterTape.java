package data;

public abstract class RegisterTape {
    private static final int TAPE_SIZE = 30000; // 30000 default value by U.Muller(author)

    public static char getCellValue() {
        return arr[index];
    }
    public static void nextCell() {
        if (index >= TAPE_SIZE) {} //throw
        index++;
    }
    public static void backCell() {
        if (index <= 0) {} //throw
        index--;
    }

    private static char[] arr = new char[TAPE_SIZE];
    private static Integer index = 0;
}