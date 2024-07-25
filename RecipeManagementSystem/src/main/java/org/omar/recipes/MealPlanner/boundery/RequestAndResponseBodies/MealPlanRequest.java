package org.omar.recipes.MealPlanner.boundery.RequestAndResponseBodies;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import org.omar.recipes.MealPlanner.entity.enums.IntermittentfastingTypes;

public record MealPlanRequest(List<Integer> included,
    List<Integer> excluded,
    IntermittentfastingTypes intermittentfastingType,
    @FutureOrPresent(message = "Plan must be set in the Future Or Present") LocalDate startingDate,
    @Min(value = 1, message = "Plan Should be at least one week long.")
    @Max(value = 4, message = "Plan Should be at max one week month.") int numberOfweeks) {

}
