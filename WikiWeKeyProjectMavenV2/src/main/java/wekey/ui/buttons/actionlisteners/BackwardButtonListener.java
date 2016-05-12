package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import wekey.ui.pages.UIFrame;

public class BackwardButtonListener implements ActionListener {

  /**
   * this method allow user to go back to the previously visited page when triggered
   *
   * @param: event, ActionEvent when mouse button down is true
   *
   *
   *         //> @param: is illegal. The code has a lot of instance of this mistake and Javadoc
   *         generation generates errors
   * 
   *         //> -2
   */
  @Override
  public void actionPerformed(final ActionEvent event) {
    final UIFrame mainFrame = UIFrame.getInstance();
    mainFrame.goPreviousPage();
  }
}
