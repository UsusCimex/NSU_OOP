package data;

public abstract class StackWhile {
    private final static Integer STACK_SIZE = 1000;

    public static Loop top() {
        if (pointer < 0 || pointer >= STACK_SIZE) {
            System.out.println("Stack overflow(top)");
        } // throw
        return stack[pointer];
    }

    public static void push(Integer fValue, Integer sValue) {
        pointer++;
        if (pointer >= STACK_SIZE) {
            System.out.println("Stack push miss");
        } // throw
        stack[pointer].from = fValue;
        stack[pointer].to = sValue;
    }

    public static void pop() {
        if (pointer < 0) {
            System.out.println("Stack pop miss");
        } // throw
        pointer--;
    }

    private static Loop[] stack = new Loop[STACK_SIZE];
    static {
        for (int i = 0; i < STACK_SIZE; ++i)
            stack[i] = new Loop();
    }
    private static Integer pointer = -1;
}
