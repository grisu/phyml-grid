package phyml;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * This JPanel implements the components to specify whether the "Equilibrium
 * Frequency" is optimised or not and whether the "Transition/Transversion Ratio"
 * is estimated or user defined.
 * 
 * @author Christoph Knapp
 * @date 02/07/12
 */
public class OptimiseFreqAndRatio extends JPanel implements ActionListener {
	/**
	 * default id
	 */
	private static final long serialVersionUID = 1L;
	private JLabel ratioLab;
	private JRadioButton choice1;
	private JRadioButton choice2;
	private CustomTextField ratioField;
	private final static String DNA = "DNA";
	private final static String AA = "AA";
	private boolean isDNA;
	private String moleculeType;

	/**
	 * This JPanel implements the components to specify whether the "Equilibrium
	 * Frequency" is optimised or not and whether the "Transition/Transversion"
	 * ratio is estimated or user defined.
	 * 
	 * @param moleculeType
	 * String : Which molecule type the data is (DNA or AA).
	 */
	public OptimiseFreqAndRatio(String moleculeType) {
		this.moleculeType = moleculeType;
		ratioLab = new JLabel("Transition/Transversion Ratio");
		choice1 = new JRadioButton("Estimated");
		choice1.setSelected(true);
		choice2 = new JRadioButton("Fixed");
		ratioField = new CustomTextField("4.0");
		ratioField.setEnabled(false);
		if (moleculeType.equals(DNA)) {
			isDNA = true;
		} else if (moleculeType.equals(AA)) {
			isDNA = false;
			ratioLab.setVisible(false);
			ratioField.setVisible(false);
			choice1.setVisible(false);
			choice2.setVisible(false);
		}
		choice1.addActionListener(this);
		choice2.addActionListener(this);
		
		
		CustomGridLayout layout = new CustomGridLayout();
		setLayout(layout);
		layout.setDimensions(1, 0.1);
		add(new JPanel());
		layout.setDimensions(0.01, 0.9);
		add(new JPanel());
		layout.setDimensions(0.33, 0.9);
		add(ratioLab);
		layout.setDimensions(0.27, 0.9);
		add(ratioField);
		layout.setDimensions(0.38, 0.9);
		JPanel p1 = new JPanel();
		CustomGridLayout lo1 = new CustomGridLayout();
		p1.setLayout(lo1);
		add(p1);
		lo1.setDimensions(0.2, 1);
		p1.add(new JPanel());
		lo1.setDimensions(0.4, 1);
		p1.add(choice1);
		lo1.setDimensions(0.4, 1);
		p1.add(choice2);
		layout.setDimensions(0.01, 0.9);
		add(new JPanel());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == ratioBox) {
//			if (ratioBox.getSelectedItem() == "fixed") {
//				ratioField.setEnabled(true);
//			} else {
//				ratioField.setEnabled(false);
//			}
//		}
		if (e.getSource() == choice1) {
			if(choice1.isSelected()){
				choice2.setSelected(false);
				ratioField.setEnabled(false);
			}else{
				choice2.setSelected(true);
				ratioField.setEnabled(true);
			}
		}else if(e.getSource() == choice2){
			if(choice2.isSelected()){
				choice1.setSelected(false);
				ratioField.setEnabled(true);
			}else{
				choice1.setSelected(true);
				ratioField.setEnabled(false);
			}
		}
	}

	/**
	 * When the Moleculetype is changed in a DataType object the moleculetype
	 * needs to change here as well.
	 * 
	 * @param moleType
	 * String : Whether "DNA" or "AA" is selected in the DataType JPanel
	 */
	public void setMoleculeType(String moleType) {
		moleculeType = moleType;
		if (moleType.equals(DNA)) {
			isDNA = true;
//			optimiseLab.setVisible(true);
//			optimiseBox.setVisible(true);
			ratioLab.setVisible(true);
//			ratioBox.setVisible(true);
			choice1.setVisible(true);
			choice2.setVisible(true);
			ratioField.setVisible(true);
		} else if (moleType.equals(AA)) {
			isDNA = false;
//			optimiseLab.setVisible(false);
//			optimiseBox.setVisible(false);
			ratioLab.setVisible(false);
//			ratioBox.setVisible(false);
			choice1.setVisible(false);
			choice2.setVisible(false);
			ratioField.setVisible(false);
		}
	}

	/**
	 * Retrieves whether "DNA" or "AA" is set as molecule type.
	 * 
	 * @return
	 * boolean : returns true if molecule type is "DNA" and false when "AA".
	 */
	public boolean isDNA() {
		return isDNA;
	}

	/**
	 * Private class implementing a CustomTextField (extends JTextField) so that
	 * only double values can be entered.
	 */
	private class CustomTextField extends JTextField {
		/**
		 * default id
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor only accepting Strings which can be converted into
		 * Double.
		 * 
		 * @param string
		 * String : Needs to be convertable into a Double value or it will
		 * not be accepted. Otherwise it sets the displayed default
		 * value of the TextField.
		 */
		public CustomTextField(String string) {
			super(string);
			if (!isNumber(string)) {
				this.setText("");
			}
		}

		@Override
		protected Document createDefaultModel() {
			return new doubleOnlyDocument();
		}

		/**
		 * tests whether the input value can be converted into a Double value.
		 * 
		 * @param str
		 * String : TextField input String
		 * @return 
		 * boolean : true if str can be converted, false otherwise
		 */
		private boolean isNumber(String str) {
			try {
				Double.parseDouble(str);
				return true;
			} catch (NumberFormatException e) {
				if (str.equals(".") && !getTsTvRatio().contains(".")) {
					return true;
				}
				return false;
			}
		}

		/**
		 * Private class extending the PlainDocument document making sure only
		 * Double values can be typed into the JTextField
		 */
		private class doubleOnlyDocument extends PlainDocument {
			/**
			 * default id
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {
				if (str == null || !isNumber(str)) {
					return;
				}
				super.insertString(offs, str, a);
			}
		}
	}

	/**
	 * Returns whether the TS/TV ratio is estimated or fixed.
	 * 
	 * @return 
	 * String : "e" for estimated or Double value from CustomTextField for fixed
	 * or "" otherwise.
	 */
	public String getTsTvRatio() {
		if (moleculeType.equals(DNA)) {
//			if (ratioBox.getSelectedItem().toString().equals("estimated")) {
			if (choice1.isSelected()) {
				return "e";
			}
			return ratioField.getText();
		}
		return "";
	}
	/**
	 * Enables or disables the Transition/Transversion Ratio dropdownmenu.
	 * 
	 * @param off
	 * boolean : if true disabled otherwise enabled;
	 */
	public void setRatioBoxOff(boolean off){
//		ratioBox.setEnabled(!off);
		choice1.setEnabled(!off);
		choice2.setEnabled(!off);
	}
	/**
	 * Sets the index of ratioBox to 0.
	 */
	public void setRatioBoxDefault() {
//		ratioBox.setSelectedIndex(0);
		choice1.setSelected(true);
		choice2.setSelected(false);
	}

}
