
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Produit {

	public void affichageProduit(String valeurCliquerDansListCategories, MongoCollection<Document> collectionProduct,
			GestionListeProduit glp) {
		
		
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("categories_tags", new BasicDBObject("$regex", valeurCliquerDansListCategories));
		MongoCursor<Document> cursor = collectionProduct.find().limit(3).iterator();
		cursor = collectionProduct.find(regexQuery).iterator();
//		System.out.println("\n Exemple de produits dans la collection categories, contenant le mot : "+ " en:"+valeurCliquerDansListCategories);
		glp.cleanListCategorie();
		
		try {
			while (cursor.hasNext()) {
				
				//System.out.println(cursor.next().get("product_name"));
				glp.setListProduit(cursor.next().getString("product_name"));
			}
		} finally {
			cursor.close();
		}
	}
}
