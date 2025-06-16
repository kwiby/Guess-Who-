import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameController {
    // -=-  Other Game Setup Variables  -=-
    private static ArrayList<Person> characters;

    static Person[][] player1Board; // Player 1 will ALWAYS be an actual user, and NEVER an AI!

    private static String difficulty = "Normal"; // Normal or Hard.

    private static int turnTracker; // 1 = Player 1's turn, 2 = Player 2's turn.

    private static ArrayList<Person> aiCharacterList = new ArrayList<>();
    private static Person aiCharacter; // The character that the AI randomly selects.

    // -=-  Getter Methods  -=-
    /**
     * This method returns the difficulty of the game.
     * @return String difficulty
     */
    public static String getDifficulty() {
        return difficulty;
    }

    /**
     * This method returns whose turn it is (1 for player 1, and 2 for ai or [maybe] second user).
     * @return String turn
     */
    public static int getTurn() {
        return turnTracker;
    }

    /**
     * This method returns the AI's character.
     * @return Person aiCharacter
     */
    public static Person getAiCharacter() {
        return aiCharacter;
    }

    /**
     * This method returns the AI's character list.
     * @return ArrayList<Person> aiCharacterList
     */
    public static ArrayList<Person> getAiCharacterList() {
        return aiCharacterList;
    }


    // -=- Setter Methods  -=-
    /**
     * This method sets the difficulty of the new game.
     * @param difficulty
     */
    public static void setDifficulty(String level) {
        difficulty = level;
    }

    /**
     * This method returns whose turn it is.
     * @param turn
     */
    public static void setTurn(int turn) {
        turnTracker = turn;
    }

    /**
     * This method sets the AI's character to a random one.
     */
    public static void setAiCharacter() {
        aiCharacter = player1Board[(int) (Math.random() * 4)][(int) (Math.random() * 6)];
    }

    // -=-  Auxillery Methods  -=-
    /**
     * This method reads all the character attributes from a text file to create new objects each game instance.
     * @throws IOException
     */
    
    public static void createNewCharacterList() throws IOException {
        characters = new ArrayList<>();

        Scanner fileReader = new Scanner(new File("characterAttributes.txt"));

        while (fileReader.hasNextLine()) {
            String name = fileReader.nextLine();
            boolean isMale = fileReader.nextBoolean();
            fileReader.nextLine();
            String eyeColour = fileReader.nextLine();
            boolean isLightSkin = fileReader.nextBoolean();
            fileReader.nextLine();
            String hairColour = fileReader.nextLine();
            boolean hasFacialHair = fileReader.nextBoolean();
            boolean hasGlasses = fileReader.nextBoolean();
            boolean hasVisibleTeeth = fileReader.nextBoolean();
            boolean hasHat = fileReader.nextBoolean();
            fileReader.nextLine();
            String hairType = fileReader.nextLine();
            boolean hasEarPiercings = fileReader.nextBoolean();
            fileReader.nextLine();
            fileReader.nextLine();

            characters.add(new Person(name, isMale, eyeColour, isLightSkin, hairColour, hasFacialHair, hasGlasses, hasVisibleTeeth, hasHat, hairType, hasEarPiercings));
        }
        
        fileReader.close();
    }

    /**
     * This method resets all players' boards to the default values.
     * @throws IOException 
     */
    public static void resetPlayerBoardsToDefault() throws IOException {
        createNewCharacterList();
        player1Board = new Person[4][6];

        int counter = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                player1Board[row][col] = characters.get(counter);
                counter++;
            }
        }
    }

    /**
     * This method randomly chooses which player goes first.
     */
    public static void chooseRandomFirstTurn() {
        double randNum = Math.random();
        turnTracker = (int) (Math.round(randNum) + 1);
    }

    /**
     * This method resets all values to their default values for a new game instance.
     * @throws IOException 
     */
    public static void newGame() throws IOException {
        resetPlayerBoardsToDefault();
        setAiCharacter();

        System.out.println(GameController.getAiCharacter().getName());

        aiCharacterList = new ArrayList<>();
        for (Person person : characters) {
            aiCharacterList.add(person);
        }

        if ((Question.getQuestionsAsked() != null) && (Question.getQuestionResponses() != null) && (Question.getAiAskedQuestions() != null)) {
            Question.getQuestionsAsked().clear();
            Question.getQuestionResponses().clear();
            Question.getAiAskedQuestions().clear();
        }

        timer = defaultTimer;
    }
  
    public static void updateAiValidCharactersList(boolean response, int questionIndex) {
        ArrayList<Person> removingCharacters = new ArrayList<>();

        switch (questionIndex) {
            case 0: // "Is the person a male?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.isMale()) { // Remove all females.
                            removingCharacters.add(character);
                        } else if (!response && character.isMale()) { // Remove all males.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 1: // "Is the eye colour brown?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getEyeColour().equals("brown")) { // Remove all non brown eyed people.
                            removingCharacters.add(character);
                        } else if (!response && character.getEyeColour().equals("brown")) { // Remove all brown eyed people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 2: // "Is the eye colour green?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getEyeColour().equals("green")) { // Remove all non green eyed people.
                            removingCharacters.add(character);
                        } else if (!response && character.getEyeColour().equals("green")) { // Remove all green eyed people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 3: // "Is the eye colour blue?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getEyeColour().equals("blue")) { // Remove all non blue eyed people.
                            removingCharacters.add(character);
                        } else if (!response && character.getEyeColour().equals("blue")) { // Remove all blue eyed people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 4: // "Does the person have a light skin tone?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.isLightSkin()) { // Remove all dark skin toned people.
                            removingCharacters.add(character);
                        } else if (!response && character.isLightSkin()) { // Remove all light skin toned people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 5: // "Is the hair colour black?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairColour().equals("black")) { // Remove all non black haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairColour().equals("black")) { // Remove all black haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 6: // "Is the hair colour brown?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairColour().equals("brown")) { // Remove all non brown haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairColour().equals("brown")) { // Remove all brown haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 7: // "Is the hair colour ginger?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairColour().equals("ginger")) { // Remove all non ginger haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairColour().equals("ginger")) { // Remove all ginger haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 8: // "Is the hair colour blonde?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairColour().equals("blonde")) { // Remove all non blonde haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairColour().equals("blonde")) { // Remove all blonde haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 9: // "Is the hair colour white?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairColour().equals("white")) { // Remove all non white haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairColour().equals("white")) { // Remove all white haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 10: // "Does the person have facial hair?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.hasFacialHair()) { // Remove all non facial haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.hasFacialHair()) { // Remove all facial haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 11: // "Is the person wearing glasses?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.hasGlasses()) { // Remove all people without glasses
                            removingCharacters.add(character);
                        } else if (!response && character.hasGlasses()) { // Remove all people with glasses.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 12: // "Does the person have visible teeth?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.hasVisibleTeeth()) { // Remove all people without visible teeth.
                            removingCharacters.add(character);
                        } else if (!response && character.hasVisibleTeeth()) { // Remove all people with visible teeth.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 13: // "Is the person wearing a hat?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.hasHat()) { // Remove all people without hats.
                            removingCharacters.add(character);
                        } else if (!response && character.hasHat()) { // Remove all people with hats.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 14: // "Does the person have short hair?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairType().equals("short")) { // Remove all non short haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairType().equals("short")) { // Remove all short haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 15: // "Does the person have their hair tied up?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairType().equals("tied")) { // Remove all non tied hair people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairType().equals("tied")) { // Remove all tied hair people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 16: // "Does the person have long hair?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairType().equals("long")) { // Remove all non long haired people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairType().equals("long")) { // Remove all long haired people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 17: // "Is the person bald?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.getHairType().equals("bald")) { // Remove all non bald people.
                            removingCharacters.add(character);
                        } else if (!response && character.getHairType().equals("bald")) { // Remove all bald people.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
            case 18: // "Does the person have ear piercings?"
                for (int i = 0; i < aiCharacterList.size(); i++) {
                    Person character = aiCharacterList.get(i);

                    if (character.getVisibility()) { // If character is not "hidden".
                        if (response && !character.hasEarPiercings()) { // Remove all people without ear piercings.
                            removingCharacters.add(character);
                        } else if (!response && character.hasEarPiercings()) { // Remove all people with ear piercings.
                            removingCharacters.add(character);
                        }
                    }
                }

                break;
        }

        for (Person person : removingCharacters) {
            aiCharacterList.remove(person);
        }
        
    }

    /**
     * This method writes to a file for history viewing.
     * @param result
     */
    public static void recordGameResult(String result) {
        try {
            File file = new File("game_history.txt");
            ArrayList<String> lines = new ArrayList<>();
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine());
                }
                scanner.close();
            }

            ArrayList<String> updatedLines = new ArrayList<>();
            updatedLines.add(result);
            for (int i = 0; i < lines.size() && i < 4; i++) {//only last 5 match results
                updatedLines.add(lines.get(i));
            }

            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < updatedLines.size(); i++) {
                writer.write(updatedLines.get(i) + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing game history: " + e.getMessage());
        }
    }

    /**
     * This method reads from a file to get the game history.
     * @return
     */
    public static String getGameHistory() {
        String history = "";
        File file = new File("game_history.txt");

        try {
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                int count = 1;

                while (scanner.hasNextLine()) {
                    history += count + ". " + scanner.nextLine() + "\n";
                    count++;
                }
                scanner.close();
            } else {
                history = "No game history found.";
            }
        } catch (IOException e) {
            history = "Error reading history: " + e.getMessage();
        }

        return history;
    }

    /**
     * Returns the current date and time.
     * @return
     */
    public static String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        return formattedDateTime;
    }

    // Music Variables
    private static Clip music;
    private static boolean playingMusic;
    /**
     * This method setups the audio/music of the game.
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public static void audioSetup() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        playingMusic = true;

        File audioFile = new File("music.wav");
        AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);

        music = AudioSystem.getClip();

        music.open(stream);
        music.start();
        music.loop(Clip.LOOP_CONTINUOUSLY);
	}

    /**
     * This method helps with toggling the music (muting/unmuting).
     */
    public static void toggleMusic() {
        if (playingMusic) {
            music.stop();
        } else {
            music.start();
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }

        playingMusic = !playingMusic;
    }

    // Timer Mode Variables
    private static boolean timerMode = false;
    private static final int defaultTimer = 180; // 180 seconds -> 3 minutes
    static int timer;

    /**
     * This method returns "On" or "Off" based on whether the timer mode is enabled or disabled.
     * @return
     */
    public static String getTimerMode() {
        if (timerMode) {
            return "On";
        } else {
            return "Off";
        }
    }

    /**
     * This method toggles the timer mode variable.
     */
    public static void toggleTimerMode() {
        timerMode = !timerMode;
    }
}
