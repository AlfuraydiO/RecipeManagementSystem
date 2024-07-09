/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package org.omar.recipes.exceptions;

import org.springframework.http.HttpStatusCode;

/**
 *
 * @author oalfuraydi
 */
public record ErrorMessage(String date,HttpStatusCode httpstatus,String message) {

}
