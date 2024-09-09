package com.ridango.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CocktailResponse {
    @JsonProperty("drinks")
    private List<Cocktail> drinks;
}
