package logic;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import operation.*;

public class OperationFactory {
    private final static String FILE_PATH = "initOperators.txt";
    
    private static Map<String, Operation> map = new HashMap<>();

    public static void register(String key, String className) {
        try {
            Class<?> nClass = Class.forName(className);
            Operation op = (Operation)nClass.getDeclaredConstructor().newInstance();
            map.put(key, op);
        } catch (Exception ex) {
            System.out.println("Register error: " + ex.getMessage());
        }
    }

    public static Object create(String key) {
        Operation op = null;
        try {
            op = map.get(key);
        } catch (Exception ex) {
            System.out.println("Create error: " + ex.getMessage());
        }
        if (op == null) {
            System.out.println("class (" + key + ") don't searched");
        }
        return op;
    }

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String string = reader.readLine();
            while (string != null) {
                String key = string.substring(0, string.indexOf(' '));
                String value = string.substring(string.indexOf(' ') + 1, string.length());
                register(key, value);
                string = reader.readLine();
            }
        } catch (Exception ex) {
            System.out.println("File reading error: " + ex.getMessage());
        }
    }
}