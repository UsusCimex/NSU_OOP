package data;

import java.util.Arrays;

public abstract class RegisterTape {
    private static final int TAPE_SIZE = 30000; // 30000 default value by U.Muller(author)

    public static char getCellValue() {
        return arr[index];
    }
    public static void setCellValue(char value) {
        arr[index] = value;
    }

    public static Integer getCellIndex() {
        return index;
    }
    public static void setCellIndex(int value) {
        if (value < 0 || value >= TAPE_SIZE) {} //throw
        index = value;
    }

    private static char[] arr = new char[TAPE_SIZE];
    private static Integer index = 0;
    static {
        Arrays.fill(arr, (char)0);
    }
}