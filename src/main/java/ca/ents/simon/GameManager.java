package ca.ents.simon;

import ca.ents.simon.configuration.ConfigKey;
import ca.ents.simon.configuration.SimonConfiguration;
import ca.ents.simon.io.device.IODevice;
import ca.ents.simon.io.device.SerialPort;
import ca.ents.simon.io.session.SessionManager;
import ca.ents.simon.util.EntsFont;
import ca.ents.simon.util.EntsImage;
import ca.ents.simon.util.UIGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * Game operation for Simon
 */
public class GameManager {

    private static final String ID_TOTAL_PLAYERS_LABEL = "totalPlayersLabel";
    private static final String ID_TOTAL_PLAYERS_COUNT = "totalPlayersCount";

    private UIGroup ui;
    private SessionManager sessionManager;

    private byte playerAddrStart = SimonConfiguration.getByteValue(ConfigKey.GAME_ADDRESSING_START);
    private byte playerAddrEnd = SimonConfiguration.getByteValue(ConfigKey.GAME_ADDRESSING_END);

    public void configureScene(Scene scene, Group root) {
        ui = new UIGroup(root);

        // Header
        Image logoImage = EntsImage.LOGO.original();
        ImageView logo = new ImageView(logoImage);
        logo.setX(10);
        logo.setY(10);
        ui.addChild(logo);

        Text headerText = new Text(Branding.GAME_NAME);
        headerText.setFont(EntsFont.REGULAR.size(144));
        headerText.setX(logoImage.getWidth() + 30);
        headerText.setY(logoImage.getHeight() - 20);
        ui.addChild(headerText);

        // Show total players
        Text totalPlayersLabel = new Text("Total players:");
        totalPlayersLabel.setFont(EntsFont.REGULAR.size(25));
        totalPlayersLabel.setFill(Branding.MUTED_TEXT_COLOR);
        totalPlayersLabel.setId(ID_TOTAL_PLAYERS_LABEL);
        ui.addChild(totalPlayersLabel);

        Text totalPlayersCount = new Text("0");
        totalPlayersCount.setFont(EntsFont.REGULAR.size(25));
        totalPlayersCount.setFill(Branding.MUTED_TEXT_COLOR);
        totalPlayersCount.setId(ID_TOTAL_PLAYERS_COUNT);
        ui.addChild(totalPlayersCount);

        updateTotalPlayers();
    }

    public void beginOperation() {
        String deviceName = SimonConfiguration.getValue(ConfigKey.IO_DEVICE_SERIALPORT_PORTNAME);
        IODevice device;
        if (deviceName == null || deviceName.equalsIgnoreCase("discover"))
            device = new SerialPort();
        else device = new SerialPort(deviceName);
        sessionManager = SessionManager.forDevice(device);

        disoverPlayers();
    }

    public void shutdown() {
        sessionManager.shutdown();
    }

    private void updateTotalPlayers() {
        // TODO
    }

    private void disoverPlayers() {
        for (byte address = playerAddrStart; address <= playerAddrEnd; address++) {
            sessionManager.createOrFindSession(address).tryDiscover();
        }
    }
}
