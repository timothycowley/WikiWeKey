package wekey.ui.buttons;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

import wekey.ui.buttons.actionlisteners.PreviewButtonListener;

public class PreviewButton extends JButton {
  private static final String PREVIEWBUTTONTEXT = "Preview";
  private static final String PREVIEWBUTTONTIP = "Click to preview the current wiki page";

  /**
   * constructor to create PreviewButton object. upon clicking it UIFrame turns from edit mode to
   * render mode, showing the interpreted version of the wikipage
   * 
   * @param: oldFileName, a String specifying the name of the file when just before editing
   * @param: file_name_field, JTextField, containing the current file name to be saved, may or may
   *         not be the same as the oldFileNameoteote
   * @param: textArea, JEditorPane, containing the text of the interpreted text of the wiki page
   */
  public PreviewButton(String oldFileName, JTextField file_name_field, JEditorPane textArea) {
    this.setText(PREVIEWBUTTONTEXT);
    this.setToolTipText(PREVIEWBUTTONTIP);
    this.addActionListener(new PreviewButtonListener(oldFileName, file_name_field, textArea));
  }
}
