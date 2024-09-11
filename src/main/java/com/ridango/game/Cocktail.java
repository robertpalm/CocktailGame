package com.ridango.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Cocktail {
   @JsonProperty("strDrink")
    private String name;

   @JsonProperty("strInstructions")
    private String instructions;

   @JsonProperty("strIngredient1")
   private String strIngredient1;

   private boolean firstMistake = true;
   private List<String> ingredients = new ArrayList<>();

   //Add ingredients from API response
    public void addIngredients() {
        if (strIngredient1 != null && !strIngredient1.trim().isEmpty()) {
            ingredients.add(strIngredient1);
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