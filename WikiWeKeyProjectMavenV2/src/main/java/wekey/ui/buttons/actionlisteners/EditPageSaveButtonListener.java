package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

import wekey.file.storage.FileStorage;

public class EditPageSaveButtonListener implements ActionListener {
  private final String oldFileName;
  private final JTextField file_name_field;
  private final JEditorPane textArea;

  /**
   * constructor for EditPageSaveButtonListener object, it takes in oldFileName and newFileName so
   * that it can check the validity of the file name to be saved as.
   *
   * @param: oldFileName, String the name of the file before editing
   * @param: newFileName, JTextFied containing the currently file name, may or may not be the same
   *         the as the oldFileName
   * @param: textArea, JEditorPane containing the content of the wiki page intended to be saved in
   */
  public EditPageSaveButtonListener(final String oldFileName, final JTextField newFileName,
      final JEditorPane textArea) {
    this.oldFileName = oldFileName;
    this.file_name_field = newFileName;
    this.textArea = textArea;
  }

  /**
   * upon clicking on it, the current wiki content is saved in a text file under the name specified
   * by the string in file_name_field
   *
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(final ActionEvent event) {
    final FileStorage testSave = new FileStorage();
    try {
      testSave.storeText(this.oldFileName, this.file_name_field.getText(), this.textArea);
    } catch (final Exception e) {
      e.printStackTrace(); // > this is a catch all that prints to standard out.

      // > -1
    }
  }

}
