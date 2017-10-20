import java.io.IOException;
import java.util.ArrayList;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Projections;

/**
 * Classe Main de test, pour se connecter et tester la base de données MongoDB
 * @author PDL_GROUPE7
 *
 */
public class Main {

	public static void main(String[] args) {

		// Connexion à la base MongoDB et récupération de la base de données OFF
		MongoConnect mongo = new MongoConnect();
		MongoDatabase databaseOff=null;
		try {
			databaseOff = mongo.connectToDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/***********************************Création d'une collection de catégories*****************************************/
		
		
		MongoCollection<Document> categoriesCollection = databaseOff.getCollection("categories");
		
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
		
		
		
		/***********************************Affichage de test de 3 catégories random *****************************************/
		
		//test de recherche et d'affichage de 3 categories random dans la collection Categories nouvellement crée
		MongoCursor<Document> cursor = categoriesCollection.find().limit(3).iterator();
		System.out.println("Exemple de 3 catégories dans cette nouvelle collection :");
		try {
		    while (cursor.hasNext()) {
		    	System.out.println(cursor.next().toJson());
		    } 
		}finally {
			    cursor.close();
		}
		
		
		
		/***********************************Affichage de test de 3 produits random****************************************/
	
		
		// Récupère la collection "products", représentant tous les produits de la base de données OFF
		MongoCollection<Document> collectionProduct = databaseOff.getCollection("products");
		cursor = collectionProduct.find().limit(3).iterator();
		System.out.println("Exemple de 3 produits dans la collection product :");
		try {
		    while (cursor.hasNext()) {
		    	System.out.println(cursor.next().toJson());
		    } 
		}finally {
			    cursor.close();
		}
		

	}

}
