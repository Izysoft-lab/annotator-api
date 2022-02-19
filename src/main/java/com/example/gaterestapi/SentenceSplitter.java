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

public class SentenceSplitter {
	static ProcessingResource sentenceSplitter;
	public static ProcessingResource PR() {
	    try {
	    	//Define regExSentenceSplitter Resource by calling the right plugin
	        sentenceSplitter = (LanguageAnalyser)Factory.createResource("gate.creole.splitter.SentenceSplitter");

	    } catch (Exception e) {
	        Out.prln(e);
	    }    
	    return sentenceSplitter;	    
	}	
}