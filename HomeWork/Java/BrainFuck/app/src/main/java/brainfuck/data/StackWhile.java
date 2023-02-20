package brainfuck.data;

import java.util.EmptyStackException;

public abstract class StackWhile {
    private final static Integer STACK_SIZE = 1000;

    public static Loop top() throws IndexOutOfBoundsException {
        if (pointer < 0 || pointer >= STACK_SIZE) {
            throw new IndexOutOfBoundsException();
        }
        return stack[pointer];
    }

    public static void push(Integer fValue, Integer sValue) throws RuntimeException {
        pointer++;
        if (pointer >= STACK_SIZE) {
            throw new RuntimeException(); //Stack overflow
        }
        stack[pointer].from = fValue;
        stack[pointer].to = sValue;
    }

    public static void pop() throws EmptyStackException {
        if (pointer < 0) {
            throw new EmptyStackException();
        }
        pointer--;
    }

    public static void resetStack() {
        for (int i = 0; i < STACK_SIZE; ++i) {
            stack[i].from = 0;
            stack[i].to = 0;
        }
        pointer = -1;
    }

    private static Loop[] stack = new Loop[STACK_SIZE];
    static {
        for (int i = 0; i < STACK_SIZE; ++i)
            stack[i] = new Loop();
    }
    private static Integer pointer = -1;
}
