// CatanMainMenu.java
package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameState;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.CreditsView;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.MainMenuView;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.PlayerSetupView;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.RulesView;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.InputStream;
import java.util.List;


/**
 * Custom FXGL main menu for The Settlers of Java.
 * 
 * Provides navigation to Player Setup, Rules, and Credits screens,
 * and allows the user to start a new game or exit the application.
 */
public class CatanMainMenu extends FXGLMenu {

    private VBox mainMenu;
    private VBox creditsView;
    private VBox rulesView;
    private VBox playerSetupView;

    
    /**
     * Constructs the main menu using FXGL's {@link FXGLMenu} with type {@code MAIN_MENU}.
     * Initializes and lays out the UI components.
     */
    public CatanMainMenu() {
        super(MenuType.MAIN_MENU);
        getContentRoot().setCursor(Cursor.DEFAULT);
        setupUI();
    }

    /**
     * Initializes and arranges all views for the menu, including background and submenus.
     * Connects navigation logic to appropriate buttons.
     */
    private void setupUI() {
    	
        // Load background image
        String backgroundImagePath = "/images/background.png";
        InputStream imageStream = getClass().getResourceAsStream(backgroundImagePath);

        ImageView backgroundView = new ImageView();
        if (imageStream != null) {
            Image backgroundImage = new Image(imageStream);
            backgroundView.setImage(backgroundImage);
        }

        backgroundView.setPreserveRatio(true);
        backgroundView.setFitHeight(getAppHeight());
        backgroundView.setFitWidth(getAppWidth());

        // Main menu layout
        mainMenu = MainMenuView.create(
        	    this::showPlayerSetup,
        	    this::showRules,
        	    this::showCredits,
        	    this::fireExit
        	);

        	// Other views
        	creditsView = CreditsView.create(this::showMainMenu);
        	rulesView = RulesView.create(this::showMainMenu);

        	playerSetupView = PlayerSetupView.create(
        	    this::showMainMenu, // Back button handler
        	    players -> {
        	    	App.getGameStatus().deletePlayersList();
        	        players.forEach(p -> App.getGameStatus().addPlayer(p));
        	        fireNewGame(); 
        	    }
        	);
        	
        StackPane root = new StackPane(backgroundView, mainMenu, creditsView, rulesView, playerSetupView);
        root.setAlignment(Pos.CENTER);

        showMainMenu();

        getContentRoot().getChildren().add(root);
    }

    /**
     * Displays the main menu and hides all other views.
     */
    private void showMainMenu() {
        mainMenu.setVisible(true);
        creditsView.setVisible(false);
        rulesView.setVisible(false);
        playerSetupView.setVisible(false);
    }

    /**
     * Displays the credits screen and hides all other views.
     */
    private void showCredits() {
        mainMenu.setVisible(false);
        creditsView.setVisible(true);
        rulesView.setVisible(false);
    }

    /**
     * Displays the rules screen and hides all other views.
     */
    private void showRules() {
        mainMenu.setVisible(false);
        creditsView.setVisible(false);
        rulesView.setVisible(true);
    }
    
    /**
     * Displays the player setup screen and hides all other views.
     */
    private void showPlayerSetup() {
        mainMenu.setVisible(false);
        creditsView.setVisible(false);
        rulesView.setVisible(false);
        playerSetupView.setVisible(true);
    }

}
