
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
    private static final String[] resourceImages = { "/wood.png", "/wool.png", "/dessert.png", "/iron.png", "/clay.png", "/wheat.png" };
    private static final int[][] coords = {{0,-2},{-2,0},{-1,-1},{0,-1},{-1,0},{0,0},{0,2},{2,0},{1,1},{0,1},{1,0},{-1,1},{1,-1},{-1,2},{2,-1},{-2,1},{-2,2},{1,-2},{2,-2}};
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
        
        
        		
        for(int[] coord : coords) {
        	int number = 0;
            do {
            	number = random.nextInt(2,13);
            } while (number == 7);
            String imagePath = resourceImages[random.nextInt(resourceImages.length)];
            HexagonTile tile = new HexagonTile(coord[0], coord[1], HEX_SIZE, imagePath, number); 
            tiles.add(tile);
            spawn("hexagon", tile.getSpawnData());
        }
                
            
       
    }

    public static void main(String[] args) {
        launch(args);
    }
}

