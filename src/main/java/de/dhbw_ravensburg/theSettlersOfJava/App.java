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
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;

import de.dhbw_ravensburg.theSettlersOfJava.game.GameController;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.CatanFactory;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.CatanMainMenu;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexPosition;
import javafx.scene.input.KeyCode;


public class App extends GameApplication {
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 1080;
    private static GameController controller;
    private double zoom = 1.0;
    
	@Override
	protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("SettlersOfJava");
        settings.setAppIcon("icon.png");
        settings.setMainMenuEnabled(true);
        setTaskbar("/assets/textures/icon.png");
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new CatanMainMenu();
            }
        });
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
        controller = new GameController();
        
        Viewport viewport = getGameScene().getViewport();
        HexPosition pos = controller.getGameBoard().getHexByPosition(new HexPosition(0,0)).getPosition();
        
        viewport.setX(pos.getX() - WIDTH / 2);
        viewport.setY(pos.getY() - HEIGHT / 2);
    }
    
    public static GameController getGameController() {
    	return controller;
    }
    	
	public static void main(String[] args) {
	    launch(args);
	}
}