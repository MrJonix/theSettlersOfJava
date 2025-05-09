package de.mrjonix.catan;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import java.net.URL;
import java.util.Map;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// EntityFactory für das Hexagon
public class CatanFactory implements EntityFactory {
	private static final Map<Material, String> resourceImages = Map.of(
		    Material.WOOD, "/wood.png",
		    Material.WOOL, "/wool.png",
		    Material.DESSERT, "/dessert.png",
		    Material.IRON, "/iron.png",
		    Material.CLAY, "/clay.png",
		    Material.WHEAT, "/wheat.png",
		    Material.WATER, "/water.png",
		    Material.BOAT_WATER, "/water-boat-left.png"
		);

    @Spawns("hexagon")
    public Entity newHexagon(SpawnData data) {
        double size = data.get("size");
        int number = data.get("number");
        
        // Load the selected image
        URL resourceUrl = getClass().getResource(resourceImages.get(data.get("material")));
        if (resourceUrl == null) {
            System.err.println("Image resource not found. Ensure that the image file is correctly located in the resources folder.");
        } 

        Polygon hex = createHexagon(size);


        // Load the selected image
        Image image = new Image(resourceUrl.toExternalForm());
        ImageView imageView = new ImageView(image);

        // Set the image size to fit the hexagon
        double hexWidth = size * 2; // Hexagon's width
        double hexHeight = Math.sqrt(3) * size; // Hexagon's height

        // Adjust the image's size based on the hexagon's size
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
        return entityBuilder(data)
                .viewWithBBox(hex) // Add the hexagon shape as background
                .view(imageView)  // Add the selected image on top of the hexagon
                .view(stack)
                .build();
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
        hex.setFill(Color.LIGHTGRAY);
        hex.setStroke(Color.WHITE);
        return hex;
    }
}

