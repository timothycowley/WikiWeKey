package wekey.ui.buttons;

import java.io.File;

import javax.swing.JButton;

import wekey.ui.buttons.actionlisteners.EditButtonListener;

public class EditButton extends JButton {
  private static final String EDITBUTTONTEXT = "Edit"; // button name
  private static final String EDITBUTTONTIP = "Click to edit the current wiki page"; // tip
                                                                                     // for
                                                                                     // the
                                                                                     // button

  /**
   * constructor to instantiate an EditButton object. upon clicking on it, the currently viewed wiki
   * page is turned into editable mode and its file name is allowed for changes as well
   * 
   * @param: file, File object specifying the directory of the file being viewed and to be changed
   * 
   *         <pre>
   * file.exists() is true
   * </pre>
   */
  public EditButton(File file) {
    this.setText(EDITBUTTONTEXT);
    this.setToolTipText(EDITBUTTONTIP);
    this.addActionListener(new EditButtonListener(file));
  }
}
