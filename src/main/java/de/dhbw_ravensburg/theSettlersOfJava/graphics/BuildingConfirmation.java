package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class BuildingConfirmation {
	
	public static boolean askForBuildingConfirmation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Build");
        alert.setHeaderText("Are you sure you want to build here?");
        alert.setContentText("This location will be used to construct a building. Do you wish to proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
