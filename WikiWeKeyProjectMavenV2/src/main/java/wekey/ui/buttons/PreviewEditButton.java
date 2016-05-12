package wekey.ui.buttons;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

import wekey.ui.buttons.actionlisteners.PreviewEditButtonListener;

public class PreviewEditButton extends JButton {
  private static final String PREVIEWEDITBUTTONTIP = "Click to edit the current content"; // tip for
                                                                                          // the
                                                                                          // button
  private static final String PREVIEWEDITBUTTONTEXT = "Edit"; // button name

  /**
   * constructor to create PreviewEditButton object. upon clicking it UIFrame turns from preview
   * mode to edit mode to allow client to further make changes to the content of the wiki page and
   * its file name
   * 
   * @param: currentFileName, JTextField, containing the current file name of the wiki page
   * @param: contentPane, JEditorPane containing the text of the text of the wiki page
   */
  public PreviewEditButton(JEditorPane contentPane, JTextField currentFileName) {
    this.setText(PREVIEWEDITBUTTONTEXT);
    this.setToolTipText(PREVIEWEDITBUTTONTIP);
    this.addActionListener(new PreviewEditButtonListener(contentPane, currentFileName));
  }
}
