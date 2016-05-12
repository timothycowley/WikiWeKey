package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import wekey.ui.pages.EditPage;
import wekey.ui.pages.UIFrame;

public class EditButtonListener implements ActionListener {

  private File file;

  /**
   * constructor the EditButtonListener object
   * 
   * @param: file, File object specifying the path of the wiki page to be edited
   */
  public EditButtonListener(File file) {
    this.file = file;
  }

  /**
   * upon clicking on it, the currently viewed wiki page is turned into editable mode and its file
   * name is allowed for changes as well
   * 
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    UIFrame mainUI = UIFrame.getInstance();
    mainUI.updateHistoryNDisplay(new EditPage(file));
  }

}
