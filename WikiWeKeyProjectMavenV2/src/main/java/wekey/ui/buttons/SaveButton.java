package wekey.ui.buttons;


import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

import wekey.ui.buttons.actionlisteners.EditPageSaveButtonListener;

/**
 * This class creates a SaveButton instance upon clicking on it, the currently editing wiki note is
 * saved under the file name specified. Note: - a file can be renamed to any file name that has not
 * been used in the directory. - one file cannot overwrite another file when giving the same file
 * name in the file name field.
 */

public class SaveButton extends JButton {
  private static final String SAVEBUTTONTEXT = "Save"; // button name
  private static final String SAVEBUTTONTIP = "Click to save the current wiki page"; // tip for the
                                                                                     // button

  /**
   * constructor for SaveButton, it takes in oldFileName and newFileName so that it can check the
   * validity of the file name to be saved as.
   * 
   * @param: oldFileName, String the name of the file before editing
   * @param: newFileName, JTextFied containing the currently file name, may or may not be the same
   *         the as the oldFileName
   * @param: textArea, JEditorPane containing the content of the wiki page intended to be saved in
   */
  public SaveButton(String oldFileName, JTextField newFileName, JEditorPane textArea) {
    this.setText(SAVEBUTTONTEXT);
    setToolTipText(SAVEBUTTONTIP);
    this.addActionListener(new EditPageSaveButtonListener(oldFileName, newFileName, textArea));
  }
}
