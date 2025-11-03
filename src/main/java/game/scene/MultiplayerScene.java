package game.scene;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import game.UI.MyButton;
import game.UI.MyLabel;
import game.info.GameInfo;
import game.soundmanager.SoundManager;
import network.RelayClient;

/**
 * Player 1 can start the match when player2 is connected.
 */
public class MultiplayerScene extends JPanel implements IDisposable {
	private final MyLabel title;
	private final MyLabel p1Status;
	private final MyLabel p2Status;
	private final MyButton playBtn;
	private final MyButton backBtn;

	private final RelayClient client = new RelayClient();
	private int assignedId = 0; // 0 = not assigned / rejected, 1 or 2
	private boolean peerConnected = false;

	private final String relayHost = "1.55.188.101"; // default host

	public MultiplayerScene() {
		setLayout(null);
		setBackground(new Color(30,20,60));

		title = new MyLabel("Multiplayer", GameInfo.SCREEN_WIDTH/2, 80, 500, 48);
		add(title);

		p1Status = new MyLabel("Player 1: waiting", GameInfo.CAMPAIGN_WIDTH/2, 180, 400, 24);
		add(p1Status);

		p2Status = new MyLabel("Player 2: waiting", GameInfo.CAMPAIGN_WIDTH/2, 240, 400, 24);
		add(p2Status);

		playBtn = new MyButton("Play", GameInfo.CAMPAIGN_WIDTH/2, 360, 200, 70);
		playBtn.setEnabled(false);
		playBtn.addActionListener(e -> {
			// only allowed for player1
			if (assignedId == 1 && peerConnected) {
				client.sendStart();
			}
		});
		add(playBtn);

		backBtn = new MyButton("Back", GameInfo.CAMPAIGN_WIDTH/2, 460, 200, 70);
		backBtn.addActionListener(e -> {
			// disconnect and go back
			try { client.sendDisconnect(); } catch (Exception ex) {}
			client.stop();
			SoundManager.playSound("button");
			GameManager.instance.switchTo(new Lobby());
		});
		add(backBtn);

		// Setup relay listener
		client.setRelayListener(new RelayClient.RelayListener() {
			@Override
			public void onAssigned(int id) {
				assignedId = id;
				updateStatuses();
			}

			@Override
			public void onPeerDisconnected() {
				peerConnected = false;
				updateStatuses();
			}

			@Override
			public void onStart() {
				// server asked to start match
				GameManager.instance.switchTo(new BattleManager(true));
			}
			@Override
			public void onPeerJoined() {
				peerConnected = true;
				updateStatuses();
			}
        });
        try {
            client.start(relayHost); // already async
        } catch (Exception e) {
            System.err.println("Failed to connect to relay: " + e.getMessage());
        }
        
        client.sendJoin();   // safe to call right after
		// // Connect in background thread
		// new Thread(() -> {
		// 	try {
		// 		client.start(relayHost);
		// 		// send a lightweight JOIN so server can react if needed (server already assigns on connect)
		// 		client.sendJoin();
		// 	} catch (Exception ex) {
		// 		System.err.println("Failed to connect to relay: " + ex.getMessage());
		// 	}
		// }, "relay-connector").start();
	}

	private void updateStatuses() {
		if (assignedId == 1) {
			p1Status.setText("Player 1: you (connected)");
		} else {
			p1Status.setText("Player 1: waiting");
		}
		if (assignedId == 2) {
			p2Status.setText("Player 2: you (connected)");
		} else {
			p2Status.setText(peerConnected ? "Player 2: connected" : "Player 2: waiting");
		}

		// enable Play only if this client is player1 and peerConnected
		playBtn.setEnabled(assignedId == 1 && peerConnected);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// could draw decorations here
	}

	@Override
	public void dispose() {
		try { client.sendDisconnect(); } catch (Exception ignored) {}
		client.stop();
	}
}
