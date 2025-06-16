import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Question {
    private static String[] questionBank = {
        "Is the person a male?", // Index 0
        "Is the eye colour brown?", // Index 1
        "Is the eye colour green", // Index 2
        "Is the eye colour blue?", // Index 3
        "Does the person have a light skin tone?", // Index 4
        "Is the hair colour black?", // Index 5
        "Is the hair colour brown?", // Index 6
        "Is the hair colour ginger?", // Index 7
        "Is the hair colour blonde?", // Index 8
        "Is the hair colour white?", // Index 9
        "Does the person have facial hair?", // Index 10
        "Is the person wearing glasses?", // Index 11
        "Does the person have visible teeth?", // Index 12
        "Is the person wearing a hat?", // Index 13
        "Does the person have short hair?", // Index 14
        "Does the person have their hair tied up?", // Index 15
        "Does the person have long hair?", // Index 16
        "Is the person bald?", // Index 17
        "Does the person have ear piercings?" // Index 18
    };

    private static ArrayList<Integer> questionsAsked; // For simplicity, the questions are mapped to specific integer values -> refer to the "Question.java" file for more info.
    private static ArrayList<Boolean> questionResponses; // true = yes, false = no
    private static Set<Integer> aiAskedQuestions = new HashSet<>();

    // -=-  Getter Methods  -=-
    /**
     * This method returns the question bank.
     * @return String[] questionBank
     */
    public static String[] getQuestionBank() {
        return questionBank;
    }

    /**
     * This method returns the questions asked list.
     * @return ArrayList<Integer> questionsAsked
     */
    public static ArrayList<Integer> getQuestionsAsked() {
        return questionsAsked;
    }

    /**
     * This method returns the question responses list.
     * @return ArrayList<Integer> questionResponses
     */
    public static ArrayList<Boolean> getQuestionResponses() {
        return questionResponses;
    }

    /**
     * This method returns a hashset of all questions the ai has asked in a game instance.
     * @return Set<Integer> aiAskedQuestions.
     */
    public static Set<Integer> getAiAskedQuestions() {
        return aiAskedQuestions;
    }

    
    // -=-  Setter Methods  -=-
    /**
     * This method adds the asked question index to the questionsAsked arraylist.
     * @param questionIndex
     */
    public static void addAskedQuestion(int questionIndex) {
        questionsAsked.add(questionIndex);
    }

    /**
     * This method adds the asked question index to the questionsResponses arraylist.
     * @param response
     */
    public static void addAskedQuestion(boolean response) {
        questionResponses.add(response);
    }


    // -=-  Auxillery Methods  -=-
    /**
     * This method returns true or false based on the question the user asks about the AI's character.
     * @param questionIndex
     * @return boolean
     */
    public static String validateUserQuestion(int questionIndex) {
        boolean response = false;

        switch (questionIndex) {
            case 0: // "Is the person a male?"
                response = GameController.getAiCharacter().isMale() == true;
                break;
            case 1: // "Is the eye colour brown?"
                response = GameController.getAiCharacter().getEyeColour().equals("brown");
                break;
            case 2: // "Is the eye colour green"
                response = GameController.getAiCharacter().getEyeColour().equals("green");
                break;
            case 3: // "Is the eye colour blue?"
                response = GameController.getAiCharacter().getEyeColour().equals("blue");
                break;
            case 4: // "Does the person have a light skin tone?
                response = GameController.getAiCharacter().isLightSkin() == true;
                break;
            case 5: // "Is the hair colour black?"
                response = GameController.getAiCharacter().getHairColour().equals("black");
                break;
            case 6: // "Is the hair colour brown?"
                response = GameController.getAiCharacter().getHairColour().equals("brown");
                break;
            case 7: // "Is the hair colour ginger?"
                response = GameController.getAiCharacter().getHairColour().equals("ginger");
                break;
            case 8: // "Is the hair colour blonde?"
                response = GameController.getAiCharacter().getHairColour().equals("blonde");
                break;
            case 9: // "Is the hair colour white?"
                response = GameController.getAiCharacter().getHairColour().equals("white");
                break;
            case 10: // "Does the person have facial hair?"
                response = GameController.getAiCharacter().hasFacialHair() == true;
                break;
            case 11: // "Does the person have glasses?"
                response = GameController.getAiCharacter().hasGlasses() == true;
                break;
            case 12: // "Does the person have visible teeth?"
                response = GameController.getAiCharacter().hasVisibleTeeth() == true;
                break;
            case 13: // "Is the person wearing a hat?"
                response = GameController.getAiCharacter().hasHat() == true;
                break;
            case 14: // "Does the person have short hair?"
                response = GameController.getAiCharacter().getHairType().equals("short");
                break;
            case 15: // "Does the person have their hair tied up?"
                response = GameController.getAiCharacter().getHairType().equals("tied");
                break;
            case 16: // "Does the person have long hair?"
                response = GameController.getAiCharacter().getHairType().equals("long");
                break;
            case 17: // "Is the person bald?"
                response = GameController.getAiCharacter().getHairType().equals("bald");
                break;
            case 18: // "Does the person have ear piercings?"
                response = GameController.getAiCharacter().hasEarPiercings() == true;
                break;
        }

        if (response) {
            return "Yes";
        } else {
            return "No";
        }
    }

    /**
     * This method returns the question index of the question the AI asked.
     * @return
     */
    public static int getNewAiAskedQuestionIndex() {
        int questionIndex = 0;

        if (GameController.getDifficulty().equals("Normal")) { // AI question choosing logic for normal mode.
            questionIndex = (int) (Math.random() * 19);
            while (aiAskedQuestions.contains(questionIndex)) { // AI question choosing logic for hard mode.
                questionIndex = (int) (Math.random() * 19);
            }
        } else {
           ArrayList<Person> remainingChars = GameController.getAiCharacterList();
        
            // If only one character remains, signal to guess
            if (remainingChars.size() == 1) {
                return -1;
            }

            double smallestDiff = Double.MAX_VALUE;

            // Check all unasked questions
            for (int qIndex = 0; qIndex < questionBank.length; qIndex++) {
                if (!aiAskedQuestions.contains(qIndex)) {
                    int yesCount = 0;
                    
                    // Count how many would answer "yes" to this question
                    for (Person character : remainingChars) {
                        boolean matches = false;
                        switch (qIndex) {
                            case 0: 
                                matches = character.isMale();
                                break;
                            case 1: 
                                matches = character.getEyeColour().equals("brown");
                                break;
                            case 2: 
                                matches = character.getEyeColour().equals("green"); 
                                break;
                            case 3: 
                                matches = character.getEyeColour().equals("blue"); 
                                break;
                            case 4: 
                                matches = character.isLightSkin(); 
                                break;
                            case 5:
                                matches = character.getHairColour().equals("black"); 
                                break;
                            case 6: 
                                matches = character.getHairColour().equals("brown"); 
                                break;
                            case 7: 
                                matches = character.getHairColour().equals("ginger"); 
                                break;
                            case 8: 
                                matches = character.getHairColour().equals("blonde"); 
                                break;
                            case 9: 
                                matches = character.getHairColour().equals("white"); 
                                break;
                            case 10:
                                matches = character.hasFacialHair(); 
                                break;
                            case 11: 
                                matches = character.hasGlasses(); 
                                break;
                            case 12: 
                                matches = character.hasVisibleTeeth(); 
                                break;
                            case 13: 
                                matches = character.hasHat(); 
                                break;
                            case 14: 
                                matches = character.getHairType().equals("short"); 
                                break;
                            case 15: 
                                matches = character.getHairType().equals("tied"); 
                                break;
                            case 16: 
                                matches = character.getHairType().equals("long"); 
                                break;
                            case 17: 
                                matches = character.getHairType().equals("bald"); 
                                break;
                            case 18:
                                matches = character.hasEarPiercings(); 
                                break;
                        }
                        
                        if (matches){
                            yesCount++;
                        }
        
                    }

                    // Calculate how close to 50/50 this split is
                    double ratio = (double) yesCount / remainingChars.size();
                    double diff = Math.abs(ratio - 0.5);

                    // Track best question
                    if (diff < smallestDiff) {
                        smallestDiff = diff;
                        questionIndex = qIndex;
                    }
                }
            }

            aiAskedQuestions.add(questionIndex);

        }

        return questionIndex;
    }

    /**
     * This method returns whether or not the user's guess is correct (if wrong, the game will end right after).
     * @param character
     * @return
     */
    public static String validateUserGuess(String character) {
        if (character.equals(GameController.getAiCharacter().getName())) {
            return "Yes, you win!";
        } else {
            return "No, you lost, and the AI chose \"" + GameController.getAiCharacter().getName() + "\"!";
        }
    }
}