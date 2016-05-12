package wekey.ui.buttons;

import java.io.File;

import javax.swing.JButton;

import wekey.ui.buttons.actionlisteners.RawButtonListener;

public class RawButton extends JButton {
  private static final String VIEWRAWPAGETEXT = "Raw"; // button name
  private static final String VIEWRAWPAGETIP = "Click to render the current wiki page in raw view"; // tip
  // for
  // the
  // button

  /**
   * constructor to create a RawButton instance upon clicking on it, the the UIFrame turns from
   * ViewInterpretedPage to ViewRawPage, showing original marked-up text of the wiki page
   *
   * @param: file, File object specifying the path of the wiki page to be rendered
   */
  public RawButton(File file) {
    this.setText(VIEWRAWPAGETEXT);
    this.setToolTipText(VIEWRAWPAGETIP);
    this.addActionListener(new RawButtonListener(file));
  }
}
