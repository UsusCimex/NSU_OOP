package data;

import java.util.Arrays;

public abstract class RegisterTape {
    private static final Integer TAPE_SIZE = 30000; // 30000 default value by U.Muller(author)

    public static Integer getCellValue() {
        return arr[index];
    }

    public static void setCellValue(Integer value) {
        arr[index] = value;
    }

    public static Integer getCellIndex() {
        return index;
    }

    public static void setCellIndex(Integer value) {
        if (value < 0 || value >= TAPE_SIZE) {
            System.out.println("Index overflow!");
        } // throw
        index = value;
    }

    private static Integer[] arr = new Integer[TAPE_SIZE];
    private static Integer index = 0;
    static {
        Arrays.fill(arr, 0);
    }
}