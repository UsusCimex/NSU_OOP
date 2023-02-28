package brainfuck.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterTape {
    private static final Logger logger = LogManager.getLogger(RegisterTape.class);
    private static final Integer TAPE_SIZE = 30000; // 30000 default value by U.Muller(author)
    private static RegisterTape instance = null;

    public static RegisterTape GetInstance() {
        if (instance == null) {
            logger.info("RegisterTape created!");
            instance = new RegisterTape();
        }
        return instance;
    }

    private RegisterTape() {
        arr = new Integer[TAPE_SIZE];
        resetTape();
    }

    public Integer getCellValue() {
        return arr[index];
    }

    public void setCellValue(Integer value) {
        arr[index] = value;
    }

    public Integer getCellIndex() {
        return index;
    }

    public void setCellIndex(Integer value) throws IndexOutOfBoundsException{
        if (value < 0 || value >= TAPE_SIZE) {
            logger.error("Attempt to index = " + value + ", max index = " + TAPE_SIZE);
            throw new IndexOutOfBoundsException();
        }
        index = value;
    }

    public void resetTape() {
        for (int i = 0; i < TAPE_SIZE; ++i) {
            arr[i] = 0;
        }
        index = 0;
        logger.info("RegisterTape reseted!");
    }

    private Integer[] arr = null;
    private Integer index = 0;
}