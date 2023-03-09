package brainfuck.data;

import java.util.EmptyStackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Стек циклов */
public class StackWhile {
    /** Логер класса StackWhile. */
    private static final Logger logger = LogManager.getLogger(StackWhile.class);
    /** Максимальный размер стека. */
    private final static Integer STACK_SIZE = 1000;

    /** Создание единственного экземпляра класса StackWhile. */
    public StackWhile() {
        stack = new Loop[STACK_SIZE];
        resetStack();
    }

    /** 
     * Просмотр вершины стека. 
     * @throws IndexOutOfBoundsException при попытке посмотреть вершину пустого стека. 
     */
    public Loop top() throws IndexOutOfBoundsException {
        if (pointer < 0 || pointer >= STACK_SIZE) {
            logger.error("Stack pointer = " + pointer + ", max size = " + STACK_SIZE);
            throw new IndexOutOfBoundsException();
        }
        return stack[pointer];
    }

    /** 
     * Добавление элемента на вершину стека. 
     * @param fValue адрес начала цикла. 
     * @param sValue адрес конца цикла. 
     * @throws RuntimeException если превышен максимальный размер стека. 
     */
    public void push(Integer fValue, Integer sValue) throws RuntimeException {
        pointer++;
        if (pointer >= STACK_SIZE) {
            logger.error("Stack pointer = " + pointer + ", max size = " + STACK_SIZE + ", push error");
            throw new RuntimeException(); // Stack overflow
        }
        stack[pointer] = new Loop(fValue, sValue);
    }

    /** 
     * Удаление элемента из вершины стека. 
     * @throws EmptyStackException при попытке удалить из пустого стека. 
     */
    public void pop() throws EmptyStackException {
        if (pointer < 0) {
            logger.error("Stack pointer = " + pointer + ", pop error");
            throw new EmptyStackException();
        }
        pointer--;
    }

    /** Очистка стека, на данный момент используется только в тестах. */
    public void resetStack() {
        for (int i = 0; i < STACK_SIZE; ++i) {
            stack[i] = new Loop(0,0);
        }
        pointer = -1;
        logger.info("StackWhile reseted!");
    }

    /** Стек. */
    private Loop[] stack = null;
    /** Указатель на вершину стека. */
    private Integer pointer = -1;
}
