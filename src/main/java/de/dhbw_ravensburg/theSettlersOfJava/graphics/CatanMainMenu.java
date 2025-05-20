// CatanMainMenu.java
package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;

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

public class CatanMainMenu extends FXGLMenu {

    private VBox mainMenu;
    private VBox creditsView;
    private VBox rulesView;
    private VBox playerSetupView;

    public CatanMainMenu() {
        super(MenuType.MAIN_MENU);
        getContentRoot().setCursor(Cursor.DEFAULT);
        setupUI();
    }

    private void setupUI() {
        // Background
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

        mainMenu = MainMenuView.create(
        	    this::showPlayerSetup,
        	    this::showRules,
        	    this::showCredits,
        	    this::fireExit
        	);

        	creditsView = CreditsView.create(this::showMainMenu);
        	rulesView = RulesView.create(this::showMainMenu);

        	playerSetupView = PlayerSetupView.create(
        	    this::showMainMenu, // Back button handler
        	    players -> {
        	        // Handle confirmed player list
        	        // You can store this in a shared state or pass it into your game logic
        	        players.forEach(p -> System.out.println(p.name() + " selected " + p.color()));
        	        fireNewGame(); // Start the game
        	    }
        	);
        	
        StackPane root = new StackPane(backgroundView, mainMenu, creditsView, rulesView, playerSetupView);
        root.setAlignment(Pos.CENTER);

        showMainMenu();

        getContentRoot().getChildren().add(root);
    }

    private void showMainMenu() {
        mainMenu.setVisible(true);
        creditsView.setVisible(false);
        rulesView.setVisible(false);
        playerSetupView.setVisible(false);
    }

    private void showCredits() {
        mainMenu.setVisible(false);
        creditsView.setVisible(true);
        rulesView.setVisible(false);
    }

    private void showRules() {
        mainMenu.setVisible(false);
        creditsView.setVisible(false);
        rulesView.setVisible(true);
    }
    private void showPlayerSetup() {
        mainMenu.setVisible(false);
        creditsView.setVisible(false);
        rulesView.setVisible(false);
        playerSetupView.setVisible(true);
    }

}
