package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import controller.Controller;
import model.Categorie;
import model.GestionListeCategorie;
import model.GestionListeProduit;
import model.GestionnaireCSV;
import model.GestionnaireCategories;
import model.MongoConnect;
import model.Produit;
import view.Ihm;

/**
 * Classe de test de l'application PDL
 * Cette classe de test effectue le test des fonctionnalités de récupération de données dans l'application
 * 
 * @author PDL_GROUPE7
 */
public class TestApplication {
	
	private MongoDatabase databaseOff;
	private MongoCollection<Document> categoriesCollection;
	private MongoCollection<Document> collectionProduct;
	private MongoConnect mongo;
	
	private GestionnaireCategories gestCategories;
	private GestionListeCategorie glc;
	private GestionListeProduit glp;
	private GestionnaireCSV csvGest;
	private Categorie cat;
	private Produit prod;
	private Controller controller;
	private Ihm ihm;
	
	/**
	 * Constructeur de la classe de test TestApplication
	 */
	public TestApplication(){
	
		this.databaseOff=null;
		// Connexion à la base MongoDB et récupération de la base de données OFF
		this.mongo = new MongoConnect();
		//MongoDatabase databaseOff=null;
		try {
			databaseOff = mongo.connectToDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Récupération des 2 collections mongoDB
		this.categoriesCollection = databaseOff.getCollection("categories");
		this.collectionProduct = databaseOff.getCollection("products");
		
		this.gestCategories = new GestionnaireCategories(categoriesCollection);
		this.glc = new GestionListeCategorie();
		this.glp = new GestionListeProduit();
		this.csvGest = new GestionnaireCSV(",");
		this.cat = new Categorie();
		this.prod = new Produit();
		this.controller = new Controller(categoriesCollection,collectionProduct, glc, glp, prod, cat, csvGest, gestCategories);
		this.ihm = new Ihm(controller);
	}

	/**
	 * Methode de test permettant le test de la fonction de recherche de toutes les catégories à partir d'un mot clé.
	 * Cette méthode va comparer la taille du résultat de la requête avec celle attendue
	 * Scénario de test : simulation de l'entree du mot "milk" dans la barre de recherche. 
	 * Resultat attendu : 121 catégories contenant le mot clé "milk"
	 */
	@Test
	public void testRechercheTailleCategories() {
		//On entre le mot "milk" dans la barre de recherche des catégorie
		this.ihm.getTxtRecherche().setText("milk");
		
		//On valide la recherche pour lancer la requête sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		//On recupere la taille de la liste de resutat obtenue
		int tailleListeObtenue = this.controller.getGlc().getSize();
	
		//Comparaison entre la taille obtenue et la taille attendu
		assertEquals(121,tailleListeObtenue);
	}
	

	/**
	 * Methode de test permettant le test de la fonction de recherche de toutes les catégories à partir d'un mot clé.
	 * Cette méthode va comparer la liste du résultat de la requête avec la liste attendue
	 * Scénario de test : simulation de l'entree du mot "milk" dans la barre de recherche. 
	 * Resultat attendu : le contenu de la liste stockée dans le fichier "test/ressource_test/liste_categories_attendue" de notre application
	 */
	@Test
	public void testRechercheListeCategories() {
		ArrayList<String> listeAttendue = new ArrayList<String>();
		
		//On recupere la liste de catégories qui est attendue dans le fichier "test/ressource_test/liste_categories_attendue"
		String listString = readFile("src/test/ressources_test/liste_categories_attendue");
		
		String[] listSplit = listString.split(",");
		
		for(int i=0; i <listSplit.length; i++){
			listeAttendue.add((listSplit[i]).trim());
		}
		
		//listeObtenueApresRequete = this.controller.getGlc().getListCategoriesName();
		
		//On entre le mot "milk" dans la barre de recherche des catégorie
		this.ihm.getTxtRecherche().setText("milk");
		
		//On valide la recherche pour lancer la requête sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		ArrayList<String> listeObtenueApresRequete = new ArrayList<String>();
		listeObtenueApresRequete = this.controller.getGlc().getListCategoriesName();

		//On recupere la taille de la liste de resutat obtenue
		int tailleListeObtenue = this.controller.getGlc().getSize();
	
		//Comparaison entre la taille obtenue et la taille attendu
		assertEquals(listeAttendue,listeObtenueApresRequete);
	}
	
	/**
	 * Methode permettant de lire le contenu d'un fichier
	 * Cette méthode est appellée dans les méthode de test de cette classe
	 * Dans le but de lire des fichiers test de résultats attendus
	 * 
	 * @param pathFile : le chemin du fichier à lire
	 * @return le contenu du fichier, converti en String
	 */
	private String readFile(String pathFile){
		byte[] encoded=null;
		try {
			encoded = Files.readAllBytes(Paths.get(pathFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded, StandardCharsets.UTF_8);
	}	
}