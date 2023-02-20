package brainfuck.data;

import java.util.EmptyStackException;

public class StackWhile {
    private final static Integer STACK_SIZE = 1000;
    private static StackWhile instance = null;

    public static StackWhile GetInstance() {
        if (instance == null) {
            instance = new StackWhile();
        }
        return instance;
    }

    private StackWhile() {
        stack = new Loop[STACK_SIZE];
        for (int i = 0; i < STACK_SIZE; ++i) {
            stack[i] = new Loop();
        }
        resetStack();
    }

    public Loop top() throws IndexOutOfBoundsException {
        if (pointer < 0 || pointer >= STACK_SIZE) {
            throw new IndexOutOfBoundsException();
        }
        return stack[pointer];
    }

    public void push(Integer fValue, Integer sValue) throws RuntimeException {
        pointer++;
        if (pointer >= STACK_SIZE) {
            throw new RuntimeException(); // Stack overflow
        }
        stack[pointer].from = fValue;
        stack[pointer].to = sValue;
    }

    public void pop() throws EmptyStackException {
        if (pointer < 0) {
            throw new EmptyStackException();
        }
        pointer--;
    }

    public void resetStack() {
        for (int i = 0; i < STACK_SIZE; ++i) {
            stack[i].from = 0;
            stack[i].to = 0;
        }
        pointer = -1;
    }

    private Loop[] stack = null;
    private Integer pointer = -1;
}
