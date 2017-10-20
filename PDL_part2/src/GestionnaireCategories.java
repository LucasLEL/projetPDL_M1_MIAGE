import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.json.JSONArray;

public class GestionnaireCategories {
	
	private static final String URL_API_CATEGORIES = "https://world.openfoodfacts.org/categories.json";
	private MongoCollection<Document> categories;
	
	/**
	 * Constructeur de la classe
	 * @param dataBase
	 */
	public GestionnaireCategories(MongoCollection<Document> categories){
		this.categories = categories;
	}
	
	/**
	 * Méthode qui effectue un appel à l'API OpenFoodFact pour récupérer la liste des catégories
	 * @return le json correspondant à la liste des catégories
	 * @throws IOException
	 * @throws JSONException
	 */
	public String getJsonCategories() throws IOException, JSONException {
		String jsonRet = "";
		URL searchURL = new URL(URL_API_CATEGORIES);
		URLConnection yc = searchURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			jsonRet += inputLine;
		}
		in.close();
		return jsonRet;
	}
	
	/**
	 * Méthode qui transforme le json de categories, en ArrayList de "Document" de categories
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<Document> getArrayListOfCategories(String json) throws JSONException{
		ArrayList<Document> arrayListOfCategories = new ArrayList<Document>();
		JSONObject jsonObjectOfCategories = new JSONObject(json);
		JSONArray jsonArrayOfCategories = jsonObjectOfCategories.getJSONArray("tags");

		for (int i = 0; i < jsonArrayOfCategories.length(); i++) {
		    JSONObject row = jsonArrayOfCategories.getJSONObject(i);
		    String categoryName = row.getString("id");
		    Document theMongoCategory = new Document("name", categoryName);
		    arrayListOfCategories.add(theMongoCategory);
		}
		return arrayListOfCategories;
	}
	
	/**
	 * Méthode qui insert en Base de données toutes les catégories
	 * @param arrayListOfCategories
	 * @throws JSONException
	 */
	public void insertCategoriesInDataBase(ArrayList<Document> arrayListOfCategories) throws JSONException{
		this.categories.insertMany(arrayListOfCategories);
	}
	
	public void dropCollection(){
		this.categories.drop();
	}

}
