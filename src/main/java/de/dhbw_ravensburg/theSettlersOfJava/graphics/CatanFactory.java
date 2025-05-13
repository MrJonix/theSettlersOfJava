package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import java.net.URL;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CatanFactory implements EntityFactory {

    @Spawns("hexagon")
    public Entity newHexagon(SpawnData data) {
    	
        int number = data.get("numberToken");
        
        HexType type = data.get("hexType");
        // Load the selected image
        URL resourceUrl = getClass().getResource(type.getImagePath());
        if (resourceUrl == null) {
            throw new IllegalStateException("Image resource not found: " + type.getImagePath());
        }


        Polygon hex = createHexagon(Hex.HEX_SIZE);
        // Load the selected image
        Image image = new Image(resourceUrl.toExternalForm());
        ImageView imageView = new ImageView(image);

        // Set the image Hex.HEX_SIZE to fit the hexagon
        double hexWidth = Hex.HEX_SIZE * 2 - 10; // Hexagon's width
        double hexHeight = Math.sqrt(3) * Hex.HEX_SIZE - 10; // Hexagon's height

        // Adjust the image's Hex.HEX_SIZE based on the hexagon's Hex.HEX_SIZE
        imageView.setFitWidth(hexWidth);  
        imageView.setFitHeight(hexWidth);

        imageView.setPreserveRatio(true);  // Ensure the aspect ratio is maintained

        // Center the image within the hexagon by adjusting the translateX and translateY
        imageView.setTranslateX(-hexHeight / 2);  // Center the image horizontally within the hexagon
        imageView.setTranslateY(-hexWidth / 2); // Center the image vertically within the hexagon
        Color color;
        if (number == 6 || number == 8) {
        	color = Color.RED;
        } else {
        	color = Color.BLACK;
        }
        StackPane stack = new StackPane();
        if (number != 0) {
	        Circle circle = new Circle(25); // Radius 25
	        circle.setFill(Color.WHITE);
	
	        Text text = new Text(String.valueOf(number));
	        text.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 30));
	        
	        
	        text.setFill(color);
	
	        stack = new StackPane(circle, text);
	        stack.setTranslateX(-25);
	        stack.setTranslateY(-25);
        }
        
        
        // Build the entity with the hexagon, the selected image as a view, and the number in the center
        Entity box = entityBuilder(data)
                .viewWithBBox(hex) // Add the hexagon shape as background
                .view(imageView)  // Add the selected image on top of the hexagon
                .view(stack)
                .build();
        
        box.getViewComponent().getChildren().forEach(node -> {
            node.setOnMouseClicked(e -> {
                FXGL.getDialogService().showMessageBox("You clicked the box with the Material " + type);
            });
        });

        return box;
    }

    private Polygon createHexagon(double size) {
        Polygon hex = new Polygon();
        
        // Set the starting angle to 30 degrees for the flat-top hexagon
        double startingAngle = Math.toRadians(30); 

        for (int i = 0; i < 6; i++) {
            // Adjust the angle to ensure the hexagon is properly aligned
            double angle = startingAngle + Math.toRadians(60 * i); // Add 60° for each vertex
            // Calculate the x and y position for each point
            double x = size * Math.cos(angle);
            double y = size * Math.sin(angle);
            hex.getPoints().addAll(x, y);
        }

        // Apply color and stroke to the hexagon
        hex.setFill(Color.WHITE);
        hex.setStroke(Color.WHITE);
        return hex;
    }
}
