package wekey.ui.buttons;

import javax.swing.JButton;

import wekey.ui.buttons.actionlisteners.CreateNewButtonListener;

public class CreateNewButton extends JButton {
  private static final String CREATENEWBUTTONTEXT = "Create New"; // button
                                                                  // name
  private static final String CREATENEWBUTTONTIP = "Click to create a new wiki page"; // tip
                                                                                      // for
                                                                                      // the
                                                                                      // button

  /**
   * constructor to instantiate a CreateNewButton object.
   * 
   * <pre>
   * post: Upon clicking on it, the UIFrame opens up a new wiki page for creation.
   * </pre>
   */
  public CreateNewButton() {
    this.setText(CREATENEWBUTTONTEXT);
    this.setToolTipText(CREATENEWBUTTONTIP);
    this.addActionListener(new CreateNewButtonListener());
  }
}
