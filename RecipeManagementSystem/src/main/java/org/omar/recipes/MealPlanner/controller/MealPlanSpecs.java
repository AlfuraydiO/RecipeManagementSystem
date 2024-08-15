
package org.omar.recipes.MealPlanner.controller;

import java.time.LocalDate;
import org.omar.recipes.MealPlanner.entity.MealPlan;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author oalfuraydi
 */
public class MealPlanSpecs {
    
    public static Specification <MealPlan> DaysAhead(int days){
        return (root,query,builder)->{
            LocalDate date=LocalDate.now().plusDays(days);
            return builder.greaterThan(root.get("date"), date);
        };
    }
     
}
