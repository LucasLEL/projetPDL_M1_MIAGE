
import java.util.HashMap;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Produit {

	public void affichageProduit(String valeurCliquerDansListCategories, MongoCollection<Document> collectionProduct,
			GestionListeProduit glp) {
		
		System.out.println(valeurCliquerDansListCategories);
		
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("categories_tags", valeurCliquerDansListCategories);
		 MongoCursor<Document> cursor = collectionProduct.find(regexQuery).iterator();
//		System.out.println("\n Exemple de produits dans la collection categories, contenant le mot : "+ " en:"+valeurCliquerDansListCategories);
		glp.cleanListCategorie();
		
		try {
			while (cursor.hasNext()) {
				Document product = cursor.next();
				
				glp.setListProduit(product.getString("product_name"));
			}
		} finally {
			cursor.close();
		}
	}
	
	public JSONArray getInformationsAffichageProduitsList(String valeurCliquerDansListCategories, MongoCollection<Document> collectionProduct) throws JSONException{
		
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("categories_tags", valeurCliquerDansListCategories);
		MongoCursor<Document> cursor = collectionProduct.find(regexQuery).iterator();

		JSONArray jsonArrayProducts = new JSONArray();
		
		try {
			while (cursor.hasNext()) {
				Document product = cursor.next();
				JSONObject jsonProduct = new JSONObject();
				jsonProduct.put("id", product.getString("_id"));
				jsonProduct.put("nutriments", product.get("nutriments"));
				jsonProduct.put("brands", product.get("brands"));
				jsonArrayProducts.put(jsonProduct);
			}
		} finally {
			cursor.close();
		}
		return jsonArrayProducts;
	}
	
public JSONArray getInformationsProduitsCSV(String valeurCliquerDansListCategories, MongoCollection<Document> collectionProduct) throws JSONException{
		
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("categories_tags", valeurCliquerDansListCategories);
		MongoCursor<Document> cursor = collectionProduct.find(regexQuery).iterator();

		JSONArray jsonArrayProducts = new JSONArray();
		
		try {
			while (cursor.hasNext()) {
				Document product = cursor.next();
				JSONObject jsonProduct = new JSONObject();
				jsonProduct.put("id", product.getString("_id"));
				jsonProduct.put("product_name", product.getString("product_name"));
				jsonProduct.put("nutriments", product.get("nutriments"));
				jsonProduct.put("brands", product.get("brands"));
				jsonArrayProducts.put(jsonProduct);
			}
		} finally {
			cursor.close();
		}
		return jsonArrayProducts;
	}
	
}
