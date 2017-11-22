
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
	
	private HashMap<String, ArrayList<String>> hashIngredientsListProducts;
	private HashMap<String, ArrayList<String>> hashNutrimentsListProducts;
	private ArrayList<String> arrayNutriments;

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
			
			hashIngredientsListProducts = new HashMap<>(); //Id/ArrayList de Produits
			
			arrayNutriments = new ArrayList<String>();
			
			try {
				while (cursor.hasNext()) {
					Document product = cursor.next();
					JSONObject jsonProduct = new JSONObject();
					jsonProduct.put("id", product.getString("_id"));
					jsonProduct.put("product_name", product.getString("product_name"));
					jsonProduct.put("nutriments", product.get("nutriments"));
					jsonProduct.put("brands", product.get("brands"));
					jsonProduct.put("image", product.getString("image_small_url"));
					
					ArrayList<Document> listIngredients = (ArrayList<Document>) product.get("ingredients");
					
					for (Document ingredient : listIngredients) {
						
						jsonProduct.put(ingredient.get("id").toString(), "true");
						
						
						ArrayList<String> listProductsContainedIngredient=null;
						if(hashIngredientsListProducts.containsKey(ingredient.get("id"))){
							listProductsContainedIngredient = hashIngredientsListProducts.get(ingredient.get("id"));
							listProductsContainedIngredient.add(product.getString("_id"));
							hashIngredientsListProducts.replace(ingredient.get("id").toString(), listProductsContainedIngredient);
						}
						else{
							listProductsContainedIngredient = new ArrayList<String>();
							listProductsContainedIngredient.add(product.getString("_id"));
							hashIngredientsListProducts.put(ingredient.get("id").toString(), listProductsContainedIngredient);
						}
					}
					
					Document nutriments = (Document) product.get("nutriments");
					JSONObject jsonNutriments = new JSONObject(nutriments);
					
					if(jsonNutriments.length() >0){
					
						for(int i = 0; i<jsonNutriments.names().length(); i++){
							
							String key = jsonNutriments.names().getString(i);
							String value = (String) String.valueOf(jsonNutriments.get(jsonNutriments.names().getString(i)));
							
						   jsonProduct.put(key,value);
						   				   
						   if(!arrayNutriments.contains(key)){
							   arrayNutriments.add(key);
								
							}
						   System.out.println("key = " + jsonNutriments.names().getString(i) + " value = " + jsonNutriments.get(jsonNutriments.names().getString(i)));
						}				
					   
					}
					

					
					//System.out.println(nutriments.toJson());
					
					//String nutriments = (String) 
					
					
					
					jsonArrayProducts.put(jsonProduct);
				}
			} finally {
				cursor.close();
			}
			return jsonArrayProducts;
		}

	public HashMap<String, ArrayList<String>> getHashIngredientsListProducts() {
		return hashIngredientsListProducts;
	}
	
	public void setHashIngredientsListProducts(HashMap<String, ArrayList<String>> hashIngredientsListProducts) {
		this.hashIngredientsListProducts = hashIngredientsListProducts;
	}

	public ArrayList<String> getArrayNutriments() {
		return arrayNutriments;
	}

	public void setArrayNutriments(ArrayList<String> arrayNutriments) {
		this.arrayNutriments = arrayNutriments;
	}
	
}

