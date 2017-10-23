import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Categorie {

	public void rechercherMotCle(String valeurSaisieParUtilisateur, MongoCollection<Document> categoriesCollection,
			GestionListeCategorie glc) {
		
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("name", new BasicDBObject("$regex", valeurSaisieParUtilisateur));
		MongoCursor<Document> cursor = categoriesCollection.find().limit(3).iterator();
		cursor = categoriesCollection.find(regexQuery).iterator();
		glc.cleanListCategorie();
		try {
			while (cursor.hasNext()) {
				glc.setListCategories(cursor.next().getString("key"));
			}
		} finally {
			cursor.close();
		}
	}
}
