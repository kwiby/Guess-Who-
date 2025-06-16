import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GuessWhoGame {
    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        new GUI();
        GameController.audioSetup();
    }
}