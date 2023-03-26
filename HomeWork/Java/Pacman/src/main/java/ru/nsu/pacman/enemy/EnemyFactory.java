package ru.nsu.pacman.enemy;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.generation.LevelData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static ru.nsu.pacman.generation.LevelData.convertStringToSymbol;

public class EnemyFactory {
    private static EnemyFactory instance = null;
    private final InputStream file = PacmanGame.class.getResourceAsStream("enemies.data");
    private Map<LevelData.Symbols, Class<?>> map = new HashMap<>();
    private EnemyFactory() {
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

    public static EnemyFactory getInstance() {
        if (instance == null) {
            instance = new EnemyFactory();
        }
        return instance;
    }

    public Enemy createEnemy(LevelData.Symbols symbol, PacmanGame.Coordinates startPosition, GridPane area, LevelData data) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (map.containsKey(symbol)) {
            return (Enemy) map.get(symbol).getConstructor(PacmanGame.Coordinates.class, GridPane.class, LevelData.class).newInstance(startPosition, area, data);
        } else {
            return null;
        }
    }
}
