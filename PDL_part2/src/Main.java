import java.io.IOException;
import java.util.ArrayList;

import javax.swing.UnsupportedLookAndFeelException;

import org.bson.Document;
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
	//Creation de l'objet IhmTest
	static IhmTest ihm = new IhmTest();
	//Creation de l'objet Categorie
	static Categorie cat = new Categorie();

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
		
//		String valeurCliquerDansListCategories = "en:powdered-almond-milks";
//		
//		BasicDBObject regexQuery1 = new BasicDBObject();
//		//regexQuery1.put("categories_tags", valeurCliquerDansListCategories);
//		regexQuery1.put("categories_tags", valeurCliquerDansListCategories);
//		
//		cursor = collectionProduct.find(regexQuery1).iterator();
//		System.out.println("\n Exemple de tous les produits de la categorie : " +valeurCliquerDansListCategories);
//		try {
//		    while (cursor.hasNext()) {
//		    	System.out.println(cursor.next().toJson());
//		    } 
//		}finally {
//			    cursor.close();
//		}
			
	}
	
	public static void actionBouton(){
		//Categorie
		cat.rechercherMotCle(ihm.getRecherche(), categoriesCollection, glc);
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
			System.out.println("Collection comportant toutes les categories créer !");
			
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

}
