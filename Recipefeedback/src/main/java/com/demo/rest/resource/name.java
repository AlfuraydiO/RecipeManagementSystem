/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.rest.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.Set;

/**
 *
 * @author oalfuraydi
 */
@Path("names")
public class name {
   
    
@GET
public String getNames() {
      return Set.of("Omar","Sarah").toString();
    }
    
}
