package com.ridango.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;


@Component
public class CocktailGuessGame {

    @Autowired
    private RestTemplate restTemplate;

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
            Cocktail randomCocktail = fetchRandomCocktail();

            if (randomCocktail != null) {
                System.out.println("These are the instructions for the cocktail: " + randomCocktail.getInstructions());
                System.out.println("For testing purpose: " + randomCocktail.getName());
                System.out.println("The cocktail has " + randomCocktail.getName().length() + " letters. What is the cocktail name?");

                while (gameStatus.getRemainingTries() > 0) {
                    String guess = scanner.nextLine();
                    if (guess.equalsIgnoreCase("exit game")) {
                        keepPlaying = false;
                        break;
                    } else if (guess.equalsIgnoreCase(randomCocktail.getName())) {
                        System.out.println("You are correct. Good job!");
                        System.out.println("You earned " + gameStatus.getRemainingTries() + " points");
                        gameStatus.addPoints(gameStatus.getRemainingTries());
                        gameStatus.resetTries();
                        System.out.println("Let's keep going!\n");
                        break;
                    } else {
                        gameStatus.decreaseTries();
                        if (gameStatus.isGameOver()) {
                            System.out.println("You've used up all your tries.");
                            keepPlaying = false;
                            break;
                        } else {
                            System.out.println("Alas, you did not get it this time. Tries remaining: " + gameStatus.getRemainingTries() + ". Try again, or type 'exit game' to quit the game.");
                        }
                    }
                }
            }

            if (!keepPlaying) {
                System.out.println("Game over! Your total score is: " + gameStatus.getScore());
                break;
            }
        }

        System.out.println("Thanks for playing! See you around, " + name + ".");
        scanner.close();
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
}
