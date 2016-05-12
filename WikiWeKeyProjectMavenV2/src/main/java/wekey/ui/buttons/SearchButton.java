package wekey.ui.buttons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import wekey.ui.buttons.actionlisteners.SearchButtonListener;



public class SearchButton extends JButton {
  private static final String SEARCH_BUTTON_TEXT = "Search"; // button name
  private static final String SRCH_TINY_PNG = "srchTiny.png"; // file name of search image

  /**
   * Constructor for SearchButton object, it takes in the String that user is interested in
   * searching and also the method(by address or by keyword) they want to search for the wiki notes.
   * When instantiating, the class will first try to load a search image, and if the image cannot be
   * found, it will return a String for the button instead.
   * 
   * @param: selected_method, String containing the String representing the method of search
   * @param: input, JTextField containing the String of content to search
   */
  public SearchButton(JTextField input, JComboBox<String> selected_method) {
    this.setToolTipText(SEARCH_BUTTON_TEXT);
    try {
      Image search = ImageIO.read(getClass().getResource(SRCH_TINY_PNG));
      this.setIcon(new ImageIcon(search));
    } catch (IllegalArgumentException | IOException ex) {
      this.setText(SEARCH_BUTTON_TEXT);
    }
    this.addActionListener(new SearchButtonListener(input, selected_method));
  }
}
