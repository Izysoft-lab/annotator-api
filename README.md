# Gate-api

**API for extracting concepts from the labels of an ontology contained in a document for semantic indexing**






## Project structure
 The API to the structure of a java project. In the folder **\src\main\java\com\example\gaterestapi** there are 10 classes

- **GaterestapiApplication.java** is the entry point of the application 
- **GateController.java** it is the controller in which all routes are defined
- The other classes as their names indicate are processes that are used in the Gate application
- **Gazetteer.java** This file contains the Gate application; this is where all the pipeline processes and the ontology are loaded. Each process is a static variable that can be called externally.



## Plugins 
The application uses mainly *maven* plugins. All these plugins can be found in the file **pom.xml**. As internal plugin there is the ontology plugin which is in **pluginontology**

## Requirements 
To run the application it is necessary to install:

- **apache-maven** - For dependency management
- **Java** - at least version 11

## Installation and execution

It is necessary to clone the rest, then to decompress this one to place in the root folder and to execute the following commands  

```sh
mvn package
```
To install all the mavens plugins packages

To run the application you have to execute the following command after having executed the command above

```sh
java -jar target/gaterestapi-0.0.1-SNAPSHOT.war
```
This command will launch the tomcat server that will run the application at the address <http://localhost:8080>

To execute the above commands, it is necessary that maven and java be defined as environment variables.
## Mavan plugins

| Plugin | groupId |Version|
| ------ | ------ | ------ |
| spring-boot |  ||
| gate-core | uk.ac.gate.plugins |9.0.1|
| annie | uk.ac.gate.plugins |8.5|
| ontology-tools | uk.ac.gate.plugins|8.5|
| tools | uk.ac.gate.plugins |8.5|
| gazetteer-ontology-based | uk.ac.gate.plugins |8.5|
| sqlite-jdbc              | org.xerial         |3.36.0.3|



# Documentation of API



**Annotation with file**
----
  make a annotation with document and file ontology

* **URL**

  /

* **Method:**

  `POST`
  
*  **BODY**
 
   **Required:**
   *type: multipart formdata*
 
   ``` 
        {
           "myFile":"owl file ontology",
            "document":"text to annoted"
        }
   ```

* **Data Params**

  None

* **Success Response:**

  * **Code:** 201 <br />
    **Content:** 
    ```
      [
            {
              "startNode": "index of startNote",
               "endNode": "index of endNode",
                "label": "label",
                "concept": "concept"
            },
           
            ...
                   
         ]
      ```
 
* **Error Response:**

  * **Code:** 500 INTERNAL ERROR <br />
    **Content:** `{ bat request }`


#### Description of the body ####
 * **myFile** : onotology owl file use for annoation
 * **document** : text of document to annotate

 









**Upload onotology for annotation with ontology id**
----
  For frequent annotation, you must upload the ontology owl file and use it id

* **URL**

  /ontology"

* **Method:**

  `POST`
  
*  **BODY**

   **Required:**
   *type: multipart formdata*
 
   ``` 
        {
           "myFile":"file_to_upload"
        }
   ```

* **Data Params**

  None

* **Success Response:**

  * **Code:** 201 <br />
    **Content:** 
    
    ```
        {
                "id": "id of new file"
        }
      ```
 
* **Error Response:**

  * **Code:** 500 INTERNAL ERROR <br />
    **Content:** `{ bat request }`


**GEt all ontology files**
----
  Get the list of all ontology files uploaded on the server.

* **URL**

  /ontology

* **Method:**

  `GET`
  
*  **BODY**

   **Required:**
 
   None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
     ```
      [   
                {
                    "createAt": "2022-02-17T16:04:56.947Z",
                    "fileName": "activity.owl",
                    "id": "59e370dabbb8da8ff49ca1ff28f5fb279161c72d"
                },
                
                 ....
        ]

    ```

 
* **Error Response:**

  * **Code:** 500 INTERNAL ERROR <br />
    **Content:** `{ bat request }`


**Indexation**
----
  Delete an ontology from id

* **URL**

  /delete_onto

* **Method:**

  `DELETE`
  
*  **BODY**

    None
   
  
 


* **Data Params**

   **Required:** `{
           "id":"file_to_delete"
        }`
     


* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `ok`
 
* **Error Response:**

  * **Code:** 500 INTERNAL ERROR <br />
    **Content:** `{ bat request }`



**Annotation with ontology id**
----
 When you have added an ontology, you can make an annotation by indicating the ontology id and the document to annotate 

* **URL**

  /annotation_ontology

* **Method:**

  `GET`
  
*  **BODY**

  None

  * **Data Params**

   **Required:** 
   ```
        {
          "id": "id of ontology",
          "document":"text doc"
        }
        
  ```



* **Success Response:**

  * **Code:** 201 <br />
    **Content:**  
    ```  
      [
            {
              "startNode": "index of startNote",
               "endNode": "index of endNode",
                "label": "label",
                "concept": "concept"
            },
           
            ...
                   
         ]
    ```
 
* **Error Response:**

  * **Code:** 500 INTERNAL ERROR <br />
    **Content:** `{ bat request }`


#### Description of the params ####
 * **id** : id you ontology file to use for annotation

 * **document** : Text of the document to be annotated 



For more information about this api, you can read our [article](https://duckduckgo.com) about semantic document annotation.




 











