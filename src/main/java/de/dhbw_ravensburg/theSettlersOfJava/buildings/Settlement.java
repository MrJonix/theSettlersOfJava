package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a settlement building in the game.
 * Settlements provide basic victory points and can be upgraded to cities.
 */
public class Settlement extends Building {

    private static final Map<ResourceType, Integer> SETTLEMENT_COST = createSettlementCost();

    /**
     * Creates a settlement at the specified hex corner, owned by a player.
     *
     * @param location the hex corner location of the settlement
     * @param owner the player who owns the settlement
     */
    public Settlement(HexCorner location, Player owner) {
        super(location, owner, SETTLEMENT_COST);
    }

    /**
     * Gets the number of victory points provided by the settlement.
     *
     * @return one (1) victory point
     */
    @Override
    public int getVictoryPoints() {
        return 1;
    }

    /**
     * Gets the image path for the settlement's texture based on the owner's color.
     *
     * @return the image path as a string
     */
    @Override
    public String getImagePath() {
        return owner.getPlayerColor() +  "_settlement.png";
    }

    /**
     * Visualizes the settlement on the game board and attaches a click action for upgrades.
     */
    @Override
    public void visualize() {
        Texture texture = FXGL.getAssetLoader().loadTexture(getImagePath());

        // Scale the texture
        texture.setScaleX(0.4);
        texture.setScaleY(0.4);

        // Calculate the position to center the texture on the location
        double x = getLocation().getX() - texture.getWidth() / 2;
        double y = getLocation().getY() - texture.getHeight() / 2;

        // Create and attach entity with click behavior
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(texture)
                .onClick(e -> handleClick())
                .buildAndAttach();
    }

    /**
     * Handles the click interaction on the settlement.
     * Allows the current player to attempt an upgrade to a city.
     */
    private void handleClick() {
        if (getOwner().equals(App.getGameController().getCurrentPlayer())) {
            getOwner().build(new City(getLocation(), getOwner()), success -> {
                if (success) {
                    FXGL.getNotificationService().pushNotification("Gebäude erfolgreich gebaut!");
                    //FXGL.getAudioPlayer().playSound("on_build_success.mp3");
                } else {
                    FXGL.getNotificationService().pushNotification("Bau fehlgeschlagen.");
                }
            });
        }
    }

    /**
     * Creates the resource cost map for building a settlement.
     *
     * @return an unmodifiable map representing the settlement cost
     */

    private static Map<ResourceType, Integer> createSettlementCost() {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);
        cost.put(ResourceType.WHEAT, 1);
        cost.put(ResourceType.WOOL, 1);
        return Collections.unmodifiableMap(cost);
    }

}