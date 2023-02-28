package brainfuck.logic;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;

import brainfuck.operation.Operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OperationFactory {
    private static final Logger logger = LogManager.getLogger(OperationFactory.class);
    private final String FILE_PATH = "src/main/resources/operations.properties";
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
            }
        } catch (Exception e) {
            logger.error("Failed to load classes for factory!");
            e.printStackTrace();
        }
    }

    public static OperationFactory GetInstance() {
        if (instance == null) {
            instance = new OperationFactory();
            logger.info("Factory created!");
        }
        return instance;
    }

    public Operation create(String name) {
        logger.info("Creating " + name + " object");
        return map.get(name);
    }
}