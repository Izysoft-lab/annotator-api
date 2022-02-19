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
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.Plugin;
import gate.util.GateException;
import gate.util.Out;
import java.net.MalformedURLException;
import java.net.URL;

public class Morpher {
	static ProcessingResource morpher;
	public static ProcessingResource PR(FeatureMap features) {
	    try {
			// Set plugin repo
		try {
				// Loading all the Plugins (ANNIE, Tools, Ontologie		// Gazetteer_Ontology_Based)
                   
                    Plugin tools = new Plugin.Maven("uk.ac.gate.plugins", "tools", "8.5");
                   
                    Gate.getCreoleRegister().registerPlugin(tools);
                    
		} 
                catch (Exception ex) {
			ex.printStackTrace();
		}
	    	//Define regExSentenceSplitter Resource by calling the right plugin
	        morpher = (ProcessingResource)Factory.createResource("gate.creole.morph.Morph",features);
	    } catch (Exception e) {
	        Out.prln(e);
	    }    
	    return morpher;	    
	}	
}
