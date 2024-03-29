package brainfuck.logic;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;

import brainfuck.operation.Operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Фабрика операций */
public class OperationFactory {
    /** Логгер для класса OperationFactory */
    private static final Logger logger = LogManager.getLogger(OperationFactory.class);
    /** Файл с настройками фабрики */
    private final InputStream file = getClass().getResourceAsStream("/operations.properties");
    //** Хранилище для объектов */
    private Map<String, Operation> map = new HashMap<>();
    /** Единственный экземпляр класса(Singleton) */
    private static OperationFactory instance = null;

    /** Создаёт единственный экземпляр, с операциями загруженными из файла настройки */
    private OperationFactory() {
        Properties props = new Properties();
        try {
            props.load(file);
            for (String className : props.stringPropertyNames()) {
                String objName = props.getProperty(className);
                Class<?> clazz = Class.forName(className);
                Operation object = (Operation) clazz.getConstructor().newInstance();
                map.put(objName, object);
            }
        } catch (IOException e) {
            logger.error("Failed to load class file for factory!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load classes for factory!");
            e.printStackTrace();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("OperationFactory exception...");
            e.printStackTrace();
        }
    }
    /** 
     * Получает единственный экземпляр класса OperationFactory. 
     * @return единственный экземпляр класса. 
     */
    public static OperationFactory GetInstance() {
        if (instance == null) {
            instance = new OperationFactory();
            logger.info("Factory created!");
        }
        return instance;
    }
    /** 
     * Возвращает ссылку на объект операции по имени. 
     * @param name имя операции. @return объект операции. 
     */
    public Operation getOperation(String name) {
        logger.info("Creating " + name + " object");
        return map.get(name);
    }
}