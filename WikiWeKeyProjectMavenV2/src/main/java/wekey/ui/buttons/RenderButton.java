package wekey.ui.buttons;

import javax.swing.JButton;

import wekey.ui.buttons.actionlisteners.CancelButtonListener;


/**
 * This class creates a RenderButton instance upon clicking on it, the the UIFrame turns from
 * ViewRawPage to ViewRenderedPage, showing wiki text after interpretation
 */

public class RenderButton extends JButton {
  private static final String VIEWRENDEREDPAGETEXT = "Render"; // button name
  private static final String VIEWRENDEREDPAGETIP =
      "Click to render the current wiki page in interpreted view"; // tip for the button

  /**
   * constructor to create a RenderButton instance upon clicking on it, the the UIFrame turns from
   * ViewRawPage to ViewRenderedPage, showing wiki text after interpretation
   * 
   * @param: file, File object specifying the path of the wiki page to be rendered
   */
  public RenderButton() {
    this.setText(VIEWRENDEREDPAGETEXT);
    this.setToolTipText(VIEWRENDEREDPAGETIP);
    this.addActionListener(new CancelButtonListener());
  }
}
