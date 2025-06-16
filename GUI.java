import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GUI extends JFrame implements ActionListener {
    // -=-  Constant Colour Values  -=-
    static private final Color backgroundColour = new Color(25, 25, 30);
    static private final Color buttonColour = new Color(100, 100, 130);
    static private final Color textColour = new Color(230, 230, 250);
    static private final Color shownTileColour = new Color(60, 60, 150);
    static private final Color hiddenTileColour = new Color(150, 70, 60);

    // -=-  Other Constant Values  -=-
    static final int tileSize = 150;

    // -=-  Component Initializations  -=-
    static private JPanel mainPanel = new JPanel();
    static private JPanel boardPanel = new JPanel();
    static private JPanel timerPanel = new JPanel();

    static private JLabel title = new JLabel("Welcome to Guess Who?");
    static private JLabel authors = new JLabel("By: Moxin Guo, Victor Kwong, & Victoria Wong");
    static private JPanel buttonsPanel = new JPanel();

    static private JButton playButton;
    static private JButton mainMenuButton;
    static private JButton humanVsComputerButton;

    static private JButton viewHistoryButton;
    static private JButton toggleMusicButton;
    static private JButton toggleTimerModerButton;


    // -=-  GuiFrame Constructor  -=-
    GUI() {
        // GUI frame settings
        setTitle("Guess Who? (By: Moxin Guo, Victor Kwong, & Victoria Wong)");
        setSize(1280, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel (vertical axis)
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(backgroundColour);
        add(mainPanel);

        // Padding above title
        mainPanel.add(Box.createVerticalStrut(20));

        // Title label
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Monospaced", Font.BOLD, 50));
        title.setForeground(textColour);
        mainPanel.add(title);

        // Authors label
        authors.setAlignmentX(Component.CENTER_ALIGNMENT);
        authors.setFont(new Font("Monospaced", Font.ITALIC, 20));
        authors.setForeground(textColour);
        mainPanel.add(authors);

        // Padding below title
        mainPanel.add(Box.createVerticalStrut(15));

        // Buttons panel (horizontal axis)
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBackground(backgroundColour);
        buttonsPanel.setMaximumSize(new Dimension(1200, 60));
        mainPanel.add(buttonsPanel);

        // Padding between button and timer panel.
        mainPanel.add(Box.createVerticalStrut(5));

        // Timer panel (horizontal axis)
        timerPanel.setLayout(new FlowLayout());
        timerPanel.setBackground(backgroundColour);
        timerPanel.setMaximumSize(new Dimension(300, 30));
        mainPanel.add(timerPanel);

        // Padding below timer panel
        mainPanel.add(Box.createVerticalStrut(15));

        // "Play" Button
        playButton = createButton(buttonsPanel, "Play");

        // "View History" Button
        viewHistoryButton = createButton(buttonsPanel, "View History"); //vic added this 

        // "Toggle Music" Button
        toggleMusicButton = createButton(buttonsPanel, "Toggle Music");

        // "Toggle Timer Mode" Button
        toggleTimerModerButton = createButton(buttonsPanel, "Toggle Timer Mode: " + GameController.getTimerMode());
        toggleTimerModerButton.setActionCommand("Toggle Timer Mode");

        // Adding the board visual panel to the main panel.
        boardPanel.setBackground(backgroundColour);
        mainPanel.add(boardPanel);

        // Setting the visibility of everything to true
        setVisible(true);
    }


    // -=-  ACTION LISTENER  -=-
    /**
     * This method listens to any actions/events that were performed related to the GUI and performs the respective code.
     * 
     * @param event An action that was performed.
     */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        switch (command) {
            case "Play": // Code that executues when the "Play" button is pressed.
                buttonsPanel.removeAll();

                mainMenuButton = createButton(buttonsPanel, "Main Menu");
                humanVsComputerButton = createButton(buttonsPanel, "Human VS Computer");
 
                updatePanel(buttonsPanel);

                break;
                
            case "View History": // Code that executes when the "View History" button is pressed.
                showGameHistory();

                break;
            case "Toggle Music": // Code that executes when the "Toggle Music" button is pressed.
                GameController.toggleMusic();

                break;
            case "Toggle Timer Mode":
                GameController.toggleTimerMode();
                toggleTimerModerButton.setText("Toggle Timer Mode: " + GameController.getTimerMode());
                updatePanel(buttonsPanel);

                break;
            case "Main Menu": // Code that executes when the "Main Menu" button is pressed.
                buttonsPanel.removeAll();

                addButton(playButton);
                addButton(viewHistoryButton);
                addButton(toggleMusicButton);
                addButton(toggleTimerModerButton);
                
                updatePanel(buttonsPanel);

                break;
            case "Human VS Computer": // Code that executes when the "Human VS Computer" button is pressed.
                buttonsPanel.removeAll();

                createButton(buttonsPanel, "Game Modes");
                createButton(buttonsPanel, "Normal Difficulty");
                createButton(buttonsPanel, "Hard Difficulty");

                updatePanel(buttonsPanel);

                break;
            case "Game Modes": // Code that executes when the "Game Modes" button is pressed.
                buttonsPanel.removeAll();

                addButton(mainMenuButton);
                addButton(humanVsComputerButton);

                updatePanel(buttonsPanel);

                break;
            case "Normal Difficulty":
                GameController.setDifficulty("Normal");

                buttonsPanel.removeAll();

                updatePanel(buttonsPanel);

                try {
                    GameController.newGame();
                } catch (Exception e) {}
                GameController.chooseRandomFirstTurn();

                timerSetup();
                turnSetup();
                boardSetup();

                break;
            case "Hard Difficulty":
                GameController.setDifficulty("Hard");

                buttonsPanel.removeAll();

                updatePanel(buttonsPanel);

                try {
                    GameController.newGame();
                } catch (Exception e) {}
                GameController.chooseRandomFirstTurn();

                timerSetup();
                turnSetup();
                boardSetup();

                break;
        }
    }

    // This is a seperate actionlistener specifically for checking when the user clicks on one of the tiles (this catches all tiles/button actions in grid instead of creating individual action checkings for each).
    ActionListener buttonActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JButton clickedTile = (JButton) e.getSource();
            Person person = (Person) clickedTile.getClientProperty("person");

            if (person.getVisibility()) {
                person.setVisibility(false);
                clickedTile.setBackground(hiddenTileColour);
            } else {
                person.setVisibility(true);
                clickedTile.setBackground(shownTileColour);
            }
        }
    };


    // -=-  AUXILIARY GUI METHODS  -=-
    /**
     * This method creates a button for the buttons panel.
     * @param panel, text A JPanel and a String that helps with knowing how to create the button.
     * @return void
     */
    private JButton createButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        
        button.setFont(new Font("Monospaced", Font.BOLD, 25));
        button.setForeground(textColour);
        button.setBackground(buttonColour);
        button.setFocusPainted(false);
        button.addActionListener(this);
        panel.add(button);

        return button;
    }

    /**
     * This method adds a specific button the the buttons panel/
     * @param button A JButton
     */
    private void addButton(JButton button) {
        buttonsPanel.add(button);
    }

    /**
     * This method updates the given panel.
     */
    private void updatePanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    private static Timer timer; // Timer variable
    /**
     * This method sets up the timer.
     */
    private void timerSetup() {
        JLabel timerLabel = new JLabel();
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        timerLabel.setForeground(textColour);

        if (GameController.getTimerMode().equals("On")) {
            timerLabel.setText("Timer: " + GameController.timer + " second(s) left");

            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    GameController.timer--;

                    if (GameController.timer >= 0) {
                        timerLabel.setText("Timer: " + GameController.timer + " second(s) left");
                    } else {
                        timer.stop();
                        timerLabel.setText("Timer ran out, game over!");
                        loseGame(3);
                    }
                }
            });

            timer.start();
        } else {
            timerLabel.setText("Timer: DISABLED");
        }

        timerPanel.add(timerLabel);
    }

    /**
     * This method sets up each turn.
     */
    private void turnSetup() {
        buttonsPanel.removeAll();

        JLabel turnTrackerText = new JLabel("Turn: ");

        turnTrackerText.setFont(new Font("Monospaced", Font.BOLD, 20));
        turnTrackerText.setForeground(textColour);

        if (GameController.getTurn() == 1) {
            turnTrackerText.setText("Turn: User");
            buttonsPanel.add(turnTrackerText);

            buttonsPanel.add(Box.createHorizontalStrut(30)); // Padding between the turn tracker text and the question asking drop down.

            askDropDownSetup();

            buttonsPanel.add(Box.createHorizontalStrut(30)); // Padding between the question asking drop down and the character guessing drop down.

            guessDropDownSetup();
        } else {
            turnTrackerText.setText("Turn: AI");
            buttonsPanel.add(turnTrackerText);

            buttonsPanel.add(Box.createHorizontalStrut(30)); // Padding between the turn tracker text and the user response buttons.

            if (GameController.getAiCharacterList().size() == 0) {
                loseGame(2);
            } else if (GameController.getAiCharacterList().size() == 1) {
                userResponseButtonsSetup(1);
            } else if (GameController.getAiCharacterList().size() == 2 && GameController.getDifficulty().equals("Hard")) {
                userResponseButtonsSetup(2);
            } else {
                userResponseButtonsSetup(3);
            }
        }

        updatePanel(buttonsPanel);
    }

    /**
     * This methods sets up the question asking drop down.
     */
    private void askDropDownSetup() {
        JLabel askText = new JLabel("Ask:");
        askText.setFont(new Font("Monospaced", Font.BOLD, 20));
        askText.setForeground(textColour);

        JComboBox<String> askDropDown = new JComboBox<>(Question.getQuestionBank());
        
        JButton submitAskButton = new JButton("Submit");
        submitAskButton.setFont(new Font("Monospaced", Font.BOLD, 15));
        submitAskButton.setForeground(textColour);
        submitAskButton.setBackground(buttonColour);
        submitAskButton.setFocusPainted(false);

        // Action listener for the question asking drop down.
        submitAskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) askDropDown.getSelectedItem();
                
                JOptionPane.showMessageDialog(buttonsPanel, "You Asked: " + selectedOption + "\nThe AI Says: " + Question.validateUserQuestion((int) askDropDown.getSelectedIndex()));

                GameController.setTurn(2);
                turnSetup();
            }
        });

        buttonsPanel.add(askText);
        buttonsPanel.add(askDropDown);
        buttonsPanel.add(submitAskButton);
    }

    /**
     * This methods sets up the character guessing drop down.
     */
    private void guessDropDownSetup() {
        JLabel guessText = new JLabel("Guess:");
        guessText.setFont(new Font("Monospaced", Font.BOLD, 20));
        guessText.setForeground(textColour);

        String[] characterBank = new String[]{
            "Sam", "Olivia", "Nick", "David", "Sofia", "Liz",
            "Lily", "Leo", "Emma", "Daniel", "Ben", "Katie",
            "Al", "Amy", "Mike", "Gabe", "Farah", "Laura",
            "Jordan", "Eric", "Carmen", "Rachel", "Joe", "Mia"
        };
        JComboBox<String> guessDropDown = new JComboBox<>(characterBank);
        
        JButton submitGuessButton = new JButton("Submit");
        submitGuessButton.setFont(new Font("Monospaced", Font.BOLD, 15));
        submitGuessButton.setForeground(textColour);
        submitGuessButton.setBackground(buttonColour);
        submitGuessButton.setFocusPainted(false);

        // Action listener for the character guessing drop down (submit button).
        submitGuessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) guessDropDown.getSelectedItem();
                JOptionPane.showMessageDialog(buttonsPanel, "You Guessed: " + selectedOption + "\nThe AI Says: " + Question.validateUserGuess(guessDropDown.getSelectedItem().toString()));

                if (Question.validateUserGuess(guessDropDown.getSelectedItem().toString()).equals("Yes, you win!")) {
                    GameController.recordGameResult("[" + GameController.getDateTime() + "] {" + GameController.getDifficulty() + " Difficulty} (Timer: " + GameController.getTimerMode() + ")  →  " + "You guessed \"" + selectedOption + "\" and won!");
                } else {
                    GameController.recordGameResult("[" + GameController.getDateTime() + "] {" + GameController.getDifficulty() + " Difficulty} (Timer: " + GameController.getTimerMode() + ")  →  " + "You guessed \"" + selectedOption + "\" and lost.");
                }

                boardPanel.removeAll();
                buttonsPanel.removeAll();
                timerPanel.removeAll();
                try {
                    timer.stop();
                } catch (Exception err) {}

                updatePanel(mainPanel);

                addButton(playButton);
                addButton(viewHistoryButton);
                addButton(toggleMusicButton);
                addButton(toggleTimerModerButton);
            }
        });

        buttonsPanel.add(guessText);
        buttonsPanel.add(guessDropDown);
        buttonsPanel.add(submitGuessButton);
    }

    /**
     * This method sets up the user response UI.
     */
    private void userResponseButtonsSetup(int option) {
        if (option == 1) { // Choose the only remaining character.
            JLabel aiQuestionText = new JLabel("\"Is your character " + GameController.getAiCharacterList().get(0).getName() + "?\":");
            aiQuestionText.setFont(new Font("Monospaced", Font.BOLD, 20));
            aiQuestionText.setForeground(textColour);

            JButton yesButton = new JButton("Yes");
            yesButton.setFont(new Font("Monospaced", Font.BOLD, 15));
            yesButton.setForeground(textColour);
            yesButton.setBackground(buttonColour);
            yesButton.setFocusPainted(false);

            JButton noButton = new JButton("No");
            noButton.setFont(new Font("Monospaced", Font.BOLD, 15));
            noButton.setForeground(textColour);
            noButton.setBackground(buttonColour);
            noButton.setFocusPainted(false);

            // Action listener for the yes button.
            yesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loseGame(1);
                }
            });

            // Action listener for the no button.
            noButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loseGame(2);
                }
            });

            buttonsPanel.add(aiQuestionText);
            buttonsPanel.add(yesButton);
            buttonsPanel.add(noButton);
        } else if (option == 2) { // Choose a character out of the 2 (always the character on index 0, since we don't 100% know, so it doesn't matter if we randomize the guess or not).
            JLabel aiQuestionText = new JLabel("\"Is your character " + GameController.getAiCharacterList().get(0).getName() + "?\":");
            aiQuestionText.setFont(new Font("Monospaced", Font.BOLD, 20));
            aiQuestionText.setForeground(textColour);

            JButton yesButton = new JButton("Yes");
            yesButton.setFont(new Font("Monospaced", Font.BOLD, 15));
            yesButton.setForeground(textColour);
            yesButton.setBackground(buttonColour);
            yesButton.setFocusPainted(false);

            JButton noButton = new JButton("No");
            noButton.setFont(new Font("Monospaced", Font.BOLD, 15));
            noButton.setForeground(textColour);
            noButton.setBackground(buttonColour);
            noButton.setFocusPainted(false);

            // Action listener for the yes button.
            yesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loseGame(1);
                }
            });

            // Action listener for the no button.
            noButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(buttonsPanel, "The AI guessed \"" + GameController.getAiCharacterList().get(0).getName() + "\", and you won!");
                    GameController.recordGameResult("[" + GameController.getDateTime() + "] {" + GameController.getDifficulty() + " Difficulty} (Timer: " + GameController.getTimerMode() + ")  →  " + "The AI guessed \"" + GameController.getAiCharacterList().get(0).getName() + "\", and you won!");
                
                    boardPanel.removeAll();
                    buttonsPanel.removeAll();
                    timerPanel.removeAll();
                    try {
                        timer.stop();
                    } catch (Exception err) {}

                    updatePanel(mainPanel);

                    addButton(playButton);
                    addButton(viewHistoryButton);
                    addButton(toggleMusicButton);
                    addButton(toggleTimerModerButton);
                }
            });

            buttonsPanel.add(aiQuestionText);
            buttonsPanel.add(yesButton);
            buttonsPanel.add(noButton);
        } else {
            if (Question.getAiAskedQuestions().size() == 18) {
                loseGame(2);
            } else {
                int aiQuestionIndex = Question.getNewAiAskedQuestionIndex();
                String aiQuestion = Question.getQuestionBank()[aiQuestionIndex]; // Gets the actual question string based on the question index.
                JLabel aiQuestionText = new JLabel("\"" + aiQuestion + "\":");
                aiQuestionText.setFont(new Font("Monospaced", Font.BOLD, 20));
                aiQuestionText.setForeground(textColour);

                JButton yesButton = new JButton("Yes");
                yesButton.setFont(new Font("Monospaced", Font.BOLD, 15));
                yesButton.setForeground(textColour);
                yesButton.setBackground(buttonColour);
                yesButton.setFocusPainted(false);

                JButton noButton = new JButton("No");
                noButton.setFont(new Font("Monospaced", Font.BOLD, 15));
                noButton.setForeground(textColour);
                noButton.setBackground(buttonColour);
                noButton.setFocusPainted(false);

                // Action listener for the yes button.
                yesButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        GameController.updateAiValidCharactersList(true, aiQuestionIndex);

                        GameController.setTurn(1);
                        turnSetup();
                    }
                });

                // Action listener for the no button.
                noButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        GameController.updateAiValidCharactersList(false, aiQuestionIndex);

                        GameController.setTurn(1);
                        turnSetup();
                    }
                });

                buttonsPanel.add(aiQuestionText);
                buttonsPanel.add(yesButton);
                buttonsPanel.add(noButton);
            }
        }
    }

    /**
     * This method displays how the user lost the game.
     * @param option
     */
    private void loseGame(int option) {
        if (option == 1) { // User loses, and AI wins.
            JOptionPane.showMessageDialog(buttonsPanel, "You lose, the AI chose \"" + GameController.getAiCharacter().getName() + "\" and won!");

            GameController.recordGameResult("[" + GameController.getDateTime() + "] {" + GameController.getDifficulty() + " Difficulty} (Timer: " + GameController.getTimerMode() + ")  →  " + "The AI guessed your character correctly, and you lost!");

            boardPanel.removeAll();
            buttonsPanel.removeAll();
            timerPanel.removeAll();
            try {
                timer.stop();
            } catch (Exception err) {}

            addButton(playButton);
            addButton(viewHistoryButton);
            addButton(toggleMusicButton);
            addButton(toggleTimerModerButton);

            updatePanel(mainPanel);
        } else if (option == 2) { // User loses, and user lied.
            JOptionPane.showMessageDialog(buttonsPanel, "You lose, you lied somewhere, and the AI chose \"" + GameController.getAiCharacter().getName() + "\"!");

            GameController.recordGameResult("[" + GameController.getDateTime() + "] {" + GameController.getDifficulty() + " Difficulty} (Timer: " + GameController.getTimerMode() + ")  →  " + "The AI didn't guess your character, but you lied!");

            boardPanel.removeAll();
            buttonsPanel.removeAll();
            timerPanel.removeAll();
            try {
                timer.stop();
            } catch (Exception err) {}

            addButton(playButton);
            addButton(viewHistoryButton);
            addButton(toggleMusicButton);
            addButton(toggleTimerModerButton);

            updatePanel(mainPanel);
        } else if (option == 3) { // Timer ran out, no one won.
            JOptionPane.showMessageDialog(buttonsPanel, "The timer ran out, no one won, and the air chose \"" + GameController.getAiCharacter().getName() + "\"!");

            GameController.recordGameResult("[" + GameController.getDateTime() + "] {" + GameController.getDifficulty() + " Difficulty} (Timer: " + GameController.getTimerMode() + ")  →  " + "The timer ran out, no one won!");

            boardPanel.removeAll();
            buttonsPanel.removeAll();
            timerPanel.removeAll();
            try {
                timer.stop();
            } catch (Exception err) {}

            addButton(playButton);
            addButton(viewHistoryButton);
            addButton(toggleMusicButton);
            addButton(toggleTimerModerButton);

            updatePanel(mainPanel);
        }
    }

    /**
     * This method will set up all the settings and visuals of the player's game board.
     * 
     * @return void
     */
    private void boardSetup() {
        boardPanel.removeAll();

        boardPanel.setLayout(new GridLayout(4, 6, 5, 5));
        boardPanel.setMaximumSize(new Dimension(6 * tileSize, 4 * tileSize));

        for (Person[] row : GameController.player1Board) {
            for (Person col : row) {
                JButton tile = new JButton();

                ImageIcon ogIcon = new ImageIcon(col.getImgPath());
                Image scaledImage = ogIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                tile.setIcon(scaledIcon);

                tile.setFont(new Font("Monospaced", Font.BOLD, 20));
                tile.setForeground(textColour);
                tile.setText(col.getName());

                if (col.getVisibility()) {
                    tile.setBackground(shownTileColour);
                } else {
                    tile.setBackground(hiddenTileColour);
                }

                tile.setHorizontalAlignment(SwingConstants.CENTER);
                tile.setVerticalAlignment(SwingConstants.CENTER);
                tile.setHorizontalTextPosition(SwingConstants.CENTER);
                tile.setVerticalTextPosition(SwingConstants.BOTTOM);

                tile.putClientProperty("person", col);
                tile.addActionListener(buttonActionListener);

                boardPanel.add(tile);
            }
        }
        
        
        updatePanel(boardPanel);
    }

    /**
     * This method shows the game history.
     */
    private void showGameHistory() {
        String history = GameController.getGameHistory(); 
        if (history == null || history.isEmpty()) {
            history = "No game history available.";
        }
        JOptionPane.showMessageDialog(this, history, "Game History", JOptionPane.INFORMATION_MESSAGE);
}

}
