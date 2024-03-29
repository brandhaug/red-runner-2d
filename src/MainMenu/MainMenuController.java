package MainMenu;

import Game.GameController;
import Highscores.FileHandler;
import Jar.JarUtil;
import SceneChanger.SceneChanger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class MainMenuController {

    @FXML
    private Button infoButton, exitButton, highscoresButton, playButtonSurvival,
            playButtonLevel, soundButton, musicButton, BULLET_A, BULLET_B, BULLET_C,
            createLevelButton, confirm, decline, killCoin1, killCoin2, killCoin3,
            exitBulletBuyPane, exitChooseBulletPane;
    @FXML
    private Text bullet1Price, bullet2Price;
    @FXML
    private Text bullet3Price;
    @FXML
    private Text bullet1Owned;
    @FXML
    private Text bullet2Owned;
    @FXML
    private Text bullet3Owned;
    @FXML
    private Label killCoinInfo;
    @FXML
    private Pane chooseBulletPane;
    @FXML
    private Pane buyBulletPane;
    @FXML
    private Pane buyBulletPaneConfirm;
    @FXML
    private Label errorLabel;

    private SceneChanger sceneChanger;
    private Preferences preferences = Preferences.userRoot();
    private Clip musicClip;
    private boolean initialized = false;
    private boolean bullet1Available;
    private boolean bullet2Available;
    private boolean bullet3Available;
    private String bulletPrice;
    public static String selectedBullet;
    private FileHandler fileHandler;

    /**
     * Initializes a new FileHandler instance
     */
    public MainMenuController() {
        fileHandler = FileHandler.getInstance();
    }

    /**
     * Exits the game
     */
    @FXML
    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Sets sound to on if its off. Sets if to off if its on. Changes icon accordingly.
     */
    @FXML
    protected void toggleSound() {
        boolean soundOn = preferences.getBoolean("sound", true);

        if (initialized) {
            preferences.putBoolean("sound", !soundOn);
            soundOn = !soundOn;
        }

        if (soundOn) {
            soundButton.setStyle("-fx-graphic: 'Resources/buttons/sound_on.png'");
        } else {
            soundButton.setStyle("-fx-graphic: 'Resources/buttons/sound_off.png'");
        }
    }

    /**
     * Sets music to on if its off. Sets if to off if its on. Changes icon accordingly.
     */
    @FXML
    protected void toggleMusic() {
        boolean musicOn = preferences.getBoolean("music", true);

        if (initialized) {
            preferences.putBoolean("music", !musicOn);
            musicOn = !musicOn;
        }

        if (musicOn) {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_on.png'");
            musicClip.start();
        } else {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_off.png'");
            musicClip.stop();
        }
    }

    /**
     * Changes the scene to the classic level selection scene.
     * Stops the music.
     *
     * @param event the ActionEvent
     */
    @FXML
    protected void openGameLevel(ActionEvent event) {
        musicClip.stop();
        sceneChanger.changeScene(event, "/ClassicLevels/ClassicLevels.fxml", true);
    }

    /**
     * Changes the scene to the survival game scene. Stops the music.
     *
     * @param event the Action Event
     */
    @FXML
    private void openGameSurvival(ActionEvent event) {
        musicClip.stop();

        GameController.setMapName("/Resources/maps/survivalfolder/survival");

        sceneChanger.changeScene(event, "/Game/Game.fxml", true);
    }

    /**
     * Starts the game if the bullet is available. Forwards to bullet buy screen if user has enough money to buy.
     *
     * @param selectedBulletType the selected bullet type
     * @param bulletAvailable    true if bullet is available, false if not
     * @param bulletPrice        the price of the bullet
     * @param event              the ActionEvent
     * @param points             the kill coins
     */
    private void setBullet(String selectedBulletType, boolean bulletAvailable, Text bulletPrice, ActionEvent event, int points) {
        selectedBullet = selectedBulletType;
        if (bulletAvailable) {
            openGameSurvival(event);
        } else if (points >= Integer.parseInt(bullet1Price.getText())) {
            buyBulletPane.setVisible(true);
            this.bulletPrice = bulletPrice.getText();
        }
    }

    /**
     * When selecting a bullet in Survival Mode, tries to either use it if its available or buy if its not.
     *
     * @param event the ActionEvent
     */
    @FXML
    protected void bulletSelected(ActionEvent event) {
        int points = getKillCoins();

        if (BULLET_A.isFocused()) {
            setBullet(BULLET_A.getId(), bullet1Available, bullet1Price, event, points);
        }
        if (BULLET_B.isFocused()) {
            setBullet(BULLET_B.getId(), bullet2Available, bullet2Price, event, points);
        }
        if (BULLET_C.isFocused()) {
            setBullet(BULLET_C.getId(), bullet3Available, bullet3Price, event, points);
        }

        if (exitChooseBulletPane.isFocused()) chooseBulletPane.setVisible(false);
    }

    /**
     * Get the content of the survival file by decrypting, reading and encrypting.
     *
     * @return ArrayList of Strings.
     */
    private ArrayList<String> getSurvivalFileContent() {
        fileHandler.decryptFile(fileHandler.getSurvivalPath());
        ArrayList<String> arrayList = fileHandler.getArrayListFromFile(fileHandler.getSurvivalPath());
        fileHandler.encryptFile(fileHandler.getSurvivalPath());
        return arrayList;
    }

    /**
     * Gets the kill coins by reading the survival file. Gives user an error message if something goes wrong.
     *
     * @return kill coins
     */
    private int getKillCoins() {
        int points = 0;
        try {
            points = Integer.parseInt(getSurvivalFileContent().get(0));
        } catch (Exception e) {
            if (!fileHandler.getErrorLabel().equals("")) {
                errorLabel.setText(fileHandler.getErrorLabel());
                playButtonLevel.setDisable(true);
                playButtonSurvival.setDisable(true);
                highscoresButton.setDisable(true);
            } else {
                errorLabel.setText("Survival file seems to be corrupt. Deletion of 'survival_info.txt' may help, but all your survival progress will be lost");
                playButtonSurvival.setDisable(true);
            }
        }
        return points;
    }

    /**
     * Buys the selected bullet by reading and writing to file. Decreasing kill coins by bullet price.
     */
    @FXML
    protected void bulletPurchase() {
        int points = getKillCoins();

        if (confirm.isFocused()) {
            try {
                ArrayList<String> survivalFileContent = getSurvivalFileContent();
                survivalFileContent.set(0, Integer.toString(points - Integer.parseInt(bulletPrice)));
                Files.write(fileHandler.getSurvivalPath(), survivalFileContent, StandardCharsets.ISO_8859_1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateAvailableBullets();
            buyBulletPane.setVisible(false);
        }

        killCoinInfo.setText("KillCoins: " + getKillCoins());

        if (exitBulletBuyPane.isFocused() || decline.isFocused()) buyBulletPane.setVisible(false);
    }

    /**
     * Sets the selectedBullet of the class to available by reading and writing to survival file.
     */
    private void updateAvailableBullets() {
        try {
            int position = 0;

            BufferedReader reader = new BufferedReader(new FileReader("survivalInfo.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.contains(selectedBullet)) {
                    ArrayList<String> survivalFileContent = getSurvivalFileContent();
                    survivalFileContent.set(position, selectedBullet + "=true");
                    Files.write(fileHandler.getSurvivalPath(), survivalFileContent, StandardCharsets.ISO_8859_1);
                    break;
                }
                position++;
                line = reader.readLine();
            }

            chooseBullet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Chooses bullet from available bullets, by reading the survival file. Decrypts and encrypts file after open.
     */
    @FXML
    protected void chooseBullet() {
        try {
            fileHandler.decryptFile(fileHandler.getSurvivalPath());
            BufferedReader reader = new BufferedReader(new FileReader("survivalInfo.txt"));
            String line = reader.readLine();

            line = reader.readLine();
            if (line.contains("true")) {
                BULLET_A.setStyle("-fx-graphic: 'Resources/buttons/bullet_A_Available.png'");
                bullet1Available = true;
                bullet1Price.setVisible(false);
                killCoin1.setVisible(false);
                bullet1Owned.setVisible(true);
            }

            line = reader.readLine();
            if (line.contains("true")) {
                BULLET_B.setStyle("-fx-graphic: 'Resources/buttons/bullet_B_Available.png'");
                bullet2Available = true;
                bullet2Price.setVisible(false);
                killCoin2.setVisible(false);
                bullet2Owned.setVisible(true);
            }

            line = reader.readLine();
            if (line.contains("true")) {
                BULLET_C.setStyle("-fx-graphic: 'Resources/buttons/bullet_C_Available.png'");
                bullet3Available = true;
                bullet3Price.setVisible(false);
                killCoin3.setVisible(false);
                bullet3Owned.setVisible(true);
            }
            fileHandler.encryptFile(fileHandler.getSurvivalPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        chooseBulletPane.setVisible(true);
    }

    /**
     * Opens the info scene. Stops the music.
     *
     * @param event the ActionEvent
     */
    @FXML
    protected void openInfo(ActionEvent event) {
        musicClip.stop();
        sceneChanger.changeScene(event, "/Info/Info.fxml", true);
    }

    /**
     * Opens the create level scene. Stops the music.
     *
     * @param event the ActionEvent
     */
    @FXML
    protected void openCreateLevel(ActionEvent event) {
        musicClip.stop();
        sceneChanger.changeScene(event, "/CreateLevel/CreateLevel.fxml", false);
    }

    /**
     * Opens the high score scene. Stops the music.
     *
     * @param event the ActionEvent
     */
    @FXML
    protected void openHighScores(ActionEvent event) {
        musicClip.stop();
        sceneChanger.changeScene(event, "/Highscores/Highscores.fxml", true);
    }

    /**
     * Initializes a new SceneChanger.
     * Sets kill coin text.
     * Initializes music and sounds.
     *
     * @see SceneChanger
     */
    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        killCoinInfo.setText("KillCoins: " + getKillCoins());

        try {
            initializeMusic();
            toggleMusic();
            toggleSound();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }

        initialized = true;
    }


    /**
     * Create an AudioInputStream from a given sound file
     * Acquire music format and create a DataLine.Infoobject
     * Obtain the Clip
     * Open the AudioInputStream and start playing
     *
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    private void initializeMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        InputStream inputStream = getClass().getResourceAsStream("/Resources/music/main_song.wav");
        InputStream bufferedInputStream = new BufferedInputStream(inputStream);
        AudioInputStream musicStream = AudioSystem.getAudioInputStream(bufferedInputStream);
        musicClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, musicStream.getFormat()));
        musicClip.open(musicStream);
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        musicClip.start();
    }
}