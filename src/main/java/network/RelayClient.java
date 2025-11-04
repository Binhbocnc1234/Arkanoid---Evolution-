package network;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class RelayClient {
    private final Client client = new Client();
    private Listener listener;
    public interface RelayListener {
        void onAssigned(int id); // 0=rejected,1,2
        void onPeerDisconnected();
        void onStart();
        void onPeerJoined();
    }
    private RelayListener relayListener;

    public void start(String host) throws Exception {
        // Register byte[] class with Kryo for serialization
        client.getKryo().register(byte[].class);
        
        listener = new Listener() {
            @Override
            public void received(Connection c, Object o) {
                if (!(o instanceof byte[] data)) return;
                if (data.length < 1) return;
                int opcode = data[0] & 0xFF;
                switch (opcode) {
                    case 0x01: // ASSIGN_ID
                        int id = (data.length >= 2) ? (data[1] & 0xFF) : 0;
                        System.out.println("Assigned id: " + id);
                        if (relayListener != null) relayListener.onAssigned(id);
                        break;
                    case 0x02: // START
                        System.out.println("Start received");
                        if (relayListener != null) relayListener.onStart();
                        break;
                    case 0x06: // PEER_JOINED
                        System.out.println("Peer joined");
                        if (relayListener != null) relayListener.onPeerJoined();
                        break;
                    case 0x05: // DISCONNECT
                        System.out.println("Peer disconnected");
                        if (relayListener != null) relayListener.onPeerDisconnected();
                        break;
                    default:
                        // other opcodes (pos) handled elsewhere later
                        break;
                }
            }
        };
        client.addListener(listener);

        // Parse host:tcpPort from the TCP tunnel address
        String[] hostParts = host.split(":");
        String hostname = hostParts[0];
        int tcpPort = Integer.parseInt(hostParts[1]);
        
        // Start the client with update thread
        client.start();
        
        // Start a thread to run client.update() during connection
        Thread updateThread = new Thread(() -> {
            try {
                while (!client.isConnected()) {
                    try {
                        client.update(100);
                    } catch (IOException e) {
                        System.err.println("Error during client update: " + e.getMessage());
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Update thread error: " + e.getMessage());
            }
        }, "client-updater");
        updateThread.setDaemon(true);
        updateThread.start();

        // Try to connect (this will block until connected or timeout)
        try {
            client.connect(5000, hostname, tcpPort);
            System.out.println("Connected to relay in TCP-only mode!");
        } finally {
            // Stop the update thread whether we connected or not
            updateThread.interrupt();
        }
    }

    public void sendPosition(float x, float y) {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putFloat(x).putFloat(y);
        client.sendTCP(buf.array());
    }

    public void setRelayListener(RelayListener l) {
        this.relayListener = l;
    }

    // Send a JOIN message (opcode 0x10) to inform server we're here
    public void sendJoin() {
        byte[] msg = new byte[] {(byte)0x10};
        client.sendTCP(msg);
    }

    // Send START (only player1 will do this)
    public void sendStart() {
        byte[] msg = new byte[] {(byte)0x02};
        client.sendTCP(msg);
    }

    public void sendDisconnect() {
        byte[] msg = new byte[] {(byte)0x05};
        client.sendTCP(msg);
    }

    public void stop() {
        try {
            client.stop();
        } catch (Exception ignored) {}
    }

    // public static void main(String[] args) throws Exception {
    //     if (args.length == 0) {
    //         System.out.println("Usage: java RelayClient <relay_ip>");
    //         return;
    //     }
    //     RelayClient c = new RelayClient();
    //     c.start(args[0]);
    //     for (int i = 0; i < 5; i++) {
    //         c.sendPosition(i * 1.2f, i * 0.7f);
    //         Thread.sleep(1000);
    //     }
    // }
}
