package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.JSONArray;
import org.json.JSONException;

import controller.Controller;
import model.GestionnaireCSV;

public class IhmTest {
	static String recherche = "";
	static String categorie = "";
	JComboBox cbInput;
	ArrayList<String> items;
	DefaultComboBoxModel model;
	static JList listProduits = new JList();
	static JList listIngredients = new JList();
	
	private JFrame frame = new JFrame();
	private JTextField txtRecherche = new JTextField();
	private JLabel nbResultats = new JLabel("", SwingConstants.CENTER);
	private JButton btRecherche = new JButton("Rechercher");
	private JTextField txtInput = new JTextField();
	private JButton btValider = new JButton("Valider");
	private JLabel resultats = new JLabel();
	private JButton btInfo = new JButton("Informations");
	private JLabel nbResultatsProduits = new JLabel();
	private JButton btGenererCSV = new JButton("Générer CSV");
	
	private Controller controller;
	
	public IhmTest(Controller controller){
		this.controller = controller;
		this.initialisaionFenetre();
		try {
			this.initialisationListenersIHM();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	private void initialisaionFenetre(){
		this.frame = new JFrame();
		this.txtRecherche = new JTextField();
		this.nbResultats = new JLabel("", SwingConstants.CENTER);
		this.btRecherche = new JButton("Rechercher");
		this.txtInput = new JTextField();
		this.btValider = new JButton("Valider");
		this.resultats = new JLabel();
		this.btInfo = new JButton("Informations");
		this.nbResultatsProduits = new JLabel();
		this.btGenererCSV = new JButton("Générer CSV");
		
		
		txtRecherche.setColumns(30);
		nbResultats.setVerticalAlignment(SwingConstants.TOP);
		txtInput.setVisible(false);
		btValider.setVisible(false);
		btGenererCSV.setVisible(false);
		setupAutoComplete(txtInput, this.controller.getGlc().getListCategoriesName());
		txtInput.setColumns(30);
		
		frame.setLayout(null);
		frame.add(txtRecherche);
		frame.add(btRecherche);
		frame.add(nbResultats);
		frame.add(txtInput);
		frame.add(btValider);
		frame.add(resultats);
		frame.add(btGenererCSV);
		frame.add(nbResultatsProduits);
		frame.add(btInfo);
		resultats.setLayout(new GridLayout());
	
		
		txtRecherche.setBounds(10, 10, 400, 20);
		btRecherche.setBounds(420, 10, 120, 20);
		nbResultats.setBounds(10, 40, 200, 20);
		txtInput.setBounds(10, 70, 400, 20);
		btValider.setBounds(420, 70, 120, 20);
		nbResultatsProduits.setBounds(10, 100, 400, 20);
		resultats.setBounds(10, 130, 540, 200);
		btGenererCSV.setBounds(420, 340, 120, 20);
		btInfo.setBounds(10, 340, 120, 20);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(560, 400);
		frame.setTitle("PDL - OFF");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	private void initialisationListenersIHM() throws ClassNotFoundException, InstantiationException, IllegalAccessException,UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		txtRecherche.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btRecherche.doClick();
	            }
			}
		});
		
		btRecherche.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				txtInput.setText("");
				resultats.setText("");
				txtInput.setVisible(true);
				btValider.setVisible(true);
				txtInput.requestFocus();
				frame.revalidate();
				recherche = txtRecherche.getText();
				controller.actionBouton(getRecherche());
				nbResultats.setText(String.valueOf(controller.getGlc().getSize()) + " catégories comportant le mot : " + recherche);
				nbResultatsProduits.setText("");
				btGenererCSV.setVisible(false);
			
			}

		});
		
		
		btInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JFrame frameInfo = new JFrame();
				frameInfo.setSize(450, 150);
				frameInfo.setTitle("PDL - OFF - Informations");
				frameInfo.setResizable(false);
				frameInfo.setVisible(true);
				frameInfo.setLocationRelativeTo(null);
				frameInfo.setLayout(null);
				JLabel informations = new JLabel();
				frameInfo.add(informations);
				informations.setText("<html>Les données de l'appplication sont issues de Open Food Facts (https://world.openfoodfacts.org/). <br/> <br/>L'application à été "
						+ "réalisée dans le cadre du module de Projet de Développement Logiciel (PDL) de M1 MIAGE à l'université de Rennes 1 par Vivien Busson, Lucas Lelièvre, "
						+ "Olivier La Rivière, Clément Le Huërou et Vincent Pelletier. </html>");
				informations.setBounds(10, 10, 430, 80);
			}
		});
		
		btValider.addActionListener(new ActionListener() {
			@SuppressWarnings("null")
			@Override
			public void actionPerformed(ActionEvent ev) {
				resultats.removeAll();
				listProduits.setListData(new Object[0]);
				resultats.setText("");
				categorie = txtInput.getText();
				System.out.println("act = "+categorie);
				// Retourne le tag lié à un name
				String tag = controller.getGlc().getTagFromName(categorie);
				controller.actionBoutonValider(tag);
				String[] tab = new String[controller.getGlp().getSize()];
				
				for(int i = 0 ; i < controller.getGlp().getSize() ; i++)
				{
//					resutats.setText(resutats.getText() + " // " + Main.glp.getElement(i));
					tab[i] = controller.getGlp().getElement(i);
					
				}
				//Arrays.sort(tab); tri de la liste -> problème affichage, hypothèse -> caractères spéciaux
				listProduits.setListData(tab);
				resultats.add(listProduits);
				
				resultats.add(new JScrollPane(listProduits));
				nbResultatsProduits.setText("Nombre de résultats : "+ String.valueOf(controller.getGlp().getSize()));
				System.out.println("Nombre de résultats : "+ controller.getGlp().getSize());
				btGenererCSV.setVisible(true);
			}
		});	
		
		
		btGenererCSV.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				GestionnaireCSV csvGest = new GestionnaireCSV(",");
				String categorieTag = controller.getGlc().getTagFromName(categorie);
				controller.actionBoutonValider(categorieTag);
				JSONArray arrayOfProducts = controller.getInformationsCSV(categorieTag);
				
				ArrayList<String> headersList = new ArrayList<String>();
				headersList.add("id");
				headersList.add("product_name");
				headersList.add("brands");
				headersList.add("image");
				
				HashMap<String, ArrayList<String>> hash1 = controller.getListOfProductsForEachIngredient();
				ArrayList<String> arrayOfNutriments = controller.getListOfNutriments(); 
				
				float nombreProduitsTotal = arrayOfProducts.length();
				
				for(Map.Entry<String, ArrayList<String>> entry : hash1.entrySet()) {
				    String key = entry.getKey();	
				    
				    ArrayList<String> values = entry.getValue();
				    
				    float nbProducts = values.size();
				    //Si le nombre de produits contenant un ingredient est supérieur à 3% du nombre total, et à un "id" non vide, on l'ajoute dans le header permettant de former le CSV
				    
				    float pourcentage = (nbProducts/nombreProduitsTotal)*100;
				 
				    if(pourcentage >= 3 && !key.isEmpty()){
				    	headersList.add(key);
				    }
				}
				
				for(String nutriment : arrayOfNutriments){
					headersList.add(nutriment);
				}
				
				
				
				String nameOfFile="export.csv";
				
				try {
					FileWriter csvFile = csvGest.createCSVFile(nameOfFile);
					csvGest.addHeaders(csvFile, headersList);
					csvGest.addDatas(csvFile, arrayOfProducts, headersList);
					csvGest.closeCSVFile(csvFile);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(frame, "Fichier CSV \""+nameOfFile+"\" créé !");
			}
		});	
	}
	
	
	
	private static boolean isAdjusting(JComboBox cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}

	private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}

	public static void setupAutoComplete(final JTextField txtInput, final ArrayList<String> items) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		@SuppressWarnings("unchecked")
		
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		
		setAdjusting(cbInput, false);
		for (String item : items) {
			model.addElement(item);
		}
		cbInput.setSelectedItem(null);
		cbInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(cbInput)) {
					if (cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
					}
				}
			}
		});

		txtInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//Déclanche valider
	            }
							
				setAdjusting(cbInput, true);
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (cbInput.isPopupVisible()) {
						e.setKeyCode(KeyEvent.VK_ENTER);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(cbInput);
					cbInput.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						cbInput.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cbInput.setPopupVisible(false);
				}
				setAdjusting(cbInput, false);		 
			}
		});
		
		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateList(cbInput, items, txtInput, model);
			}

			public void removeUpdate(DocumentEvent e) {
				updateList(cbInput, items, txtInput, model);
			}

			public void changedUpdate(DocumentEvent e) {
				updateList(cbInput, items, txtInput, model);
			}
		});
		
		txtInput.addFocusListener(new FocusListener() {
		      public void focusGained(FocusEvent e) {
		    	  updateList(cbInput, items, txtInput, model);
		      }
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
			
		
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
	
	
	public static void updateList(JComboBox cbInput, ArrayList<String> items, JTextField txtInput, DefaultComboBoxModel model) {
		setAdjusting(cbInput, true);
		model.removeAllElements();
		String input = txtInput.getText();
		for (String item : items) {
			if (item.toLowerCase().contains((input.toLowerCase()))) {
				model.addElement(item);
			}
		}
		cbInput.setPopupVisible(model.getSize() > 0);
		setAdjusting(cbInput, false);
	}
	
	public String getRecherche(){
		return recherche;
	}
	
	public String getCategorie(){
		return categorie;
	}
}