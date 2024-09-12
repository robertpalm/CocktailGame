package com.ridango.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class CocktailGuessGame {

    @Autowired
    private RestTemplate restTemplate;

    private int highScore = 0;

    public void displayMainMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMain Menu");
            System.out.println("a) New Game");
            System.out.println("b) View High Score");
            System.out.println("c) Exit");
            System.out.println("Choose an option (a/b/c) : ");
            String choice = scanner.nextLine();

            switch (choice.toLowerCase()) {
                case "a":
                    startGame();
                    break;
                case "b":
                    viewHighScore();
                    break;
                case "c":
                    System.out.println("Exiting the game. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose a, b, or c.");
            }
        }
    }

    public void startGame() {
        //Game initialization
        Scanner scanner = new Scanner(System.in);
        GameStatus gameStatus = new GameStatus();

        System.out.println("Welcome to 'Guess this cocktail'!");
        System.out.println("""
        We will provide you with a description and number of letters in the name
        and you need to guess it!
        Also, at anytime you wish to exit, just type 'exit game'.
        """);
        System.out.println("What is our contestant's name?");
        String name = scanner.nextLine();

        System.out.println("Welcome " + name + "! Let's play!");

        //Coctail guessing loop
        boolean keepPlaying = true;

        while (keepPlaying) {
            Cocktail randomCocktail = fetchUniqueCocktail(gameStatus);

            if (randomCocktail != null) {
                gameStatus.markDrinkAsSeen(randomCocktail.getId());
                randomCocktail.addIngredients();
                String hiddenName = randomCocktail.generateHiddenName();

                String instructions = randomCocktail.getInstructions();
                if (instructions == null || instructions.trim().isEmpty()) {
                    System.out.println("It seems there are no special instructions for this cocktail...");
                } else {
                    System.out.println("These are the instructions for the cocktail: " + instructions);
                }

                //For testing display the cocktail name
                System.out.println("For testing purpose: " + randomCocktail.getName());

                System.out.println("The cocktail has " + randomCocktail.getName().replace(" ", "").length() + " letters and " +
                        "consists of " +randomCocktail.getName().split(" ").length + " words." +
                        "What is the cocktail name?");

                boolean hintGiven = false;

                while (gameStatus.getRemainingTries() > 0) {
                    System.out.println("Current guess: " + hiddenName);
                    String guess = scanner.nextLine();
                    if (guess.equalsIgnoreCase("exit game")) {
                        keepPlaying = false;
                        break;
                    } else if (guess.equalsIgnoreCase(randomCocktail.getName())) {
                        int pointsEarned = gameStatus.getRemainingTries();
                        gameStatus.addPoints(pointsEarned);

                        System.out.println("You are correct. Good job!");
                        System.out.println("You earned " + pointsEarned + " points, for a total of: " + gameStatus.getScore());
                        gameStatus.resetTries();
                        randomCocktail.setFirstMistake(false); // reset for next cocktail.
                        System.out.println("Let's keep going!\n");
                        break;
                    } else {
                        gameStatus.decreaseTries();

                        if (gameStatus.getRemainingTries() > 0) {
                            System.out.println("Alas, you did not get it this time. Tries remaining: " +
                                    gameStatus.getRemainingTries() + ". Try again, or type 'exit game' to quit the game.");

                            if (!hintGiven) {
                                String ingredientsHint = String.join(", ", randomCocktail.getIngredients());
                                System.out.println("Here's a hint for you: First letter is '" + randomCocktail.getName().charAt(0) +
                                        "' and the cocktail's ingredients are: " + ingredientsHint);
                                hintGiven = true;
                            }

                            hiddenName = randomCocktail.revealLetters(hiddenName);
                        }

                        if (gameStatus.getRemainingTries() == 0) {
                            System.out.println("You've used up all your tries.");
                            System.out.println("Game over! Your total score is: " + gameStatus.getScore());
                            if (gameStatus.getScore() > highScore) {
                                highScore = gameStatus.getScore();
                                System.out.println("Congratulations! You beat the high score! New high score: " + highScore);
                            }
                            keepPlaying = false;
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Failed to fetch a cocktail. Exiting game.");
                keepPlaying = false;
            }

            if (keepPlaying && gameStatus.getRemainingTries() <= 0) {
                System.out.println("You've used up all your tries.");
                System.out.println("Game over! Your total score is: " + gameStatus.getScore());
                if (gameStatus.getScore() > highScore) {
                    highScore = gameStatus.getScore();
                    System.out.println("Congratulations! You beat the high score! New high score: " + highScore);
                }
                keepPlaying = false;
            }
        }

        System.out.println("Thanks for playing! See you around, " + name + ".");
    }

    private void viewHighScore() {
        System.out.println("The current high score is: " + highScore);
    }

    public Cocktail fetchRandomCocktail() {
        String url = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
        try {
            ResponseEntity<CocktailResponse> response = restTemplate.getForEntity(url, CocktailResponse.class);
            CocktailResponse cocktailResponse = response.getBody();
            if (cocktailResponse != null && !cocktailResponse.getDrinks().isEmpty()) {
                return cocktailResponse.getDrinks().get(0);
            }
        } catch (Exception e) {
            System.err.println("Error occured: " + e.getMessage());
        }

        return null;
    }

    private Cocktail fetchUniqueCocktail(GameStatus gameStatus) {
        Cocktail cocktail;
        int attempt = 0;
        do {
            cocktail = fetchRandomCocktail();
            attempt++;
        } while (cocktail != null && gameStatus.isDrinksSeen(cocktail.getId()) && attempt < 20);

        if (attempt >= 20) {
            return null;
        }

        return cocktail;
    }
}