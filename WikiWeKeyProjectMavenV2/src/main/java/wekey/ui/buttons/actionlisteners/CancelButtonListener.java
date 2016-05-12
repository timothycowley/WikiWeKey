package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import wekey.ui.pages.UIFrame;

public class CancelButtonListener implements ActionListener {

  /**
   * When clicked the UIFrame returns to the previously available page (ie any pages except for
   * EditPage and PreviewPage)
   * 
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    UIFrame mainUI = UIFrame.getInstance();
    mainUI.goPreviousPage();
  }

}
