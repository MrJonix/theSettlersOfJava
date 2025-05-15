package de.dhbw_ravensburg.theSettlersOfJava;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.onKey;

import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Taskbar.Feature;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.game.GameController;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.CatanFactory;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class App extends GameApplication {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    
	@Override
	protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("SettlersOfJava");
        settings.setAppIcon("icon.png");
	}
    @Override
    protected void initInput() {


    	onKey(KeyCode.UP, () -> {
            double y = getGameScene().getViewport().getY();
            getGameScene().getViewport().setY(y - 10); // move up
        });
        onKey(KeyCode.DOWN, () -> {
            double y = getGameScene().getViewport().getY();
            getGameScene().getViewport().setY(y + 10); // move down
        });

        onKey(KeyCode.LEFT, () -> {
            double x = getGameScene().getViewport().getX();
            getGameScene().getViewport().setX(x - 10); // move left
        });

        onKey(KeyCode.RIGHT, () -> {
            double x = getGameScene().getViewport().getX();
            getGameScene().getViewport().setX(x + 10); // move right
        });
    }
    private void setTaskbar(String res) {
        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                Image dockIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource(res));
                taskbar.setIconImage(dockIcon);
            }
        }
    }
    
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CatanFactory());
        setTaskbar("/assets/textures/icon.png");
        new GameController();
    }
    	
	public static void main( String[] args ) {launch(args);}

}
