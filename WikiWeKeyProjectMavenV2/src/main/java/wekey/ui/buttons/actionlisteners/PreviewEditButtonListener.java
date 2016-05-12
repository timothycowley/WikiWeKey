package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

import wekey.ui.pages.EditPage;
import wekey.ui.pages.UIFrame;

public class PreviewEditButtonListener implements ActionListener {

  private JEditorPane pane;
  private JTextField file_name_field;

  /**
   * constructor to create PreviewEditButtonListener object.
   * 
   * @param: currentFileName, JTextField, containing the current file name of the wiki page
   * @param: contentPane, JEditorPane containing the text of the text of the wiki page
   */
  public PreviewEditButtonListener(JEditorPane pane, JTextField file_name_field) {
    this.pane = pane;
    this.file_name_field = file_name_field;
  }

  /**
   * upon clicking it UIFrame turns from preview mode to edit mode to allow client to further make
   * changes to the content of the wiki page and its file name
   * 
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    UIFrame mainUI = UIFrame.getInstance();
    mainUI.updateHistoryNDisplay(new EditPage(pane, file_name_field));
  }

}
