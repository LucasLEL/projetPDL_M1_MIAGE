import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class IhmTest {
	static String recherche = "";
	static String categorie = "";
	JComboBox cbInput;
	ArrayList<String> items;
	DefaultComboBoxModel model;
	static JList listProduits = new JList();
	
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
		listProduits.setVisibleRowCount(17);
		
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
				listProduits.setListData(tab);
				frame.getContentPane().add(listProduits);
				frame.add(new JScrollPane(listProduits));
				nbResultatsProduits.setText("Nombre de résultats : "+ String.valueOf(Main.glp.getSize()));
				System.out.println("Nombre de résultats : "+ Main.glp.getSize());
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