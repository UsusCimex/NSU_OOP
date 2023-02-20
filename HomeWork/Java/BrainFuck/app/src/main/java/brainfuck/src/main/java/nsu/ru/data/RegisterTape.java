package brainfuck.data;

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

    public static void setCellIndex(Integer value) throws IndexOutOfBoundsException{
        if (value < 0 || value >= TAPE_SIZE) {
            throw new IndexOutOfBoundsException();
        }
        index = value;
    }

    public static void resetTape() {
        for (int i = 0; i < TAPE_SIZE; ++i) {
            arr[i] = 0;
        }
        index = 0;
    }

    private static Integer[] arr = new Integer[TAPE_SIZE];
    private static Integer index = 0;
    static {
        resetTape();
    }
}