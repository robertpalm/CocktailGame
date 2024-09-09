package com.ridango.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.Scanner;


@Component
public class CocktailGuessGame {

    @Autowired
    private RestTemplate restTemplate;

    public void startGame() {
        //Game initialization
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to 'Guess this cocktail'!");
        System.out.println("What is our contestant's name?");
        String name = scanner.nextLine();

        System.out.println("Welcome " + name + "! Let's play!");

        //Coctail guessing
        boolean keepPlaying = true;

        while (keepPlaying) {
            Cocktail randomCocktail = fetchRandomCocktail();

            if (randomCocktail != null) {
                System.out.println("These are the instructions for the cocktail: " + randomCocktail.getInstructions());
                System.out.println("For testing purpose: " + randomCocktail.getName());
                System.out.println("The cocktail has " + randomCocktail.getName().length() + " letters. What is the cocktail name?");

                while (true) {
                    String guess = scanner.nextLine();
                    if (guess.equalsIgnoreCase("exit game")) {
                        keepPlaying = false;
                        break;
                    } else if (guess.equalsIgnoreCase(randomCocktail.getName())) {
                        System.out.println("You are correct. Good job!");
                        System.out.println("Let's keep going! Also, if you wish to exit, just type 'exit game'.");
                        break;
                    } else {
                        System.out.println("Alas, you did not get it this time. Try again, or type 'exit game' to quit the game.");
                    }
                }
            }

            if (!keepPlaying) {
                break;
            }
        }

        System.out.println("Thanks for playing! See you around, " + name + ".");
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
