/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package org.omar.recipes.rating.boundary;

/**
 *
 * @author oalfuraydi
 */
public record RatingRequest(long recipeId,double rating,String review) {}
