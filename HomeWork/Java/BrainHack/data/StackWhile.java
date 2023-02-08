package data;

public abstract class StackWhile {
    private final static int STACK_SIZE = 1000; 

    public static int top() {
        return stack[pointer];
    }
    public static void push(int value) {
        if (pointer >= STACK_SIZE) {} //throw
        stack[++pointer] = value;
    }
    public static void pop() {
        if (pointer < 0) {} //throw
        pointer--;
    }

    private static int[] stack = new int[STACK_SIZE];
    private static int pointer = -1;
}
