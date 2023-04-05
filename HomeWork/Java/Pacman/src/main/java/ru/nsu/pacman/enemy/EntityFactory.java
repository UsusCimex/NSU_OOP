package ru.nsu.pacman.enemy;

import ru.nsu.pacman.Game;
import ru.nsu.pacman.generation.LevelData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static ru.nsu.pacman.generation.LevelData.convertStringToSymbol;
import static ru.nsu.pacman.GameData.Coordinates;

public class EntityFactory {
    private static EntityFactory instance = null;
    private final InputStream file = Game.class.getResourceAsStream("enemies.data");
    private Map<LevelData.Symbols, Class<?>> map = new HashMap<>();
    private EntityFactory() {
        Properties props = new Properties();
        try {
            props.load(file);
            for (String className : props.stringPropertyNames()) {
                String objName = props.getProperty(className);
                Class<?> clazz = Class.forName(className);
                map.put(convertStringToSymbol(objName), clazz);
            }
        } catch (IOException | ClassNotFoundException | SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }
        return instance;
    }

    public Entity createEnemy(LevelData.Symbols symbol, Coordinates startPosition, LevelData data) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (map.containsKey(symbol)) {
            return (Entity) map.get(symbol).getConstructor(Coordinates.class, LevelData.class).newInstance(startPosition, data);
        } else {
            return null;
        }
    }
}
