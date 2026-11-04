/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipemanagementsystem.recipe.entity.bodies;

import java.util.List;

/**
 *
 * @author oalfuraydi
 */
public record SearchRecipeByTags (List<Integer> includedTags,List<Integer>excludedTags){}
