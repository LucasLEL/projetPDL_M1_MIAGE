package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * Classe Produit qui récupère des produits et leurs informations dans la base MongoDB
 *
 */
public class Produit {

	private HashMap<String, ArrayList<String>> hashIngredientsListProducts;
	private ArrayList<String> arrayNutriments;

	/**
	 * Méthode qui permet de récupérer la liste des produits correspondants à la catégorie entrée par l'utilisateur, pour l'affichage de la liste des produits
	 * @param valeurCliquerDansListCategories : critère (catégorie) saisie par l'utilisateur
	 * @param collectionProduct : collection Mongo permettant d'effectuer la requête MongoDB
	 * @param glp : Liste permettant de stocker et d'afficher la liste des produits
	 */
	public void affichageProduit(String valeurCliquerDansListCategories, MongoCollection<Document> collectionProduct, GestionListeProduit glp) {

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
	
	/**
	 * Méthode qui permet de récupérer les informations des produits, correspondants à la catégorie entrée par l'utilisateur, pour la création du fichier CSV 
	 * @param valeurCliquerDansListCategories : critère (catégorie) saisie par l'utilisateur
	 * @param collectionProduct : collection Mongo permettant d'effectuer la requête MongoDB
	 * @return : Les informations sous forme de JSONArray
	 * @throws JSONException : exception JSONException
	 */
	public JSONArray getInformationsProduitsCSV(String valeurCliquerDansListCategories, MongoCollection<Document> collectionProduct) throws JSONException {

		BasicDBObject regexQuery = new BasicDBObject();
		regexQuery.put("categories_tags", valeurCliquerDansListCategories);
		MongoCursor<Document> cursor = collectionProduct.find(regexQuery).iterator();
		JSONArray jsonArrayProducts = new JSONArray();
		// HashMap contenant en clé l'Id des ingrédient, et en valeur une ArrayList de produits contenant l'ingrédient en clé
		hashIngredientsListProducts = new HashMap<>(); 
		arrayNutriments = new ArrayList<String>();

		try {
			while (cursor.hasNext()) {
				Document product = cursor.next();
				JSONObject jsonProduct = new JSONObject();
				
				//Récupération de l'id, du nom et de la marque d'un produit 
				
				jsonProduct.put("id", product.getString("_id"));
				jsonProduct.put("product_name", product.getString("product_name"));
				jsonProduct.put("brands", product.get("brands"));
				
				//Construction et récupération de l'url correspondant à l'image d'un produit
				
				//La construction nécessite base URL + id + rev + front (front ou front_fr)
				//Exemple : https://static.openfoodfacts.org/images/products/322/247/575/8669/front_fr.5.200.jpg

				String front_img ="";
				String rev_img ="";
				Document img = (Document) product.get("images");
				JSONObject jsonImage = new JSONObject(img);
				for (Iterator iterator = jsonImage.keys(); iterator.hasNext();) {
					Object cle = iterator.next();
					Object val = jsonImage.get(String.valueOf(cle));
					if(cle.equals("front_fr")||cle.equals("front")||cle.equals("front_en")||cle.equals("front_de")) {
						front_img = cle.toString();
						Document valRev = Document.parse(val.toString() );
						JSONObject jsonRev = new JSONObject(valRev);
						for (Iterator iteratorForRev = jsonRev.keys(); iteratorForRev.hasNext();) {
							Object cleForRev = iteratorForRev.next();
							Object valForRev = jsonRev.get(String.valueOf(cleForRev));
							if(cleForRev.equals("rev")) {
								rev_img = valForRev.toString();
							}
						}
					}					
				}

				String url_image;
				String id_traitement = "";
				String base_url = "https://static.openfoodfacts.org/images/products/";
				if (product.getString("_id").length() == 8) {
					id_traitement = product.getString("_id");
				} else if (product.getString("_id").length() == 13) {
					id_traitement = product.getString("_id").substring(0, 3) + "/"
							+ product.getString("_id").substring(3, 6) + "/" + product.getString("_id").substring(6, 9)
							+ "/" + product.getString("_id").substring(9, 13);
				}
				String end_url = front_img + "." + rev_img + ".200.jpg";
				url_image = base_url + id_traitement + "/" + end_url;
				
				jsonProduct.put("image", url_image);
				
				//Récupération des ingrédients
				
				ArrayList<Document> listIngredients = (ArrayList<Document>) product.get("ingredients");

				for (Document ingredient : listIngredients) {

					jsonProduct.put(ingredient.get("id").toString(), "true");
					ArrayList<String> listProductsContainedIngredient = null;
					if (hashIngredientsListProducts.containsKey(ingredient.get("id"))) {
						listProductsContainedIngredient = hashIngredientsListProducts.get(ingredient.get("id"));
						listProductsContainedIngredient.add(product.getString("_id"));
						hashIngredientsListProducts.replace(ingredient.get("id").toString(), listProductsContainedIngredient);
					} else {
						listProductsContainedIngredient = new ArrayList<String>();
						listProductsContainedIngredient.add(product.getString("_id"));
						hashIngredientsListProducts.put(ingredient.get("id").toString(), listProductsContainedIngredient);
					}
				}

				//Récupération des nutriments
				
				Document nutriments = (Document) product.get("nutriments");
				JSONObject jsonNutriments = new JSONObject(nutriments);
				
				if (jsonNutriments.length() > 0) {
					for (int i = 0; i < jsonNutriments.names().length(); i++) {
						String key = jsonNutriments.names().getString(i);
						String value = (String) String.valueOf(jsonNutriments.get(jsonNutriments.names().getString(i)));
						jsonProduct.put(key, value);
						if (!arrayNutriments.contains(key)) {
							arrayNutriments.add(key);
						}
					}
				}
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
