package model;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Categorie {

	public void rechercherMotCle(String valeurSaisieParUtilisateur, MongoCollection<Document> categoriesCollection, GestionListeCategorie glc) {
		
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("name", new BasicDBObject("$regex", valeurSaisieParUtilisateur));
	    MongoCursor<Document> cursor = categoriesCollection.find(regexQuery).iterator();
		try {
			while (cursor.hasNext()) {
				Document nextElem = cursor.next();
				glc.setListCategories(nextElem.getString("name"), nextElem.getString("key"));
			}
		} finally {
			cursor.close();
		}
	}
}
