import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

public class IhmTest {
	static String recherche = "";
	static String categorie = "";
	JComboBox cbInput;
	ArrayList<String> items;
	DefaultComboBoxModel model;
	static JList listProduits = new JList();
	static JList listIngredients = new JList();
	
	public String getRecherche(){
		return recherche;
	}
	
	public String getCategorie(){
		return categorie;
	}
	@SuppressWarnings("unused")
	static void recuperationData() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		//Cr�ation fenetre
		JFrame frame = new JFrame();
		frame.setSize(500, 450);
		frame.setLocationRelativeTo(null);
		frame.setTitle("PDL - OFF");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		
		
		//D�claration des �l�ments
		JTextField txtRecherche = new JTextField();
		JLabel nbResultats = new JLabel("", SwingConstants.CENTER);
		JButton btRecherche = new JButton("Rechercher");
		JTextField txtInput = new JTextField();
		JButton btValider = new JButton("Valider");
		JLabel resutats = new JLabel();
		JLabel nbResultatsProduits = new JLabel();
		JButton btGenererCSV = new JButton("Générer CSV");
		JButton btFilter = new JButton("Filtrer les résultats");
		
		
		//Caract�ristiques
		txtRecherche.setColumns(30);
		

		txtRecherche.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btRecherche.doClick();
	            }
			}
		});
		
		
		nbResultats.setVerticalAlignment(SwingConstants.TOP);
		txtInput.setVisible(false);
		btValider.setVisible(false);
		btGenererCSV.setVisible(false);
		btFilter.setVisible(false);
		setupAutoComplete(txtInput, Main.glc.getListCategoriesName());
		txtInput.setColumns(30);
//		resutats.setPreferredSize(new Dimension(300,100));
		
		//ajout �l�ments sur fenetre
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(txtRecherche);
		frame.getContentPane().add(btRecherche);
		frame.getContentPane().add(txtInput);
		frame.getContentPane().add(btValider);
		frame.getContentPane().add(nbResultats);
		frame.getContentPane().add(nbResultatsProduits);
		frame.getContentPane().add(btFilter);
		frame.getContentPane().add(btGenererCSV);
		
		
//		frame.getContentPane().add(resutats);
		
		
		
		
//		nbResultats.setBounds(80, 180, 40, 20);
//		resutats.setBounds(280, 250, 40, 20);
		frame.setVisible(true);
		
		btRecherche.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				txtInput.setText("");
				resutats.setText("");
				txtInput.setVisible(true);
				btValider.setVisible(true);
				txtInput.requestFocus();
				frame.revalidate();
				recherche = txtRecherche.getText();
				Main.actionBouton();
				nbResultats.setText(String.valueOf(Main.glc.getSize()) + " catégories comportant le mot : " + recherche);
				//frame.getContentPane().remove(listProduits);
				nbResultatsProduits.setText("");
				btGenererCSV.setVisible(false);
				btFilter.setVisible(false);
//				listProduits.setVisible(false);
			
			}

		});
		
		btValider.addActionListener(new ActionListener() {
			@SuppressWarnings("null")
			@Override
			public void actionPerformed(ActionEvent ev) {
				listProduits.setListData(new Object[0]);
				resutats.setText("");
				categorie = txtInput.getText();
				System.out.println("act = "+categorie);
				// Retourne le tag lié à un name
				String tag = Main.glc.getTagFromName(categorie);
				Main.actionBoutonValider(tag);
				String[] tab = new String[Main.glp.getSize()];
				
				for(int i = 0 ; i < Main.glp.getSize() ; i++)
				{
//					resutats.setText(resutats.getText() + " // " + Main.glp.getElement(i));
					tab[i] = Main.glp.getElement(i);
					
				}
				//Arrays.sort(tab); tri de la liste -> problème affichage, hypothèse -> caractères spéciaux
				listProduits.setListData(tab);
				frame.getContentPane().add(listProduits);
				frame.add(new JScrollPane(listProduits));
				nbResultatsProduits.setText("Nombre de résultats : "+ String.valueOf(Main.glp.getSize()));
				System.out.println("Nombre de résultats : "+ Main.glp.getSize());
				btGenererCSV.setVisible(true);
				btFilter.setVisible(true);
			}
		});	
		
		//ouvre une nouvelle fenetre et demande à l'utilisateur de selectionner les ingrédients que les 
		//produits doivent possèder.
		btFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JFrame frameFilter = new JFrame();
				frameFilter.setSize(300, 300);
				frameFilter.setLocationRelativeTo(null);
				frameFilter.setTitle("Filtre de produits");
				frameFilter.setLayout(null);
				frameFilter.setResizable(false);	
				
				
				//Dans l'idée : 
				//Affichage sur la fenetre de tous les ingrédients avec une checkbox (précochée)
				//Si on décoche la case et on enregistre ça vire tous les produits avec l'élément concerné. 
				
				//Autre possibilité des "regles de gestion" avec deux listes déroulantes : 
				// la 1ere : une liste d'action (contient / ne contient pas)
				// la 2e : la liste des ingrédients. 
				// Les produits affichés seront alors ceux qui contiennent ou ne contiennent pas les ingrédients indiqués.
				//si aucune regle on affiche tout. 
				
	
				listIngredients.setListData(new Object[0]);
				listIngredients.setVisibleRowCount(10);
				frameFilter.getContentPane().add(listIngredients);
				frameFilter.add(new JScrollPane(listIngredients));
				
				//affichage ingrédients.
				
				frameFilter.setVisible(true);
				
			}

		});
		
		btGenererCSV.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				GestionnaireCSV csvGest = new GestionnaireCSV(",");
				String categorieTag = Main.glc.getTagFromName(categorie);
				Main.actionBoutonValider(categorieTag);
				JSONArray arrayOfProducts = Main.getInformationsCSV(categorieTag);
				
				ArrayList<String> headersList = new ArrayList<String>();
				headersList.add("id");
				headersList.add("product_name");
				headersList.add("brands");
				
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
}