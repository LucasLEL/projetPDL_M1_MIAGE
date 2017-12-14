package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

import com.mongodb.client.MongoCollection;

import model.Categorie;
import model.GestionListeCategorie;
import model.GestionListeProduit;
import model.GestionnaireCSV;
import model.GestionnaireCategories;
import model.Produit;

/**
 * Classe Controller qui appelle les principales fonctions de l'application
 * @author PDL_GROUPE7
 *
 */
public class Controller {
	
	private MongoCollection<Document> categoriesCollection;
	private MongoCollection<Document> collectionProduct;
	private GestionListeCategorie glc;
	private GestionListeProduit glp;
	private Produit produit;
	private Categorie categorie;
	private GestionnaireCSV csvGest;
	private GestionnaireCategories gestCategories;

	/**
	 * Constructeur de la classe
	 * @param categoriesCollection
	 * @param collectionProduct
	 * @param glc
	 * @param glp
	 * @param produit
	 * @param categorie
	 * @param csvGest
	 * @param gestCategories
	 */
	public Controller(MongoCollection<Document> categoriesCollection, MongoCollection<Document> collectionProduct,
			GestionListeCategorie glc, GestionListeProduit glp, Produit produit, Categorie categorie, GestionnaireCSV csvGest, GestionnaireCategories gestCategories) {
		this.categoriesCollection = categoriesCollection;
		this.collectionProduct = collectionProduct;
		this.glc = glc;
		this.glp = glp;
		this.produit = produit;
		this.categorie = categorie;
		this.csvGest = csvGest;
		this.gestCategories = gestCategories;
	}
	
	/**
	 * Méthode qui vide la liste catégorie et effectue une recherche à partir du mot en paramètre (recherche)
	 * @param recherche
	 */
	public void actionBoutonRechercher(String recherche ){
		this.glc.cleanListCategorie();
		this.categorie.rechercherMotCle(recherche, this.categoriesCollection, this.glc);
	}
	
	/**
	 * Méthode qui appelle l'affichage de tous les produits de la catégorie "produit" passé en paramètre
	 * @param produit
	 */
	public void actionBoutonValider(String produit){
		this.produit.affichageProduit(produit, this.collectionProduct, this.glp);
		
	}
	/**
	 * Méthode qui génère le fichier .csv avec les informations (Header, data, etc) à partir des différentes listes
	 * @param categorie
	 * @return le nom du fichier créé
	 */
	public String actionBoutonGenererCSV(String categorie){
		csvGest = new GestionnaireCSV(",");
		String categorieTag = this.glc.getTagFromName(categorie);
		this.actionBoutonValider(categorieTag);
		JSONArray arrayOfProducts = this.getInformationsCSV(categorieTag);
		
		ArrayList<String> headersList = new ArrayList<String>();
		headersList.add("product_name");
		headersList.add("id");
		headersList.add("brands");
		headersList.add("image");
		
		HashMap<String, ArrayList<String>> hash1 = this.getListOfProductsForEachIngredient();
		ArrayList<String> arrayOfNutriments = this.getListOfNutriments(); 
		
		float nombreProduitsTotal = arrayOfProducts.length();
		
		for(Map.Entry<String, ArrayList<String>> entry : hash1.entrySet()) {
		    String key = entry.getKey();	 
		    ArrayList<String> values = entry.getValue();
		    float nbProducts = values.size();
		    //Si le nombre de produits contenant un ingredient est supérieur à 3% du nombre total, et à un "id" non vide, on l'ajoute dans le header permettant de former le CSV
		    float pourcentage = (nbProducts/nombreProduitsTotal)*100;
		    if(pourcentage >= 3 && !key.isEmpty()){
		    	headersList.add(key);
		    }
		}
		for(String nutriment : arrayOfNutriments){
			headersList.add(nutriment);
		}
		String nameOfFile="export.csv";
		
		try {
			FileWriter csvFile = csvGest.createCSVFile(nameOfFile);
			csvGest.addHeaders(csvFile, headersList);
			csvGest.addDatas(csvFile, arrayOfProducts, headersList);
			csvGest.closeCSVFile(csvFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nameOfFile;
	}
	
	/**
	 * Méthode qui retourne toutes les informations d'un produit (Ingrédients + nutriments)
	 * @param categorie
	 * @return un json des informations des produits de la catégorie passé en paramètre.
	 */
	public JSONArray getInformationsCSV(String categorie){
		JSONArray jsonArrayOfProducts = null;
		try {
			jsonArrayOfProducts = this.produit.getInformationsProduitsCSV(categorie, this.collectionProduct);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArrayOfProducts;
	}
	
	
	public HashMap<String, ArrayList<String>> getListOfProductsForEachIngredient(){
		return this.produit.getHashIngredientsListProducts();
	}
	
	public ArrayList<String> getListOfNutriments(){
		return this.produit.getArrayNutriments();
	}
	

	public MongoCollection<Document> getCategoriesCollection() {
		return categoriesCollection;
	}

	public void setCategoriesCollection(MongoCollection<Document> categoriesCollection) {
		this.categoriesCollection = categoriesCollection;
	}

	public MongoCollection<Document> getCollectionProduct() {
		return collectionProduct;
	}

	public void setCollectionProduct(MongoCollection<Document> collectionProduct) {
		this.collectionProduct = collectionProduct;
	}

	public GestionListeCategorie getGlc() {
		return glc;
	}

	public void setGlc(GestionListeCategorie glc) {
		this.glc = glc;
	}

	public GestionListeProduit getGlp() {
		return glp;
	}

	public void setGlp(GestionListeProduit glp) {
		this.glp = glp;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
}
