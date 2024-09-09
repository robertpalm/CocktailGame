package com.ridango.game;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cocktail {
   @JsonProperty("strDrink")
    private String name;

   @JsonProperty("strInstructions")
    private String instructions;
}
