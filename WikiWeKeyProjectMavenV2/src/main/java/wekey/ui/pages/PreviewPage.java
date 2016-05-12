package wekey.ui.pages;

import java.awt.FlowLayout;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import wekey.interpreter.MarkdownInterpreter;
import wekey.ui.buttons.CancelButton;
import wekey.ui.buttons.CreateNewButton;
import wekey.ui.buttons.PreviewEditButton;
import wekey.ui.buttons.SaveButton;
import wekey.ui.entities.Constants;


public class PreviewPage implements Page {
  private static final String PREVIEW_PAGE = "Preview Page"; // name of the previewPage

  private final String oldFileName;
  private final JTextField currentFileName;
  private final JEditorPane pane;


  /**
   * constructor for PreviewPage object, it takes in a file to read and display the content of the
   * file after interpretation (displaying in html format). It takes in the old name of the file and
   * the new name to be saved (may be the same as the old name) to determine if the file name is
   * valid for saving.
   *
   * @param: file, file object, specifying the path of the file to read in
   * @param: oldFileName, a String specifying the name of the file when just before editing
   * @param: file_name_field, JTextField, containing the current file name to be saved, may or may
   *         not be the same as the oldFileName
   * 
   *         //> no param for pane.
   *
   *         <pre>
   * file.exists() is true
   * </pre>
   *
   * @return a JPanel that contains an area for displaying content of the wikiPage also a JTextLabel
   *         displaying the name of the current file
   *
   *         //> constructor methods do not need a @return tag.
   */
  public PreviewPage(final JEditorPane pane, final String oldFileName,
      final JTextField file_name_field) {
    this.pane = pane;
    this.oldFileName = oldFileName;
    this.currentFileName = file_name_field;
  }

  /**
   * this method returns a JPanel that contains a scrollable JEditorPane to display text content of
   * a file after interpretation and also a JTextLabel specifying the name of the current file to be
   * saved as
   *
   * @return a JPanel that contains an area for displaying content of the wikiPage also a JTextLabel
   *         displaying the name of the current file
   */
  @Override
  public JPanel getPageBody() {
    final JPanel body = new JPanel();
    body.setLayout(new MigLayout(Constants.NO_CONSTRAIN, Constants.GROW_CONSTRAIN,
        Constants.EMPTY_CONSTRAIN + Constants.EMPTY_CONSTRAIN + Constants.GROW_CONSTRAIN));

    body.add(getFileNameLabel(), Constants.CELL_0_0_POSITION);

    body.add(getContentInScrollablePane(), Constants.CELL_0_2_POSITION + Constants.COMMA
        + Constants.GROW_CONSTRAIN_CELL);

    return body;
  }

  /**
   * this method returns JLabel containing the name of the file specified
   *
   * @return a JLabel showing the name of the file chosen
   */
  private JLabel getFileNameLabel() {
    final JLabel file_name_label =
        new JLabel(Constants.FILE_NAME_LABEL + this.currentFileName.getText());
    file_name_label.setHorizontalAlignment(SwingConstants.LEFT);
    return file_name_label;
  }

  /**
   * this method returns a Scrollable JEditorPane containing the content of the wiki note specified.
   * Content of the wiki note is in HTML format.
   *
   * @return a JScrollPane containing a JEditorPane which contains interpreted wiki mark-down
   *         content of the file
   */
  private JScrollPane getContentInScrollablePane() {
    final JScrollPane scrollPane = new JScrollPane();
    final MarkdownInterpreter interpreter = new MarkdownInterpreter();
    final String textContent = interpreter.convertToHTML(this.pane.getText());

    final JEditorPane newPane = new JEditorPane();
    newPane.setEditable(false);
    newPane.setContentType(Constants.PANE_DISPLAY_FORMAT);
    newPane.setText(textContent);
    scrollPane.setViewportView(newPane);

    return scrollPane;
  }

  /**
   * Returns a JPanel that contains a CreateNewButton, a SaveButton, a EditButton and a CancelButton
   *
   * @return a JPanel that contains multiple buttons for different tasks
   */
  @Override
  public JPanel getPageBotttom() {
    final JPanel bottom = new JPanel();
    final FlowLayout fl_buttom = (FlowLayout) bottom.getLayout();
    fl_buttom.setAlignment(FlowLayout.LEFT);

    final CreateNewButton createNewButton = new CreateNewButton();
    bottom.add(createNewButton);

    final SaveButton saveButton = new SaveButton(this.oldFileName, this.currentFileName, this.pane);
    bottom.add(saveButton);

    final PreviewEditButton editButton = new PreviewEditButton(this.pane, this.currentFileName);
    bottom.add(editButton);

    final CancelButton cancelButton = new CancelButton();
    bottom.add(cancelButton);

    return bottom;
  }

  /**
   * @return the String based on page type
   */
  @Override
  public String toString() {
    return PREVIEW_PAGE;
  }

}
