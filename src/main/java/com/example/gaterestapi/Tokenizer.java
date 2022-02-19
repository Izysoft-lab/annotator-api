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
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.util.Out;


public class Tokenizer{
	static ProcessingResource tokenizer;
	public static ProcessingResource PR(FeatureMap features) {
	    try {
	    	// Tokeniser cuts out the input text into tokens/entities
	    	tokenizer = (LanguageAnalyser)Factory.createResource( "gate.creole.tokeniser.DefaultTokeniser",features);

	    } catch (Exception e) {
	        Out.prln(e);
	    }    
	    return tokenizer;	    
	}
}