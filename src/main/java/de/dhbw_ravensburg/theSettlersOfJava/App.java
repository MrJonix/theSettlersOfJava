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

import de.dhbw_ravensburg.theSettlersOfJava.game.Dice;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameController;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.CatanFactory;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class App extends GameApplication {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static GameController controller;
    private double zoom = 1.0;
    private Dice dice;
    
	@Override
	protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("SettlersOfJava");
        settings.setAppIcon("icon.png");
	}
    @Override
    protected void initInput() {

    	onKey(KeyCode.PLUS, () -> zoomBy(0.04)); // Zoom in
    	onKey(KeyCode.MINUS, () -> zoomBy(-0.04)); // Zoom out
    
    	onKey(KeyCode.UP, () -> moveCamera(0, -10));
    	onKey(KeyCode.DOWN, () -> moveCamera(0, 10));
    	onKey(KeyCode.LEFT, () -> moveCamera(-10, 0));
    	onKey(KeyCode.RIGHT, () -> moveCamera(10, 0));
    }
    
    private void moveCamera(double dx, double dy) {
        Viewport viewport = getGameScene().getViewport();
        viewport.setX(viewport.getX() + dx);
        viewport.setY(viewport.getY() + dy);
        
        // Reposition the dice to keep it in the bottom right corner
        if (dice != null) {
            dice.positionInBottomRight();
        }
    }
    
    private void zoomBy(double delta) {
        double oldZoom = zoom;
        zoom = Math.max(0.1, zoom + delta);

        Viewport viewport = getGameScene().getViewport();

        int appWidth = WIDTH;
        int appHeight = HEIGHT;

        double centerX = viewport.getX() + appWidth / (2 * oldZoom);
        double centerY = viewport.getY() + appHeight / (2 * oldZoom);

        viewport.setZoom(zoom);

        viewport.setX(centerX - appWidth / (2 * zoom));
        viewport.setY(centerY - appHeight / (2 * zoom));
        
        // Reposition the dice to keep it in the bottom right corner
        if (dice != null) {
            dice.positionInBottomRight();
        }
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
        controller = new GameController();
        
        // Create and add the dice
        dice = new Dice();
    }
    
    public static GameController getGameController() {
    	return controller;
    }
    	
	public static void main( String[] args ) {launch(args);}

}