import java.io.IOException;

import org.bson.Document;

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
 * Classe Main de test, pour se connecter et tester la base de données MongoDB
 * @author PDL_GROUPE7
 *
 */
public class Main {

	public static void main(String[] args) {
		
		MongoDatabase databaseOff;
		MongoCollection<Document> categoriesCollection;
		MongoCollection<Document> collectionProduct;
	
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
		
		GestionnaireCategories gestCategories = new GestionnaireCategories(categoriesCollection);
		GestionListeCategorie glc = new GestionListeCategorie();
		GestionListeProduit glp = new GestionListeProduit();
		GestionnaireCSV csvGest = new GestionnaireCSV(",");
		Categorie cat = new Categorie();
		Produit prod = new Produit();
		Controller controller = new Controller(categoriesCollection,collectionProduct, glc, glp, prod, cat, csvGest, gestCategories);
		Ihm ihm = new Ihm(controller);
	}
}
