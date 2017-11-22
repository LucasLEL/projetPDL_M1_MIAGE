import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.UnsupportedLookAndFeelException;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Classe Main de test, pour se connecter et tester la base de données MongoDB
 * @author PDL_GROUPE7
 *
 */
public class Main {
	
	private static MongoDatabase databaseOff;
	private static MongoCollection<Document> categoriesCollection;
	private static MongoCollection<Document> collectionProduct;
	
	//Creation de l'objet GestionListeCategorie
	static GestionListeCategorie glc = new GestionListeCategorie();
	//Creation de l'objet GestionListeProduit
	static GestionListeProduit glp = new GestionListeProduit();

	//Creation de l'objet IhmTest.
	static IhmTest ihm = new IhmTest();
	//Creation de l'objet Categorie.
	static Categorie cat = new Categorie();
	//Creation de l'objet Produit.
	static Produit prod = new Produit();

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		databaseOff=null;
		// Connexion à la base MongoDB et récupération de la base de données OFF
		MongoConnect mongo = new MongoConnect();
		//MongoDatabase databaseOff=null;
		try {
			databaseOff = mongo.connectToDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Récupération des 2 collections mongoDB
		categoriesCollection = databaseOff.getCollection("categories");
		collectionProduct = databaseOff.getCollection("products");
		

		//Creation Fenetre.
		IhmTest.recuperationData();
		
		
		/***********************************Création d'une collection de catégories*****************************************/
		//Pas appellé a chaque fois, juste quand l'utilisateur veut mettre à jour la collection de categories
		//createCategoriesCollection();
		
		/***********************************Affichage de test de 3 catégories random *****************************************/
		
		//test de recherche et d'affichage de 3 categories random dans la collection Categories nouvellement crée
//		MongoCursor<Document> cursor = categoriesCollection.find().limit(3).iterator();
//		System.out.println("\n Exemple de 3 catégories dans cette nouvelle collection :");
//		try {
//		    while (cursor.hasNext()) {
//		    	System.out.println(cursor.next().toJson());
//		    } 
//		}finally {
//			    cursor.close();
//		}
		
		/***********************************Affichage de test de 3 produits random****************************************/

//		cursor = collectionProduct.find().limit(3).iterator();
//		System.out.println("\n Exemple de 3 produits dans la collection product :");
//		try {
//		    while (cursor.hasNext()) {
//		    	System.out.println(cursor.next().toJson());
//		    } 
//		}finally {
//			    cursor.close();
//		}
		
		//A partir de ces résultats de categories, l'utilisateur clique sur "Corsican beers"
		//Ci-dessous l'affichage des produits de cette selection
		
			
	}
	
	public static void actionBouton(){
		//Categorie
		cat.rechercherMotCle(ihm.getRecherche(), categoriesCollection, glc);
	}
	
	public static void actionBoutonValider(String produit){
		//Produits
		//String realNameCat = glc.getNomAvantSub();
		
		//prod.affichageProduit(realNameCat, collectionProduct, glp);
		prod.affichageProduit(produit, collectionProduct, glp);
		
	}
	
	public static JSONArray getInformationsCSV(String categorie){
		JSONArray jsonArrayOfProducts = null;
		try {
			jsonArrayOfProducts = prod.getInformationsProduitsCSV(categorie, collectionProduct);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArrayOfProducts;
	}
	
	
	public static HashMap<String, ArrayList<String>> getListOfProductsForEachIngredient(){
		return prod.getHashIngredientsListProducts();
	}
	
	public static ArrayList<String> getListOfNutriments(){
		return prod.getArrayNutriments();
	}
	
	public static void createCategoriesCollection(){
		
		GestionnaireCategories gestCategories = new GestionnaireCategories(categoriesCollection);
		String jsonOfAllCategories=null;
		try {
			//On supprime l'ancienne collection de Categories si elle exsitait
			gestCategories.dropCollection();
			
			//On fait une requête HTTP sur l'API OpenFoodFact pour récupérer toutes les catégories en json
			jsonOfAllCategories = gestCategories.getJsonCategories();
			
			//On Créer un arrayList de Catégories grâce au json passé en paramètre
			ArrayList<Document> arrayOfCategories = gestCategories.getArrayListOfCategories(jsonOfAllCategories);
			
			//On insert cette arrayList dans la collection de Categories sur MongoDB
			gestCategories.insertCategoriesInDataBase(arrayOfCategories);
			System.out.println("Collection comportant toutes les categories crée !");
			
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

}
