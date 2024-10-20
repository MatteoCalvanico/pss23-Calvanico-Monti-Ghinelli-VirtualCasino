package it.unibo.virtualCasino.controller;

// Import for FXML
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Import for sound
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ResourceBundle;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;

/**
 * Base class that define the base methods of a controller:
 * <pre>
- initialize: when the FXML file is launch call receiveData() and setGame();
- sendData: invoked when you need to change scene, save the player in the singleton class;
- receiveData: method to take the instance of the player thanks to the singleton class
- setGame: needs to be override to set your own game.
* </pre>
*/
public abstract class BaseController implements Initializable {

    protected Player currentPlayer; 

    /**
     * Do something when the FXML file is loaded
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receiveData();
        setGame();
    }

    /**
     * Method to be invoked when you need to change scene, save the player in the singleton class
     */
    @FXML
    protected void sendData(){
        // Save the player in the singleton class
        PlayerHolder holderInstance = PlayerHolder.getInstance();
        holderInstance.setPlayerHolded(this.currentPlayer);
    }

    //Method to take the instance of the player thanks to the singleton class
    @FXML
    private void receiveData() {
        PlayerHolder holderInstance = PlayerHolder.getInstance();

        this.currentPlayer = holderInstance.getPlayerHolded();
        
        this.currentPlayer = (this.currentPlayer == null) ? new Player("PROVA") : this.currentPlayer;
    }  
    
    /**
     * Override this method to set your own game
     */
    abstract protected void setGame();

    /**
    * Method to play a sound
    * @param soundFilePath the path of the sound file in this format: "/sounds/sound.mp3"
    * @throws IllegalArgumentException if the file is not found or the path is null
    */
    protected void playSound(String soundFilePath){
        URL resource = getClass().getResource(soundFilePath);

        if (resource == null) {
            throw new IllegalArgumentException("File path is null");
        }

        try {
            Media soundFile = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(soundFile);
            mediaPlayer.play();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error playing sound: " + soundFilePath);
        }
    }

    /**
    * Method to get and set an image in the ImageView
    * @param imageFilePath the path of the image file in this format: "image.png"
    * @return the ImageView with the image set
    * @throws IllegalArgumentException if the file is not found or the path is null
    */
    protected ImageView getImage(String imageFilePath){
        Image sprite;

        if(imageFilePath == null){
            throw new IllegalArgumentException("File path is null");
        } 

        // Check the path (in resources we have different directory) and load the image
        try {
            if (imageFilePath.contains("chip")) {
                sprite = new Image(getClass().getResourceAsStream("/sprite/chips/" + imageFilePath));   
            } else if (imageFilePath.contains("card")) {
                sprite = new Image(getClass().getResourceAsStream("/sprite/cards/" + imageFilePath));   
            } else if (imageFilePath.contains("die")) {
                sprite = new Image(getClass().getResourceAsStream("/sprite/dice/" + imageFilePath));   
            } else {
                throw new IllegalArgumentException("File not found: " + imageFilePath);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during image load: " + imageFilePath);
        }

        // Set the image in the ImageView
        ImageView spriteView = new ImageView(sprite);
        return spriteView;
    }
}