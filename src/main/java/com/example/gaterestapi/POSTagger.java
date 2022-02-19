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

public class POSTagger{
	static ProcessingResource POSTagger;
	public static ProcessingResource PR() {
	    try {
	    	//Define POSTagger Resource by calling the right plugin
	        POSTagger = (ProcessingResource)Factory.createResource( "gate.creole.POSTagger");
	    } catch (Exception e) {
	        Out.prln(e);
	    }    
	    return POSTagger;	    
	}	
}