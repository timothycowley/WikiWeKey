package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

import wekey.ui.pages.PreviewPage;
import wekey.ui.pages.UIFrame;


public class PreviewButtonListener implements ActionListener {


  private String oldFileName;
  private JTextField file_name_field;
  private JEditorPane textArea;

  /**
   * constructor to create PreivewButtonListener object.
   * 
   * @param: oldFileName, a String specifying the name of the file when just before editing
   * @param: currentFileName, JTextField, containing the current file name to be saved, may or may
   *         not be the same as the oldFileNameoteote
   * @param: textArea, JEditorPane, containing the text of the interpreted text of the wiki page
   */
  public PreviewButtonListener(String oldFileName, JTextField currentFileName, JEditorPane textArea) {
    this.oldFileName = oldFileName;
    this.file_name_field = currentFileName;
    this.textArea = textArea;
  }

  /**
   * upon clicking it UIFrame turns from edit mode to render mode, showing the interpreted version
   * of the current content of the wiki page
   * 
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    UIFrame mainUI = UIFrame.getInstance();
    mainUI.updateHistoryNDisplay(new PreviewPage(textArea, oldFileName, file_name_field));
  }

}
