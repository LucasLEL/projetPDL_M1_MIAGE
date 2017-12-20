# projetPDL_M1_MIAGE
This project has been realised during the SDL lesson (Software Development Project) of master 1 MIAGE in Rennes 1 university. 
Contributors : Vivien Busson, Olivier La Rivière, Clément Le Huërou, Vincent Pelletier, Lucas Lelièvre.

This project, realised in Java, is associated to a base MongoDB (see below to connect your database to our project) and to Open Food Facts API. 
The MongoDB base, is consisting of a dump of datas from Open Food Facts and in our case is hosted on an university server. 
During the utilisation of our program, you could generate a.”csv” file, which is composed by a comparison matrix of data, that you could organised in OpenCompare.org

<b>I/ Project structure </b>

Project files are available into “PDL_part2”. It contains two repository : “config” for the connexion to database, “src” for the sources organised into a MVC (Model, View, Controller) architecture and a tests repository (JUnit). There is also a “pom.xml” file,  for the integration of the required libraries (JUnit, Json, Mongo) with Maven. 

<b>II/ Connection to the MongoDB database : </b>

Open sources in an IDE like Eclipse.
Open the file “config/config.properties”. Replace the fields (ie:“your****”, …) by the information of your database to establish the connexion. 
Save the file.

<b>III/ Librairies integration : </b>

Required libraries are automatically included during the project importation with Maven dependencies. 
Into Eclipse, after the project importation, files will be marked with an error, you just need to wait few second (Maven process) and these errors will disappear. 

<b>IV/ Run project : </b>

The purpose of this application is to generate a list of products which can be compared with OpenCompare.org
Firstly, you need to write a word in the first text field, for example “milk” (be careful, the search is mainly released in English, and if you look for a word in another language, it’s possible that your search doesn’t succeed, or with few results). 
After validation, a list of categories associated is proposed. Select the one which represents the best type of products you want to compare. For example “Baby milk”. 
This selection print a product list corresponding to your search. If the products corresponding to your expectation, you could click on “génerer csv” button. Your “.csv” file will be generated. 
Now, you could import this file to OpenCompare.org and use it (see OpenCompare documentation for more informations about this tool). 

<b>--- French / Français ---</b>

Projet réalisé dans le cadre du module de PDL (Projet de Développement Logiciel) du master 1 MIAGE de l'université de Rennes 1. 
Par Vivien Busson, Olivier La Rivière, Clément Le Huërou, Vincent Pelletier, Lucas Lelièvre

Ce projet, réalisé en Java, s'associe à une base MongoDB (voir ci dessous pour associer votre base à notre projet) et à l'API d'Open Food Facts. 
La base MongoDB est constitué d’un dump des données d’Open Food Facts et dans notre cas est hébergé sur un serveur de l’université. 
Au cours de l'utilisation de ce programme, vous pourrez générer un fichier .csv composée d’une matrice de comparaison de données que vous pourrez mettre en forme sous OpenCompare.org.

<b>I/ Structure du projet :  </b>
Les fichiers du projet sont disponibles dans le répertoire “PDL_part2” qui contient lui même deux répertoires : “config” pour la connexion à la base de données”, “src” pour les sources structurées autour d’une architecture de type MVC (Modèle, Vue, Controlleur) et d’un répertoire de tests (JUnit). Il est également consisté d’un fichier “pom.xml” qui permet l’intégration des librairies (junit, json, mongo) avec Maven. 

<b>II/ Connexion à la base MongoDB : </b>

- Après avoir récupéré les sources du projet depuis GitHub, ouvrez les dans un IDE tel que Eclipse. 
- Ouvrez le fichier "config/config.properties". Remplacez les champs de type "your****" par les informations de votre base afin d'établir la connexion.

- Une fois les informations saisies, sauvegardez le fichier. 


<b>III/ Intégration des librairies : </b>

Les librairies sont incluses directement lors de l’importation du projet par l’intermédiaire de dépendances Maven. 
Sous Eclipse, suite à l’importation du projet, les fichiers seront marqués comme comportant une erreur, il suffit simplement d’attendre quelques secondes (le traitement Maven) et ces erreurs disparaitront. 

<b>IV/ Lancer le projet : </b>

- À partir d'un IDE : lancer la classe Main.java depuis le répertoire “src”.



<b>V/ Utilisation : </b>

Le but de l'application est de générer une liste de produits que l'on pourra ensuite comparer à partir d'OpenCompare.org .
Pour cela, il faut écrire un mot dans le champ de texte qui vous est proposé, par exemple "milk" (attention: la recherche s'effectue principalement en anglais, si vous cherchez un mot d'une autre longue, il est probable que vous recherche n'aboutira pas, où contienne très peu de résultats).
Après validation du mot à rechercher, une liste de catégories associée vous est alors proposée, sélectionnez celle qui correspond le mieux au type de produits que vous souhaitez comparer. Par exemple "Baby milk".
Cette sélection vous retourne alors la liste des produits correspondant à votre recherche. Si les produits sont bien ceux souhaités, vous pouvez cliquer sur "générer csv", cela vous générera le fichier .csv associé. 
Vous pourrez ensuite importer ce fichier dans OpenCompare.org et l'utiliser (voir doc OpenCompare pour plus d’informations à ce sujet)
