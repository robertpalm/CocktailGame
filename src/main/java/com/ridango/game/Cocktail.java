package com.ridango.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Cocktail {
    @JsonProperty("idDrink")
    private String id;

   @JsonProperty("strDrink")
    private String name;

   @JsonProperty("strInstructions")
    private String instructions;

   @JsonProperty("strIngredient1")
   private String strIngredient1;

   @JsonProperty("strIngredient2")
   private String strIngredient2;

   @JsonProperty("strIngredient3")
   private String strIngredient3;

   @JsonProperty("strIngredient4")
   private String strIngredient4;

   @JsonProperty("strIngredient5")
   private String strIngredient5;

   @JsonProperty("strIngredient6")
   private String strIngredient6;

   @JsonProperty("strIngredient7")
   private String strIngredient7;

   @JsonProperty("strIngredient8")
   private String strIngredient8;

   @JsonProperty("strIngredient9")
   private String strIngredient9;

   @JsonProperty("strIngredient10")
   private String strIngredient10;

   @JsonProperty("strIngredient11")
   private String strIngredient11;

   @JsonProperty("strIngredient12")
   private String strIngredient12;

   @JsonProperty("strIngredient13")
   private String strIngredient13;

   @JsonProperty("strIngredient14")
   private String strIngredient14;

   @JsonProperty("strIngredient15")
   private String strIngredient15;

   private boolean firstMistake = true;
   private List<String> ingredients = new ArrayList<>();

   //Add ingredients from API response
    public void addIngredients() {
        List<String> ingredientFields = Arrays.asList(
                strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
                strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
                strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15
        );

        for (String ingredient : ingredientFields) {
            if (ingredient != null && !ingredient.trim().isEmpty()) {
                ingredients.add(ingredient);
            }
        }
    }

   //Generate the hidden name
    public String generateHiddenName() {
        StringBuilder hiddenName = new StringBuilder();
        for (char ch : name.toCharArray()) {
            if (ch == ' ') {
                hiddenName.append("  ");
            } else {
                hiddenName.append("_ ");
            }
        }
        return hiddenName.toString().trim();
    }

    //Reveal letters
    public String revealLetters(String hiddenName) {
        char[] hiddenArray = hiddenName.replaceAll("  ", " ").replaceAll(" ", "").toCharArray();
        char[] nameArray = name.replaceAll(" ", "").toCharArray();

        if(firstMistake) {
            hiddenArray[0] = nameArray[0];
            firstMistake = false;
        } else {
            Random random = new Random();
            List<Integer> unrevealedPositions = new ArrayList<>();

            for (int i = 0; i < hiddenArray.length; i++) {
                if (hiddenArray[i] == '_') {
                    unrevealedPositions.add(i);
                }
            }

            if (!unrevealedPositions.isEmpty()) {
                int pos = unrevealedPositions.get(random.nextInt(unrevealedPositions.size()));
                hiddenArray[pos] = nameArray[pos];
            }
        }

        //Hidden name with spaces
        StringBuilder updatedHiddenName = new StringBuilder();
        int index = 0;
        for (char ch : name.toCharArray()) {
            if (ch == ' ') {
                updatedHiddenName.append("   ");
            } else {
                updatedHiddenName.append(hiddenArray[index++]).append(" ");
            }
        }
        return updatedHiddenName.toString().trim();
    }
}