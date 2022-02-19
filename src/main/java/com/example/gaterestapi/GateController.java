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
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.util.GateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.UUID;

@RestController
class GateController {

  // Aggregate root
  // tag::get-aggregate-root[]
  @PostMapping("/")
  List<Hashtable<String, String>> all(@RequestParam("document") String docu, @RequestParam("myFile") MultipartFile ontologyParams) throws GateException, IOException {
        
       
		
       
        File onto = multipartToFile(ontologyParams,ontologyParams.getOriginalFilename());
        System.out.println(onto.toURI());
       
       
        Gazetteer.init(onto.toURI());
      
       
        Document document = Factory.newDocument(docu);
       // Document document = (Document)Factory.createResource("gate.corpora.DocumentImpl", doc_params);
        List<Hashtable<String, String>> concepts = Gazetteer.execute(document);
        Gazetteer.deleteResource();
        Factory.deleteResource(document);
        System.out.println(concepts);
    return concepts;
  }
  
  
  public  static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
    File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
    multipart.transferTo(convFile);
    return convFile;
}
  
  // end::get-aggregate-root[]
 @GetMapping("/")
  String get(){
      String response = "<h3> this API allows to return the concepts of an ontology found in a document <h3>";
      return response; 
  }
  
  @PostMapping("/concepts")
  List<List<String>> concepts(@RequestParam("documents") List<String> docus, @RequestParam("myFile") MultipartFile ontologyParams) throws GateException, IOException {
        
        List<String> documents = (List<String>)docus;
        System.out.println(docus.get(3));
        File onto = multipartToFile(ontologyParams,ontologyParams.getOriginalFilename());
        System.out.println(onto.toURI());
        Gazetteer.init(onto.toURI());
        List<List<String>> concepts = new ArrayList<List<String>>();
      
        for (int i=0; i<documents.size();i++){
            FeatureMap doc_params = Factory.newFeatureMap(); 
            doc_params.put("encoding", "UTF-8");
            doc_params.put("sourceUrl", "file:/F:/text1.txt"); 
            System.out.println(documents.get(i));
            Document document = Factory.newDocument(documents.get(i));
       // Document document = (Document)Factory.createResource("gate.corpora.DocumentImpl", doc_params);
            List<Hashtable<String, String>> conceptes = Gazetteer.execute(document);
            System.out.println(conceptes);
            
       // Document document = (Document)Factory.createResource("gate.corpora.DocumentImpl", doc_params);
            
        }
       
        Gazetteer.deleteResource();
        onto.delete();
    return concepts;
  }
  @PostMapping("/allconcept")
  List<String> getAllConcepts(@RequestParam("myFile") MultipartFile ontologyParams) throws IllegalStateException, GateException, IOException{
    File onto = multipartToFile(ontologyParams,ontologyParams.getOriginalFilename());
    Gazetteer.init(onto.toURI());
    List<String> concepts =   Gazetteer.getAllConcepts();
    Gazetteer.deleteResource();
    onto.delete();
    return concepts;
   
  }
  
  @PostMapping("/allconcept/labels")
  List<Hashtable<String, List<String>>> getAllConcepts_labels(@RequestParam("myFile") MultipartFile ontologyParams) throws IllegalStateException, GateException, IOException{
    File onto = multipartToFile(ontologyParams,ontologyParams.getOriginalFilename());
    Gazetteer.init(onto.toURI());
    List<Hashtable<String, List<String>>> concepts =   Gazetteer.getAllConceptsLabels();
    Gazetteer.deleteResource();
    onto.delete();
    return concepts;
   
  }
  
  @PostMapping("/ontology")
  Hashtable<String, String> uploadOntology(@RequestParam("myFile") MultipartFile ontologyParams) throws IllegalStateException, GateException, IOException{
    File onto = multipartToFileOntology(ontologyParams,ontologyParams.getOriginalFilename());
    Hashtable<String, String> response = new Hashtable<String, String>();
    String id = createSalt();
    String url_file = onto.getAbsolutePath();
    GaterestapiApplication.executeQueryforUpdate("insert into ontology (id_file, url_file,name) values ('"+id+"','"+url_file+"','"+onto.getName()+"')");
    System.out.println(id);
    System.out.println(url_file);
    response.put("id",id);
    return response;
   
  }
  @GetMapping("/ontology")
  List<Hashtable<String, String>> getAllOntologyId(){
     
      ResultSet rs = GaterestapiApplication.executeQueryforRS("select * from ontology");
      List<Hashtable<String, String>> response = new ArrayList<Hashtable<String, String>>();
      
      try {
            while (rs.next()){
                String id_file = rs.getString("id_file");
                String name = rs.getString("name");
                String day = rs.getString("create_at");
                
                Hashtable<String, String> my_dict = new Hashtable<String, String>();
                my_dict.put("id",id_file);
                my_dict.put("fileName",name);
                my_dict.put("createAt",day.toString());
                response.add(my_dict);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
      return response; 
  }
  
  @GetMapping("/annotation_ontology")
  List<Hashtable<String, String>> getAllAnnotationId(@RequestParam String id, @RequestParam("document") String docu) throws GateException{
      System.out.println(id);
      ResultSet rs = GaterestapiApplication.executeQueryforRS("select * from ontology where id_file='"+id+"';");
      String path = "";
      try {
            while (rs.next()){
                System.out.println("on entre ici");
                path = rs.getString("url_file");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
      if(path.equals("")){
          return null;
      }
   	
        System.out.println(docu);
        File onto = new File(path);
        System.out.println(onto.toURI());
        Gazetteer.init(onto.toURI());        
        Document document = Factory.newDocument(docu);
        List<Hashtable<String, String>> concepts = Gazetteer.execute(document);
        Gazetteer.deleteResource();
        Factory.deleteResource(document);
        System.out.println(concepts);
   // return concepts;
      return concepts;
      
      
      
  }
  
   @GetMapping("/delete_onto")
  String deleteontologyId(@RequestParam String id) throws GateException{
      System.out.println(id);
      ResultSet rs = GaterestapiApplication.executeQueryforRS("select * from ontology where id_file='"+id+"';");
      String path = "";
      try {
            while (rs.next()){
                System.out.println("on entre ici");
                path = rs.getString("url_file");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
      if(path.equals("")){
          return null;
      }
   	
        if(path.equals("")){
            return "Id not found";
        }else{
            File onto = new File(path);
            GaterestapiApplication.executeQueryforUpdate(" DELETE FROM ontology WHERE id_file="+"'"+id+"';");
            onto.delete();
            return "Done";
        }
        
       
   // return concepts;
     
      
      
      
  }
  
  public  static File multipartToFileOntology(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
    int value = LocalDateTime.now().getNano();
    
    System.out.println(value);
    //System.out.println(System.getProperty("user.dir")"/ontology/");
    // Paths.get(System.getProperty("user.dir")+"/ontology/"+fileName);
    File convFile = new File(System.getProperty("user.dir")+"/ontology/"+fileName);
    multipart.transferTo(convFile);
    return convFile;
}
  public String createSalt() {
    String ts = String.valueOf(System.currentTimeMillis());
    String rand = UUID.randomUUID().toString();
    return DigestUtils.sha1Hex(ts + rand);
}
  
}
