package brainfuck.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//** Лента(множество) ячеек с которым работает код написанный на BrainFuck. */
public class RegisterTape {
    //** Логгер для класса RegisterTape. */
    private static final Logger logger = LogManager.getLogger(RegisterTape.class);
    //** Максимальный размер данной ленты. Используется стандартное значение, присвоенное автором языка BrainFuck.*/
    private static final Integer TAPE_SIZE = 30000;
    
    //** Единаственный экземпляр класса RegisterTape(Singleton). */
    private static RegisterTape instance = null;

    //** Получает единственный экземпляр класса RegisterTape. @return единственный экземпляр класса. */
    public static RegisterTape GetInstance() {
        if (instance == null) {
            logger.info("RegisterTape created!");
            instance = new RegisterTape();
        }
        return instance;
    }

    //** Создаёт единственный экземпляр класса. */
    private RegisterTape() {
        arr = new Integer[TAPE_SIZE];
        resetTape();
    }

    //** Возвращает значение на ячейке. @return значение на ячейке, на которое указывает главный указатель. */
    public Integer getCellValue() {
        return arr[index];
    }

    //** Изменяет значение на ячейке. @param value значение, которое будет изменено в ячейке на которую указывает главный указатель. */
    public void setCellValue(Integer value) {
        arr[index] = value;
    }

    //** Возвращает адрес ячейки в данной ленте. @return адрес ячейки на который указывает главный указатель. */
    public Integer getCellIndex() {
        return index;
    }

    //** Изменяет адрес на ячейки в данной ленте. @param value новый адрес на ячейку в ленте. @throws IndexOutOfBoundsException при попытке изменить на адрес, не возможный в данной ленте. */
    public void setCellIndex(Integer value) throws IndexOutOfBoundsException{
        if (value < 0 || value >= TAPE_SIZE) {
            logger.error("Attempt to index = " + value + ", max index = " + TAPE_SIZE);
            throw new IndexOutOfBoundsException();
        }
        index = value;
    }

    //** Зануляет все ячейки ленты. На данный момент используется только в тестах. */
    public void resetTape() {
        for (int i = 0; i < TAPE_SIZE; ++i) {
            arr[i] = 0;
        }
        index = 0;
        logger.info("RegisterTape reseted!");
    }

    //** Лента или множество значений, с котороми работает BrainFuck. */
    private Integer[] arr = null;
    //** Главный указатель на ячейку ленты. */
    private Integer index = 0;
}