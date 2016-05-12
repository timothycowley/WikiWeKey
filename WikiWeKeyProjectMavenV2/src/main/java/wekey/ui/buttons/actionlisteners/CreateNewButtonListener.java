package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import wekey.ui.pages.EditPage;
import wekey.ui.pages.UIFrame;

public class CreateNewButtonListener implements ActionListener {

  /**
   * Upon clicking on it, the UIFrame opens up a new wiki page for creation
   * 
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    UIFrame mainUI = UIFrame.getInstance();
    mainUI.updateHistoryNDisplay(new EditPage());
  }



}
