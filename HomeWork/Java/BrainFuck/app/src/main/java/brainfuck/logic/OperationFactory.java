package brainfuck.logic;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;

import brainfuck.operation.Operation;

public class OperationFactory {
    private final String FILE_PATH = "/home/danil/git/21212_lanin/HomeWork/Java/BrainFuck/app/src/main/resources/operations.properties";
    private Map<String, Operation> map = null;
    private static OperationFactory instance = null;

    private OperationFactory() {
        map = new HashMap<>();
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(FILE_PATH));
            for (String className : props.stringPropertyNames()) {
                String objName = props.getProperty(className);
                Class<?> clazz = Class.forName(className);
                Operation object = (Operation) clazz.getConstructor().newInstance();
                map.put(objName, object);

                System.out.println(className + " = " + objName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OperationFactory GetInstance() {
        if (instance == null) {
            instance = new OperationFactory();
        }
        return instance;
    }

    public Operation create(String name) {
        return map.get(name);
    }
}