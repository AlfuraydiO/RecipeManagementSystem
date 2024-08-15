/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipes.config;

import static java.lang.StrictMath.log;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 *
 * @author oalfuraydi
 */
public class CacheEventLogger implements CacheEventListener <Object, Object> {

    private static final Logger LOG = Logger.getLogger(CacheEventLogger.class.getName());

    
    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> ce) {
         LOG.log(Level.INFO, "message:{0} {1} {2}", new Object[]{ce.getKey(), ce.getOldValue(), ce.getNewValue()});
        
    }

     
     
    
}
    

