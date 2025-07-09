package de.dhbw_ravensburg.theSettlersOfJava.game;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import java.util.Map;
public class TradeOffer{
    private final Player offerer;
    private final Player receiver;
    private final Map<ResourceType, Integer> offeredResources;
    private final Map<ResourceType, Integer> requestedResources;
    public TradeOffer(Player offerer, Player receiver,
                      Map<ResourceType, Integer> offeredResources,
                      Map<ResourceType, Integer> requestedResources) {
        this.offerer = offerer;
        this.receiver = receiver;
        this.offeredResources = offeredResources;
        this.requestedResources = requestedResources;
    }

    public Player getOfferer() {
        return offerer;
    }

    public Player getReceiver() {
        return receiver;
    }

    public Map<ResourceType, Integer> getOfferedResources() {
        return offeredResources;
    }

    public Map<ResourceType, Integer> getRequestedResources() {
        return requestedResources;
    }

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

    public void decline() {
        System.out.println("Handel wurde abgelehnt.");
    }
}