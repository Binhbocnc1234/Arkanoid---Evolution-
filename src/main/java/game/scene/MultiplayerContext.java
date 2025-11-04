package game.scene;

import network.RelayClient;

public class MultiplayerContext {
    private final int playerId;
    private final RelayClient client;
    private static MultiplayerContext instance;

    private MultiplayerContext(int playerId, RelayClient client) {
        this.playerId = playerId;
        this.client = client;
    }

    public static void init(int playerId, RelayClient client) {
        instance = new MultiplayerContext(playerId, client);
    }

    public static MultiplayerContext getInstance() {
        return instance;
    }

    public boolean isPlayer1() {
        return playerId == 1;
    }

    public boolean isPlayer2() {
        return playerId == 2;
    }

    public RelayClient getClient() {
        return client;
    }
    
    public void clear() {
        instance = null;
    }
}