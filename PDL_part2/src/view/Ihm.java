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
import java.util.ArrayList;
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
import controller.Controller;

public class Ihm {
	
	private JFrame frame;
	private JTextField txtRecherche;
	private JLabel nbResultats;
	private JButton btRecherche;
	private JTextField txtInput;
	private JButton btValider;
	private JLabel resultats;
	private JButton btInfo;
	private JLabel nbResultatsProduits;
	private JButton btGenererCSV;
	private String recherche;
	private String categorie;
	private JList<Object> listProduits;
	private Controller controller;
	
	public Ihm(Controller controller){
		this.controller = controller;
		this.initialisaionFenetre();
		this.initialisationListenersIHM();
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
		this.listProduits = new JList<Object>();
		this.recherche = "";
		this.categorie = "";
		
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
		nbResultats.setBounds(5, 40, 250, 20);
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
	
	private void initialisationListenersIHM(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

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
				controller.actionBoutonRechercher(getRecherche());
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
			@Override
			public void actionPerformed(ActionEvent ev) {
				resultats.removeAll();
				listProduits.setListData(new Object[0]);
				resultats.setText("");
				categorie = txtInput.getText();
				// Retourne le tag lié à un name
				String tag = controller.getGlc().getTagFromName(categorie);
				controller.actionBoutonValider(tag);
				String[] tab = new String[controller.getGlp().getSize()];
				
				for(int i = 0 ; i < controller.getGlp().getSize() ; i++)
				{
					tab[i] = controller.getGlp().getElement(i);
					
				}
				listProduits.setListData(tab);
				resultats.add(listProduits);
				
				resultats.add(new JScrollPane(listProduits));
				nbResultatsProduits.setText("Nombre de résultats : "+ String.valueOf(controller.getGlp().getSize()));
				btGenererCSV.setVisible(true);
			}
		});	
		
		btGenererCSV.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				String nameOfFile = controller.actionBoutonGenererCSV(categorie);
				JOptionPane.showMessageDialog(frame, "Fichier CSV \""+nameOfFile+"\" créé !");
			}
		});	
	}
	
	private boolean isAdjusting(JComboBox<Object> cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}

	private void setAdjusting(JComboBox<Object> cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}

	public void setupAutoComplete(final JTextField txtInput, final ArrayList<String> items) {
		DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<Object>();
		JComboBox<Object> cbInput = new JComboBox<Object>(model) {
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
			}
		});
			
		
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
	
	
	public void updateList(JComboBox<Object> cbInput, ArrayList<String> items, JTextField txtInput, DefaultComboBoxModel<Object> model) {
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

	public JTextField getTxtRecherche() {
		return txtRecherche;
	}

	public void setTxtRecherche(JTextField txtRecherche) {
		this.txtRecherche = txtRecherche;
	}

	public JTextField getTxtInput() {
		return txtInput;
	}

	public void setTxtInput(JTextField txtInput) {
		this.txtInput = txtInput;
	}

	public JButton getBtRecherche() {
		return btRecherche;
	}

	public void setBtRecherche(JButton btRecherche) {
		this.btRecherche = btRecherche;
	}

	public JButton getBtValider() {
		return btValider;
	}

	public void setBtValider(JButton btValider) {
		this.btValider = btValider;
	}

	public JButton getBtGenererCSV() {
		return btGenererCSV;
	}

	public void setBtGenererCSV(JButton btGenererCSV) {
		this.btGenererCSV = btGenererCSV;
	}
	
	
	
}