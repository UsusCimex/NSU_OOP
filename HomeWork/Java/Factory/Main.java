public class Main {
    public static void main(String[] args) {
        MyClass cls1 = (MyClass)Factory.create("mc1");
        MyClass cls2 = (MyClass)Factory.create("mc2");

        System.out.println(cls1.getVal());
        System.out.println(cls2.getVal());

        cls1.changetVal(3);
        System.out.println(cls1.getVal());

        MyClass cls3 = (MyClass)Factory.create("mc1");
        System.out.println(cls3.getVal());
    }
}
