package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UIHelpers {

	public static Button createStyledButton(String text, Runnable action, String bgColor, String textColor) {
	    Button button = new Button(text);
	    button.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 18));
	    button.setPrefSize(400, 60);
	    button.setStyle("-fx-background-color: " + bgColor + ";"
	                  + "-fx-text-fill: " + textColor + ";"
	                  + "-fx-background-radius: 12;");

	    if (action != null) button.setOnAction(e -> action.run());

	    // Add hover effect
	    button.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
	        if (isNowHovered) {
	            // Lighten the background color or change to a fixed hover color
	            button.setStyle("-fx-background-color: derive(" + bgColor + ", 20%);"  // lighter by 20%
	                          + "-fx-text-fill: " + textColor + ";"
	                          + "-fx-background-radius: 12;");
	        } else {
	            // revert to original style
	            button.setStyle("-fx-background-color: " + bgColor + ";"
	                          + "-fx-text-fill: " + textColor + ";"
	                          + "-fx-background-radius: 12;");
	        }
	    });

	    return button;
	}

	public static Button createHalfSizeButton(String text, Runnable action, String bgColor, String textColor) {
	    Button button = new Button(text);
	    button.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 18));
	    button.setPrefSize(200, 60);
	    button.setStyle("-fx-background-color: " + bgColor + ";"
	                  + "-fx-text-fill: " + textColor + ";"
	                  + "-fx-background-radius: 12;");

	    if (action != null) button.setOnAction(e -> action.run());

	    // Add hover effect
	    button.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
	        if (isNowHovered) {
	            button.setStyle("-fx-background-color: derive(" + bgColor + ", 20%);"
	                          + "-fx-text-fill: " + textColor + ";"
	                          + "-fx-background-radius: 12;");
	        } else {
	            button.setStyle("-fx-background-color: " + bgColor + ";"
	                          + "-fx-text-fill: " + textColor + ";"
	                          + "-fx-background-radius: 12;");
	        }
	    });

	    return button;
	}

}
