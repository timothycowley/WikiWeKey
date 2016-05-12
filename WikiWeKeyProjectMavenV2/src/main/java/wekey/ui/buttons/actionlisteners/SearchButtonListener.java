package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import wekey.ui.pages.SearchResultPage;
import wekey.ui.pages.UIFrame;

public class SearchButtonListener implements ActionListener {



  private JTextField input;
  private JComboBox<String> selected_method;

  /**
   * constructor for SearchButtonListener object, it takes in input containing the text to be
   * searched and selected_method which determines under what method the Search function is going to
   * use.
   * 
   * @param: input, JTextField containing the text to be searched in String
   * @param: selected_method, JComboBox containing the methods for search, now it can search either
   *         by Address or Keyword
   */
  public SearchButtonListener(JTextField input, JComboBox<String> selected_method) {
    this.input = input;
    this.selected_method = selected_method;
  }

  /**
   * upon clicking search starts, based on the method of searching, under "keyword" method it
   * searches any files that contain the text of the input. Under "Address" method, the search
   * simply returns the file located under the given address.
   * 
   * @param event, ActionEvent when hyperlink is interacted with
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    UIFrame mainUI = UIFrame.getInstance();
    String selected_method_string = (String) selected_method.getSelectedItem();
    mainUI.updateHistoryNDisplay(new SearchResultPage(selected_method_string, input.getText()));
  }

}
