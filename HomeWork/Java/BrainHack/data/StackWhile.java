package data;

public abstract class StackWhile {
    private final static Integer STACK_SIZE = 1000; 

    public static Integer top() {
        return stack[pointer];
    }
    public static void push(Integer value) {
        if (pointer >= STACK_SIZE) {System.out.println("Stack push miss");} //throw
        stack[++pointer] = value;
    }
    public static void pop() {
        if (pointer < 0) {System.out.println("Stack pop miss");} //throw
        pointer--;
    }

    private static Integer[] stack = new Integer[STACK_SIZE];
    private static Integer pointer = -1;
}
