
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Produit {
	
	private HashMap<String, ArrayList<String>> hash1;
	private HashMap<String, String> hash2;

	public void affichageProduit(String valeurCliquerDansListCategories, MongoCollection<Document> collectionProduct,
			GestionListeProduit glp) {
		
		System.out.println(valeurCliquerDansListCategories);
		
		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("categories_tags", valeurCliquerDansListCategories);
		 MongoCursor<Document> cursor = collectionProduct.find(regexQuery).iterator();
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
		
		hash1 = new HashMap<>(); //Id/ArrayList de Produits
		hash2 = new HashMap<>(); //Id/Libellé
		
		try {
			while (cursor.hasNext()) {
				Document product = cursor.next();
				JSONObject jsonProduct = new JSONObject();
				jsonProduct.put("id", product.getString("_id"));
				jsonProduct.put("product_name", product.getString("product_name"));
				jsonProduct.put("nutriments", product.get("nutriments"));
				jsonProduct.put("brands", product.get("brands"));
				
				ArrayList<Document> listIngredients = (ArrayList<Document>) product.get("ingredients");
				
				for (Document ingredient : listIngredients) {
					if(!hash2.containsKey(ingredient.get("id"))){
						hash2.put(ingredient.get("id").toString(), ingredient.get("text").toString());
					}
					
					ArrayList<String> listProductsContainedIngredient=null;
					if(hash1.containsKey(ingredient.get("id"))){
						listProductsContainedIngredient = hash1.get(ingredient.get("id"));
						listProductsContainedIngredient.add(product.getString("_id"));
						hash1.replace(ingredient.get("id").toString(), listProductsContainedIngredient);
					}
					else{
						listProductsContainedIngredient = new ArrayList<String>();
						listProductsContainedIngredient.add(product.getString("_id"));
						hash1.put(ingredient.get("id").toString(), listProductsContainedIngredient);
					}
				}
					
				jsonArrayProducts.put(jsonProduct);
				//System.out.println(product.toJson());
			}
		} finally {
			

			for(Map.Entry<String, String> entry : hash2.entrySet()) {
			    String key = entry.getKey();
			
			    System.out.println("key hash2: "+key);
			}
			
			for(Entry<String, ArrayList<String>> entry : hash1.entrySet()) {
			    String key = entry.getKey();
			    ArrayList<String> arrayListProducts = entry.getValue();
			
			    System.out.println("\n products contenus dans : "+key);
			    for (String products : arrayListProducts) {
			    	System.out.print(products);
			    }
			    
			}
			
			
			cursor.close();
		}
		return jsonArrayProducts;
	}

	public HashMap<String, ArrayList<String>> getHash1() {
		return hash1;
	}
	
	public void setHash1(HashMap<String, ArrayList<String>> hash1) {
		this.hash1 = hash1;
	}
	
	public HashMap<String, String> getHash2() {
		return hash2;
	}
	
	public void setHash2(HashMap<String, String> hash2) {
		this.hash2 = hash2;
	}
	
}
