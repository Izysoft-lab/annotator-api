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
import gate.creole.ResourceInstantiationException;
import gate.util.Out;


public class DocumentReset{
	static ProcessingResource documentReset;
	public static ProcessingResource PR() {
	        try {
	        	// Calls the Document Reset that cleans up the document from all previous annotations 
		        documentReset = (LanguageAnalyser)Factory.createResource( "gate.creole.annotdelete.AnnotationDeletePR");
	        }
		    catch (ResourceInstantiationException ex) {
		        Out.prln(ex);
		    }
	        return documentReset;
	}
}