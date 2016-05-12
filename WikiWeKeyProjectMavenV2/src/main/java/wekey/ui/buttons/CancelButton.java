package wekey.ui.buttons;

import javax.swing.JButton;

import wekey.ui.buttons.actionlisteners.CancelButtonListener;

public class CancelButton extends JButton {
  private static final String CANCELBUTTONTEXT = "Cancel"; // button name
  private static final String CANCELBUTTONTIP = "Click to cancel edit"; // tip
                                                                        // for
                                                                        // the
                                                                        // button

  /**
   * constructor to create a CancelButton Object When clicked the UIFrame returns to the previously
   * available page (any pages except for EditPage and PreviewPage)
   */
  public CancelButton() {
    this.setText(CANCELBUTTONTEXT);
    this.setToolTipText(CANCELBUTTONTIP);
    this.addActionListener(new CancelButtonListener());
  }
}
