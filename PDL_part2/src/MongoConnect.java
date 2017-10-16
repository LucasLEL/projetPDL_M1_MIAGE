import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.bson.Document;


/**
 * 
 * @author PDL_GROUPE7
 *
 */
public class MongoConnect {

	public static void main(String[] args) {
		
		Properties prop = new Properties();

		String host = "";
		String port = "";
		String database_name = "";
		String username = "";
		String password = "";
		
		try {
			prop.load(new FileInputStream("config/config.properties"));
			
			host = prop.getProperty("HOST");
			port = prop.getProperty("PORT");
			database_name = prop.getProperty("DATABASE_NAME");
			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} 

		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + database_name));
		
		MongoDatabase database = mongoClient.getDatabase("off");
		
		MongoCollection<Document> collection = database.getCollection("products");
		
		//Find = SELECT en SQL
		//Find 3 documents dans la collection "products", et les renvoie sous forme d'iterateur
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
