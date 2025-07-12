package de.dhbw_ravensburg.theSettlersOfJava.game;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import java.util.Map;


/**
 * Represents a trade offer between two players in the game.
 * 
 * A trade offer consists of a set of resources that the offering player wants to trade away
 * and a set of resources that they request in return from the other player.
 * 
 * The trade can either be accepted—causing a direct exchange of resources between players—
 * or declined, resulting in no changes.
 */
public class TradeOffer{
	/**
     * The player who initiates the trade and offers resources.
     */
    private final Player offerer;
    /**
     * The player who receives the trade offer and can accept or decline.
     */
    private final Player receiver;
    /**
     * The resources offered by the offering player.
     */
    private final Map<ResourceType, Integer> offeredResources;
    /**
     * The resources requested from the receiving player.
     */
    private final Map<ResourceType, Integer> requestedResources;
    
    /**
     * Constructs a new TradeOffer between two players.
     *
     * @param offerer the player making the offer
     * @param receiver the player receiving the offer
     * @param offeredResources the resources the offerer is giving
     * @param requestedResources the resources the offerer wants in return
     */
    public TradeOffer(Player offerer, Player receiver,
                      Map<ResourceType, Integer> offeredResources,
                      Map<ResourceType, Integer> requestedResources) {
        this.offerer = offerer;
        this.receiver = receiver;
        this.offeredResources = offeredResources;
        this.requestedResources = requestedResources;
    }

    /**
     * Returns the player who initiated the trade.
     *
     * @return the offering player
     */
    public Player getOfferer() {
        return offerer;
    }

    /**
     * Returns the player who received the trade offer.
     *
     * @return the receiving player
     */
    public Player getReceiver() {
        return receiver;
    }

    /**
     * Returns the resources offered by the offering player.
     *
     * @return a map of offered resources and their amounts
     */
    public Map<ResourceType, Integer> getOfferedResources() {
        return offeredResources;
    }

    /**
     * Returns the resources requested from the receiving player.
     *
     * @return a map of requested resources and their amounts
     */
    public Map<ResourceType, Integer> getRequestedResources() {
        return requestedResources;
    }

    /**
     * Executes the trade if both players have the required resources.
     * Transfers the offered and requested resources between the two players.
     * 
     * If a player lacks the necessary resources, the trade is aborted and a message is printed.
     */
    public void accept() {
        if (offerer.hasResources(offeredResources) && receiver.hasResources(requestedResources)) {
            offerer.removeResourcesMap(offeredResources);
            receiver.removeResourcesMap(requestedResources);

            offerer.addResourcesMap(requestedResources);
            receiver.addResourcesMap(offeredResources);
        } else {
            System.out.println("Handel fehlgeschlagen: Ein Spieler hat nicht genug Ressourcen.");
        }
    }

    /**
     * Declines the trade offer.
     * No changes are made to either player's resources.
     * This method simply prints a message.
     */
    public void decline() {
        System.out.println("Handel wurde abgelehnt.");
    }
}