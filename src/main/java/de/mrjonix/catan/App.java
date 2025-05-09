
package de.mrjonix.catan;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Taskbar.Feature;


public class App extends GameApplication {

    private final List<HexagonTile> tiles = new ArrayList<>();
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 1200;
    private static final int HEX_SIZE = 100;
    private static final int[][] coords = {{0,-2},{-2,0},{-1,-1},{0,-1},{-1,0},{0,0},{0,2},{2,0},{1,1},{0,1},{1,0},{-1,1},{1,-1},{-1,2},{2,-1},{-2,1},{-2,2},{1,-2},{2,-2}};
    private static final int[][] waterCoords = {{-3,0},{-2,-1},{-1,-2},{0,-3},{1,-3},{2,-3},{3,-3},{3,-2},{3,-1},{3,0},{2,1},{1,2},{0,3},{-1,3},{-2,3},{-3,3},{-3,2},{-3,1}};
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("theSettlersOfJava");
        settings.setAppIcon("icon.png");
        
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.PLUS, () -> {
            double currentZoom = getGameScene().getViewport().getZoom();
            if(currentZoom < 2.5)
            getGameScene().getViewport().setZoom(currentZoom + 0.025);
        });

        onKey(KeyCode.MINUS, () -> {
        	
            double currentZoom = getGameScene().getViewport().getZoom();
            if(currentZoom > 1)
            getGameScene().getViewport().setZoom(currentZoom - 0.025);
        });

        onKey(KeyCode.UP, () -> {
            double y = getGameScene().getViewport().getY();
            if (y > 0)
            getGameScene().getViewport().setY(y - 10); // move up
        });

        onKey(KeyCode.DOWN, () -> {
            double y = getGameScene().getViewport().getY();
            getGameScene().getViewport().setY(y + 10); // move down
        });

        onKey(KeyCode.LEFT, () -> {
            double x = getGameScene().getViewport().getX();
            if (x > 0)
            getGameScene().getViewport().setX(x - 10); // move left
        });

        onKey(KeyCode.RIGHT, () -> {
            double x = getGameScene().getViewport().getX();
            getGameScene().getViewport().setX(x + 10); // move righ
        });
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CatanFactory());
        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                Image dockIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/assets/textures/icon.png"));
                taskbar.setIconImage(dockIcon);
            }
        }

        Random random = new Random();
        
        
        		

        List<Material> materialsList = Material.generateMaterialList();
        for (int i = 0; i < coords.length; i++) {
            int[] coord = coords[i];
            Material material = materialsList.get(i);

            int number = 0;
            do {
            	number = random.nextInt(2,13);
            } while (number == 7);
            
            if (material == Material.DESSERT) {
                number = 0; // Dessert bekommt keine Zahl (0)
            }
            HexagonTile tile = new HexagonTile(coord[0], coord[1], HEX_SIZE, number, material);
            tiles.add(tile);
            spawn("hexagon", tile.getSpawnData());
        }
        
        for (int i = 0; i < waterCoords.length; i++) {
        	int[] coord = waterCoords[i];
        	HexagonTile tile = new HexagonTile(coord[0], coord[1], HEX_SIZE, 0, 
        						i % 2 == 0 ? Material.WATER : Material.BOAT_WATER);
        	tiles.add(tile);
        	spawn("hexagon", tile.getSpawnData());
        	
        }
       
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}

