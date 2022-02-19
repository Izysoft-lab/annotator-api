/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gaterestapi;

/**
 *
 * @author paul
 */

import gate.Factory;
import gate.FeatureMap;
import gate.ProcessingResource;
import gate.util.Out;

public class DefaultGazetteer {
	static ProcessingResource AnnieGaz;
	public static ProcessingResource PR(FeatureMap features) {
	    try {
	    	AnnieGaz = (ProcessingResource)
	    			Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", features);
                
	    } catch (Exception e) {
	        Out.prln(e);
	    }    
	    return AnnieGaz;	    
	}
}