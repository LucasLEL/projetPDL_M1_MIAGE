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
 * Cette classe de test effectue le test des fonctionnalit�s de r�cup�ration de donn�es dans l'application
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
		// Connexion � la base MongoDB et r�cup�ration de la base de donn�es OFF
		this.mongo = new MongoConnect();
		//MongoDatabase databaseOff=null;
		try {
			databaseOff = mongo.connectToDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//R�cup�ration des 2 collections mongoDB
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
	 * Methode de test permettant le test de la fonction de recherche de toutes les cat�gories � partir d'un mot cl�.
	 * Cette m�thode va comparer la taille du r�sultat de la requ�te avec celle attendue
	 * Sc�nario de test : simulation de l'entree du mot "milk" dans la barre de recherche. 
	 * Resultat attendu : 121 cat�gories contenant le mot cl� "milk"
	 */
	@Test
	public void testRechercheTailleCategoriesAvecCritere() {
		//On entre le mot "milk" dans la barre de recherche des cat�gorie
		this.ihm.getTxtRecherche().setText("milk");
		
		//On valide la recherche pour lancer la requ�te sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		//On recupere la taille de la liste de resutat obtenue
		int tailleListeObtenue = this.controller.getGlc().getSize();
	
		//Comparaison entre la taille obtenue et la taille attendu
		assertEquals(121,tailleListeObtenue);
	}
	
	
	/**
	 * Methode de test permettant le test de la fonction de recherche de toutes les categories
	 * Cette methode va comparer la taille du resultat de la requete avec celle attendue
	 * Scenario de test : simulation d'un clique sur "Rechercher" lorsque la barre de recherche ne contient aucun critere
	 * Resultat attendu : 16267 categories 
	 */
	@Test
	public void testRechercheTailleCategoriesSansCritere() {
		
		//On valide la recherche pour lancer la requete sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		//On recupere la taille de la liste de resutat obtenue
		int tailleListeObtenue = this.controller.getGlc().getSize();	
	
		//Comparaison entre la taille obtenue et la taille attendu
		assertEquals(16267,tailleListeObtenue);
	}
	
	/**
	 * Methode de test permettant le test de la fonction de recherche de toutes les cat�gories � partir d'un mot cl�.(action repetee deux fois dans ce test)
	 * Celle-ci va permettre de tester si la recherche peut-etre effectuee plusieurs fois (ie:sans devoir relancer l'application)
	 * Cette m�thode va comparer la taille du r�sultat de la requ�te avec celle attendue
	 * Sc�nario de test : simulation de l'entree du mot "milk" dans la barre de recherche, puis de l'entree du mot cle "water"
	 * Resultat attendu : 45 cat�gories contenant le mot cl� "water"
	 */
	@Test
	public void testSuccessifRechercheTailleCategoriesAvecCritere() {
		//On entre le mot "milk" dans la barre de recherche des cat�gorie
		this.ihm.getTxtRecherche().setText("milk");
		
		//On valide la recherche pour lancer la requ�te sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		//On entre le mot "water" dans la barre de recherche des cat�gorie
		this.ihm.getTxtRecherche().setText("water");
				
		//On valide la recherche pour lancer la requ�te sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		//On recupere la taille de la liste de resutat obtenue
		int tailleListeObtenue = this.controller.getGlc().getSize();
	
		//Comparaison entre la taille obtenue et la taille attendu
		assertEquals(45,tailleListeObtenue);
	}
	

	/**
	 * Methode de test permettant le test de la fonction de recherche de toutes les cat�gories � partir d'un mot cl�.
	 * Cette m�thode va comparer la liste du r�sultat de la requ�te avec la liste attendue
	 * Sc�nario de test : simulation de l'entree du mot "milk" dans la barre de recherche. 
	 * Resultat attendu : le contenu de la liste stock�e dans le fichier "test/ressource_test/liste_categories_attendue" de notre application
	 */
	@Test
	public void testRechercheListeCategories() {
		ArrayList<String> listeAttendue = new ArrayList<String>();
		
		//On recupere la liste de cat�gories qui est attendue dans le fichier "test/ressource_test/liste_categories_attendue"
		String listString = readFile("src/test/ressources_test/liste_categories_attendue");
		
		String[] listSplit = listString.split(",");
		
		for(int i=0; i <listSplit.length; i++){
			listeAttendue.add((listSplit[i]).trim());
		}

		//On entre le mot "milk" dans la barre de recherche des cat�gorie
		this.ihm.getTxtRecherche().setText("milk");
		
		//On valide la recherche pour lancer la requ�te sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		ArrayList<String> listeObtenueApresRequete = new ArrayList<String>();
		listeObtenueApresRequete = this.controller.getGlc().getListCategoriesName();

		//On recupere la taille de la liste de resutat obtenue
		int tailleListeObtenue = this.controller.getGlc().getSize();
	
		//Comparaison entre la taille obtenue et la taille attendu
		assertEquals(listeAttendue,listeObtenueApresRequete);
	}
	
	/**
	 * Methode de test permettant le test de la fonction de recherche de toutes les cat�gories � partir d'un mot cl�. (action repetee deux fois dans ce test)
	 * Celle-ci va permettre de tester si la recherche peut-etre effectuee plusieurs fois (ie:sans devoir relancer l'application)
	 * Cette m�thode va comparer la liste du r�sultat de la requ�te avec la liste attendue
	 * Sc�nario de test : simulation de l'entree du mot-cle "milk" dans la barre de recherche., puis de l'entree du mot-cle "water"
	 * Resultat attendu : le contenu de la liste (liste attendue pour le mot-cle "water") stock�e dans le fichier "test/ressource_test/liste_categories_attendue_recherches_successives" de notre application correspondant
	 */
	@Test
	public void testSuccessifRechercheListeCategories() {
		ArrayList<String> listeAttendue = new ArrayList<String>();
		
		//On recupere la liste de cat�gories qui est attendue dans le fichier "test/ressource_test/liste_categories_attendue_recherches_successives"
		String listString = readFile("src/test/ressources_test/liste_categories_attendue_recherches_successives");
		
		String[] listSplit = listString.split(",");
		
		for(int i=0; i <listSplit.length; i++){
			listeAttendue.add((listSplit[i]).trim());
		}
		
		//On entre le mot "milk" dans la barre de recherche des cat�gorie
		this.ihm.getTxtRecherche().setText("milk");
		
		//On valide la recherche pour lancer la premiere requ�te sur la base MongoDB
		this.ihm.getBtRecherche().doClick();
		
		//On entre le mot "water" dans la barre de recherche des cat�gorie
		this.ihm.getTxtRecherche().setText("water");
				
		//On valide la recherche pour lancer la seconde requ�te sur la base MongoDB
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
	 * Cette m�thode est appell�e dans les m�thode de test de cette classe
	 * Dans le but de lire des fichiers test de r�sultats attendus
	 * 
	 * @param pathFile : le chemin du fichier � lire
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