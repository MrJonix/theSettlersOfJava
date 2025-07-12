package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Utility class for creating consistently styled JavaFX buttons.
 * Provides predefined sizes, fonts, colors, and hover effects.
 */

public class UIHelpers {

	/**
	 * Creates a full-size styled button with specified text, action, and colors.
	 *
	 * @param text      the text displayed on the button
	 * @param action    the Runnable executed when the button is clicked (can be null)
	 * @param bgColor   the background color in CSS format (e.g., "#3498db")
	 * @param textColor the text color in CSS format (e.g., "white")
	 * @return the styled {@link Button}
	 */
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

	/**
	 * Creates a half-size styled button for use in compact UI layouts.
	 *
	 * @param text      the button label
	 * @param action    the action to perform when clicked
	 * @param bgColor   background color
	 * @param textColor text color
	 * @return a 200x60 sized {@link Button} with hover style
	 */
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
