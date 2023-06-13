import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

abstract class Factory { //Only myclass
    private static Map<String, Class<?>> map = new HashMap<>();

    public static void register(String key, String className) {
        try {
            Class<?> nClass = Class.forName(className);
            map.put(key, nClass);
        } catch (ClassNotFoundException ex) {
            System.out.println("Register error: " + ex.getMessage());
        }
    }

    public static Object create(String key) {
        Object mycls = null;
        try {
            Class<?> cls = map.get(key);
            mycls = (Object)cls.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            System.out.println("Create error: " + ex.getMessage());
        }
        if (mycls == null) {
            System.out.println("class (" + key + ") don't searched");
        }
        return mycls;
    }

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader("forFactory.txt"))) {
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