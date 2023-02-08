package logic;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import operation.*;

public class OperationFactory {
    private final static String FILE_PATH = "logic/initOperation.init";
    
    private static Map<Integer, Operation> map = new HashMap<>();

    private static void register(Integer key, String className) {
        try {
            Class<?> nClass = Class.forName(className);
            Operation op = (Operation)nClass.getDeclaredConstructor().newInstance();
            map.put(key, op);
        } catch (Exception ex) {
            System.out.println("Register error: " + ex.getMessage());
        }
    }

    public static Operation create(Integer key) {
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
                Integer key = (int)string.charAt(0);
                String value = string.substring(2, string.length());
                register(key, value);
                string = reader.readLine();
            }
        } catch (Exception ex) {
            System.out.println("File reading error: " + ex.getMessage());
        }
    }
}