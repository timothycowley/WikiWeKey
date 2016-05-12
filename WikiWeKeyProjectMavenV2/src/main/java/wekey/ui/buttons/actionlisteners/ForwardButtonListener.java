package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import wekey.ui.pages.UIFrame;

public class ForwardButtonListener implements ActionListener {

  /**
   * this method allow user to the next page in respect to the current page in the PageHistory when
   * triggered
   * 
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    UIFrame mainFrame = UIFrame.getInstance();
    mainFrame.goNextPage();
  }
}
