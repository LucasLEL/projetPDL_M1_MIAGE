import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class IhmTest {
	static String recherche = "";
	static String produit = "";
	
	public String getRecherche(){
		return recherche;
	}
	
	public String getProduit(){
		return produit;
	}
	@SuppressWarnings("unused")
	static void recuperationData() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		//Création fenetre
		JFrame frame = new JFrame();
		frame.setSize(600, 300);
		frame.setLocationRelativeTo(null);
		frame.setTitle("PDL - OFF");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		
		
		//Déclaration des éléments
		JTextField txtRecherche = new JTextField();
		JLabel nbResultats = new JLabel("", SwingConstants.CENTER);
		JButton btRecherche = new JButton("Rechercher");
		JTextField txtInput = new JTextField();
		JButton btValider = new JButton("Valider");
		JLabel resutats = new JLabel();
		
		//Caractéristiques
		txtRecherche.setColumns(30);
		nbResultats.setVerticalAlignment(SwingConstants.TOP);
		txtInput.setVisible(false);
		setupAutoComplete(txtInput, Main.glc.getListCategories());
		txtInput.setColumns(30);
		
		//ajout éléments sur fenetre
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(txtRecherche);
		frame.getContentPane().add(btRecherche);
		frame.getContentPane().add(btValider);
		frame.getContentPane().add(txtInput, BorderLayout.NORTH);
		frame.getContentPane().add(nbResultats);
		frame.getContentPane().add(resutats);
		
		
		
//		nbResultats.setBounds(80, 180, 40, 20);
//		resutats.setBounds(280, 250, 40, 20);
		frame.setVisible(true);
		
		btRecherche.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				txtInput.setVisible(true);
				txtInput.setText("");
				frame.revalidate();
				recherche = txtRecherche.getText();
				Main.actionBouton();
				nbResultats.setText(String.valueOf(Main.glc.getSize()) + " catégories comportant le mot : " + recherche);
			}

		});
		
		btValider.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				produit = txtInput.getText();
				
				System.out.println("act = "+produit);
				
				Main.actionBoutonValider(produit);				
				
				for(int i = 0 ; i < Main.glp.getSize() ; i++)
				{
					System.out.println("Nombre de résultats : "+Main.glp.getSize());
					resutats.setText(resutats.getText() + " // " + Main.glp.getElement(i));
				}
				
				System.out.println("done");
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
				updateList();
			}

			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				setAdjusting(cbInput, true);
				model.removeAllElements();
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					for (String item : items) {
						if (item.toLowerCase().startsWith(input.toLowerCase())) {
							model.addElement(item);
						}
					}
				}
				cbInput.setPopupVisible(model.getSize() > 0);
				setAdjusting(cbInput, false);
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
}