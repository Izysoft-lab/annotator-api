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
import gate.Annotation;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import java.io.File;
import java.net.MalformedURLException;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.Utils;
import gate.creole.ExecutionException;
import gate.creole.Plugin;
import gate.creole.ResourceInstantiationException;
import gate.creole.ontology.Literal;
import gate.creole.ontology.OClass;
import gate.creole.ontology.Ontology;
import gate.util.GateException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Gazetteer {
	
	public static Ontology ontology;
        public static CorpusController application;
        private static ProcessingResource documentReset;
        private static ProcessingResource annieEnglishTokenizer;
	private static ProcessingResource sentenceSplitter;
	private static ProcessingResource posTagger;
	private static ProcessingResource morpher;
	private static LanguageAnalyser ontoRG;
	private static LanguageAnalyser flexibleGaz = null;
        public static boolean is_first = true;

	// OntoRootGazetteer is a Gazetteer makes lookup request to Ontology, GIST

	@SuppressWarnings("deprecation")
	public static CorpusController init(URI ontologyFilePath) throws ExecutionException, GateException {

		// Instantiate Processing resources variables
                Gate.init();
		
		
		// Set plugin repo
		
		
		try {
			// Loading all the Plugins (ANNIE, Tools, Ontology,
			// Gazetteer_Ontology_Based)
                    Plugin anniePlugin = new Plugin.Maven("uk.ac.gate.plugins", "annie", "9.0");
                    Plugin ontologyTools = new Plugin.Maven("uk.ac.gate.plugins", "ontology-tools", "8.5");
                    Plugin gazetteerOntologyBased = new Plugin.Maven("uk.ac.gate.plugins", "gazetteer-ontology-based", "8.5");
                    Plugin tools = new Plugin.Maven("uk.ac.gate.plugins", "tools", "8.5");
                    Gate.getCreoleRegister().registerPlugin(anniePlugin);
                    Gate.getCreoleRegister().registerPlugin(ontologyTools);
                    Gate.getCreoleRegister().registerPlugin(gazetteerOntologyBased);
                    Gate.getCreoleRegister().registerPlugin(tools);
                    File fil = new File("pluginontology/ontology");
                    System.out.println(fil.toURL());
                    Gate.getCreoleRegister().registerPlugin(new Plugin.Directory(fil.toURL()));
                   
			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (GateException ex) {
			ex.printStackTrace();
		}

		// Create parameter for out ontology
		FeatureMap paramOntology = Factory.newFeatureMap();
		try {
			// Set the ontology parameter with our OWL path 
			paramOntology.put("rdfXmlURL", ontologyFilePath);
			// Create our PR with our previous param
			ontology = (Ontology)Factory.createResource("gate.creole.ontology.impl.sesame.OWLIMOntology",paramOntology);
                        
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            // TODO Auto-generated catch block

		try {
			// Creating default parameters
			
			// Creating Processing Resources for ontoRootGazetteer
			documentReset = DocumentReset.PR();
   
                        FeatureMap paramsToken = Factory.newFeatureMap();
                        paramsToken.put("encoding", "UTF-8"); 
                        
			annieEnglishTokenizer = Tokenizer.PR(paramsToken);
			sentenceSplitter = SentenceSplitter.PR();
			posTagger = POSTagger.PR();
                        
                        FeatureMap paramsMorph = Factory.newFeatureMap();
                        paramsMorph.put("caseSensitive", false); 
			morpher = (ProcessingResource) Factory.createResource(
					"gate.creole.morph.Morph", paramsMorph);
                        
                        ArrayList<ProcessingResource> prs = new ArrayList<ProcessingResource>();
                            prs.add(documentReset);
                            prs.add(annieEnglishTokenizer);
                            prs.add(sentenceSplitter);
                            prs.add(posTagger);
                            prs.add(morpher);
                        
                        application = (CorpusController) Factory.createResource("gate.creole.RealtimeCorpusController");
                        application.setPRs(prs);
                        
                        FeatureMap paramsOntoGaz = Factory.newFeatureMap();
                        paramsOntoGaz.put("caseSensitive", false);
                        paramsOntoGaz.put("ontology", ontology);
                        paramsOntoGaz.put("rootFinderApplication", application);
                        ontoRG = (LanguageAnalyser)Factory.createResource("gate.clone.ql.OntoRootGaz",paramsOntoGaz);
                        ontoRG.init();
			
                        FeatureMap paramsFlexibleGaz = Factory.newFeatureMap();
                        paramsFlexibleGaz.put("gazetteerInst", ontoRG);
                        paramsFlexibleGaz.put("inputFeatureNames", "Token.root");
                        paramsFlexibleGaz.put("outputASName","Lookup");
                        flexibleGaz = (LanguageAnalyser)Factory.createResource("gate.creole.gazetteer.FlexibleGazetteer", paramsFlexibleGaz);
       
                        prs.add(ontoRG);
                        prs.add(flexibleGaz);
                        application.setPRs(prs);
                       
                          

		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
                return application;
	}
        
        public static void deleteResource(){
        Factory.deleteResource(documentReset);
        Factory.deleteResource( annieEnglishTokenizer);
	Factory.deleteResource(sentenceSplitter);
	Factory.deleteResource(posTagger);
	Factory.deleteResource(morpher);
	Factory.deleteResource(ontoRG);
	Factory.deleteResource(flexibleGaz);
        Factory.deleteResource(ontology);
        Factory.deleteResource(application);
        }
        
     public static List<Hashtable<String, String>> execute( Document document) throws ResourceInstantiationException, ExecutionException{
            Corpus corpus = Factory.newCorpus("corpus");
            corpus.add(document);
            application.setCorpus(corpus);
            application.execute();
           
            List<Hashtable<String, String>> datas = new ArrayList<Hashtable<String, String>>();
            List<String> concepts = new ArrayList<String>();
            
            for (Annotation obj : document.getAnnotations().get(new HashSet<String>(Arrays.asList("Lookup")))) {
            String label =  Utils.stringFor(document, obj);
            Set<OClass> tops = ontology.getOClasses(false);
            for(OClass c : tops) {
                if(c.getName().equals(label)){
                     Hashtable<String, String> my_dict = new Hashtable<String, String>();
                     my_dict.put("label", label);
                     my_dict.put("concept",c.getName());
                     my_dict.put("startNode",obj.getStartNode().getOffset().toString());
                     my_dict.put("endNode",obj.getEndNode().getOffset().toString());
                     datas.add(my_dict);
                }else{
                     for (Literal literal : c.getLabels()){
                        if(literal.getValue().equals(label)){
                            Hashtable<String, String> my_dict = new Hashtable<String, String>();
                            my_dict.put("label", label);
                            my_dict.put("concept",c.getName());
                            my_dict.put("startNode",obj.getStartNode().getOffset().toString());
                            my_dict.put("endNode",obj.getEndNode().getOffset().toString());
                            datas.add(my_dict);
                            break;
                        }
                    
                    }
                    
                }
            }
           
        }
            
            System.out.println(concepts);
            deleteResource();
            return datas;
        }
     
     
     public static List<String> getAllConcepts() throws ResourceInstantiationException, ExecutionException{
            List<String> concepts  = new ArrayList<String>();
            Set<OClass> tops = ontology.getOClasses(false);
            for(OClass c : tops) {
             concepts.add(c.getName());
            }
         return concepts;
     }
     
     public static List<Hashtable<String, List<String>>> getAllConceptsLabels() throws ResourceInstantiationException, ExecutionException{
            List<Hashtable<String, List<String>>> datas = new ArrayList<Hashtable<String, List<String>>>();
            
            Set<OClass> tops = ontology.getOClasses(false);
            for(OClass c : tops) {
             List<String> concepts  = new ArrayList<String>();
             concepts.add(c.getName());
             List<String> labels = new ArrayList<String>();
             for (Literal literal : c.getLabels()){
                 labels.add(literal.getValue());
                    
                 }
            Hashtable<String, List<String>> my_dict = new Hashtable<String, List<String>>();
            my_dict.put("label",labels);
            my_dict.put("concept",concepts);
            datas.add(my_dict);
            }
            
         return datas;
     }
 }

    