import java.io.IOException;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

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
		
		// Récupère la collection "products", représentant tous les produits de la base de données OFF
		MongoCollection<Document> collection = databaseOff.getCollection("products");
		
	
		/*
		 * Find en MongoBD = Select en SQL
		 * "Find" 3 documents dans la collection "products", et les renvoie sous forme d'iterateur
		 * Puis parcours l'itérateur contenant les résultats trouvées en JSON et les affiches sur le terminal
		 */
		MongoCursor<Document> cursor = collection.find().limit(3).iterator();
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next().toJson());
		    }
		} finally {
		    cursor.close();
		}
		
		

	}

}
