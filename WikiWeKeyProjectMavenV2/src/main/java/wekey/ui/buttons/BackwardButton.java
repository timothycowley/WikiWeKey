package wekey.ui.buttons;

import javax.swing.JButton;

import wekey.ui.buttons.actionlisteners.BackwardButtonListener;
import wekey.ui.pages.UIFrame;

public class BackwardButton extends JButton {
  private static final String BACKLABEL = "<"; // symbol for the button on UI
  private static final String PREVIOUS_BUTTON_TIP_TEXT = "previous page"; // tip
                                                                          // for
                                                                          // the
                                                                          // button

  /**
   * Constructor to create the backward button to allow user to go back to the previously visited
   * page. button is disabled when there is no previous page
   * 
   * <pre>
   * pre: PageHistory, history, has been instantiated
   * </pre>
   * 
   * @return a JButton allowing user to go back to the previous wiki page
   */
  public BackwardButton() {
    this.setText(BACKLABEL);
    this.setToolTipText(PREVIOUS_BUTTON_TIP_TEXT);
    UIFrame mainUI = UIFrame.getInstance();
    if (!mainUI.hasPreviousPage()) {
      setEnabled(false);
    }
    this.addActionListener(new BackwardButtonListener());
  }
}
