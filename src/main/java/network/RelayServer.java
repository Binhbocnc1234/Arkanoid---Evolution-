package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class RelayServer {
    private final Server server = new Server();
    private Connection playerA, playerB;

    public void start() throws Exception {
        // Register byte[] class with Kryo for serialization
        server.getKryo().register(byte[].class);
        
        server.start();
        // TCP-only mode for ngrok compatibility
        server.bind(54555);

        server.addListener(new Listener() {
            @Override
            public void connected(Connection c) {
                System.out.println("Client connected: " + c.getID());
                if (playerA == null) {
                    playerA = c;
                    // assign id 1
                    c.sendTCP(new byte[] {(byte)0x01, (byte)1});
                }
                else if (playerB == null) {
                    playerB = c;
                    c.sendTCP(new byte[] {(byte)0x01, (byte)2});
                    // inform playerA that playerB joined (optional)
                    if (playerA != null) playerA.sendTCP(new byte[] {(byte)0x06}); // opcode 0x06 = PEER_JOINED
                }
                else {
                    // room full
                    c.sendTCP(new byte[] {(byte)0x01, (byte)0});
                    c.close();
                }
            }

            @Override
            public void disconnected(Connection c) {
                System.out.println("Client disconnected: " + c.getID());
                if (c == playerA) {
                    playerA = null;
                    if (playerB != null) playerB.sendTCP(new byte[] {(byte)0x05}); // peer disconnect
                }
                if (c == playerB) {
                    playerB = null;
                    if (playerA != null) playerA.sendTCP(new byte[] {(byte)0x05});
                }
            }

            public void received(Connection c, Object o) {
                if (!(o instanceof byte[] data)) return;
                if (data.length < 1) return;
                int opcode = data[0] & 0xFF;
                // START opcode from player1 should be broadcast to both
                if (opcode == 0x02) {
                    if (playerA != null) playerA.sendTCP(new byte[] {(byte)0x02});
                    if (playerB != null) playerB.sendTCP(new byte[] {(byte)0x02});
                    return;
                }
                // DISCONNECT forwarded
                if (opcode == 0x05) {
                    Connection target = (c == playerA ? playerB : playerA);
                    if (target != null) target.sendTCP(new byte[] {(byte)0x05});
                    return;
                }
                // default relay: forward to peer
                Connection target = (c == playerA ? playerB : playerA);
                if (target != null) target.sendTCP(data);
            }
        });

        // Thông báo cho chủ server
        String publicIP = getPublicIP();
        
        System.out.println("Public IP: " + publicIP);
        System.out.println("Relay server started on ports 54555/54777");
    }
    public static String getPublicIP() {
        try {
            URL url = new URL("https://api.ipify.org");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return br.readLine();
            }
        } catch (Exception e) {
            return "Unknown";
        }
    }
    public static void main(String[] args) throws Exception {
        new RelayServer().start();
    }
}
