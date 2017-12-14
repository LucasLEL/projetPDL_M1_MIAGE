package model;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * Classe Categorie 
 * @author PDL_GROUPE7
 *
 */
public class Categorie {

	/**
	 * Méthode qui effectue une recherche de catégorie sur la base MongoDB à partir d'un mot + ajout dans la liste des catégories
	 * @param valeurSaisieParUtilisateur
	 * @param categoriesCollection
	 * @param glc
	 */
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
