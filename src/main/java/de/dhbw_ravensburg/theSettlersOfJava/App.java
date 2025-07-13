package de.dhbw_ravensburg.theSettlersOfJava;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Taskbar.Feature;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;

import de.dhbw_ravensburg.theSettlersOfJava.game.GameController;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameStatus;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.CatanFactory;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.CatanMainMenu;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexPosition;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;


/**
 * Main FXGL game application for SettlersOfJava.
 * 
 * Initializes game settings, user input, scene, camera zoom, and taskbar integration.
 */
public class App extends GameApplication {
    private static int WIDTH = 1920;
    private static int HEIGHT = 1080;
    private static GameController controller;
    private double zoom = 1.0;
    private static GameStatus status = new GameStatus();
    
    /**
     * Initializes the FXGL game settings such as resolution, title, fullscreen, and menus.
     *
     * @param settings the FXGL game settings to configure
     */
	@Override
	protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setFullScreenAllowed(true); 
        //settings.setFullScreenFromStart(true);
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
    
	/**
     * Initializes user input for zooming and camera movement using keyboard keys.
     */
    @Override
    protected void initInput() {
        FXGL.getGameScene().getRoot().setCursor(Cursor.DEFAULT);

        Input input = FXGL.getInput();

        input.addAction(new UserAction("Zoom In") {
            @Override
            protected void onAction() {
                zoomBy(0.04);
            }
        }, KeyCode.PLUS);

        input.addAction(new UserAction("Zoom Out") {
            @Override
            protected void onAction() {
                zoomBy(-0.04);
            }
        }, KeyCode.MINUS);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                moveCamera(0, -10);
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                moveCamera(0, 10);
            }
        }, KeyCode.DOWN);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                moveCamera(-10, 0);
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                moveCamera(10, 0);
            }
        }, KeyCode.RIGHT);
    }

    /**
     * Moves the camera viewport by the specified amount.
     *
     * @param dx horizontal shift
     * @param dy vertical shift
     */
    private void moveCamera(double dx, double dy) {
        Viewport viewport = getGameScene().getViewport();
        viewport.setX(viewport.getX() + dx);
        viewport.setY(viewport.getY() + dy);
    }
    
    /**
     * Zooms the camera in or out and keeps the viewport centered.
     *
     * @param delta the zoom increment (positive to zoom in, negative to zoom out)
     */
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

    /**
     * Sets the taskbar icon image for the game window, if supported by the OS.
     *
     * @param res the resource path to the icon image
     */
    private void setTaskbar(String res) {
        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                Image dockIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource(res));
                taskbar.setIconImage(dockIcon);
            }
        }
    }
    /**
     * Initializes the game logic, sets up the board and controller, and centers the viewport.
     */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CatanFactory());
        controller = new GameController();
        controller.setupPhase();
        
        Viewport viewport = getGameScene().getViewport();
        HexPosition pos = controller.getGameBoard().getHexByPosition(new HexPosition(0,0)).getPosition();
        
        viewport.setX(pos.getX() - WIDTH / 2);
        viewport.setY(pos.getY() - HEIGHT / 2);
        FXGL.set("GameBoard", controller.getGameBoard());

    }
    
    /**
     * Returns the global game controller instance.
     *
     * @return the game controller
     */
    public static GameController getGameController() {
    	return controller;
    }
    
    /**
     * Returns the shared game status object.
     *
     * @return the game status
     */
    public static GameStatus getGameStatus() {
		return status;
    }
    
    /**
     * Entry point to launch the FXGL game application.
     *
     * @param args command line arguments
     */
	public static void main(String[] args) {
	    launch(args);
	}
}