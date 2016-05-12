package wekey.ui.buttons;

import javax.swing.JButton;

import wekey.ui.buttons.actionlisteners.ForwardButtonListener;
import wekey.ui.pages.UIFrame;

public class ForwardButton extends JButton {
  private static final String FORWARDLABEL = ">"; // button name
  private static final String NEXT_BUTTON_TIP_TEXT = "next page"; // tip for the button

  /**
   * constructor to create a ForwardButton Object which allows user to go to the next page of the
   * current page button is disable when there is no next page
   * 
   * <pre>
   * PageHistory, history, has been instantiated
   * </pre>
   */
  public ForwardButton() {
    this.setText(FORWARDLABEL);
    this.setToolTipText(NEXT_BUTTON_TIP_TEXT);
    UIFrame mainUI = UIFrame.getInstance();
    if (!mainUI.hasNextPage()) {
      setEnabled(false);
    }
    this.addActionListener(new ForwardButtonListener());

  }
}
