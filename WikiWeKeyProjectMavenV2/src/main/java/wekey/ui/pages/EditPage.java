package wekey.ui.pages;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import wekey.ui.buttons.CancelButton;
import wekey.ui.buttons.CreateNewButton;
import wekey.ui.buttons.PreviewButton;
import wekey.ui.buttons.SaveButton;
import wekey.ui.entities.Constants;


public class EditPage implements Page {

  private static final String ERROR_LOADING_WIKINOTE_TEXT =
      "Error: program failed to load the Wiki Note"; // error message when failed to load content
  private static final String ERROR_FINDING_WIKINOTE =
      "Error: program failed to find the Wiki Note"; // error message when failed to find the file
  private static final String EDIT_PAGE = "Edit Page"; // page name of the current EditPage
  private static final String LEFT_ALIGNMET_CONSTRAIN_CELL = "alignx left"; // constrain for element
  // in a single MigLayout
  // cell
  private static final String FILENAME_FIELD_TEXT = "New File Name"; // initial text for
  // file_name_field
  private static final int FILENAME_FIELD_LENGTH = 10; // default length for file_name_field

  private File file;
  private JEditorPane newTextArea;
  private JEditorPane textArea;
  private String oldFileName = Constants.EMPTY_STRING;
  private JTextField currentFileName;

  /**
   * constructor for EditPage object. This constructor is used when editing a currently existing
   * file. The file is then passed as its argument to instantiate this object.
   *
   * @param: file, a File object that specifies the directory of the file to be edited
   */
  public EditPage(final File file) {
    this.file = file;
    final UIFrame mainUI = UIFrame.getInstance();
    final Path targetFolder = mainUI.getWikiFilePath();
    final Path filePath = file.toPath();
    this.oldFileName =
        targetFolder.relativize(filePath).toString()
        .replaceFirst(Constants.EXTENSION_EXPRESSION, Constants.EMPTY_STRING);
  }

  /**
   * constructor for EditPage object. This constructor is used when the client click on the Edit
   * button on the PreviewPage to further edit the content
   *
   * @param: pane, JEditorPane containing the current text of the wiki page
   * @param: oldFileName, JTextField containing the file name carried over from the preview page
   */
  public EditPage(final JEditorPane pane, final JTextField oldFileName) {
    this.newTextArea = pane;
    this.oldFileName = oldFileName.getText();
  }

  /**
   * constructor for EditPage object. This constructor is used when creating a new file, no input
   * argument is required.
   */
  public EditPage() {}

  /**
   * thie method returns a JPanel that contains a scrollable and editable JEditorPane, allowing user
   * to edit the text content in within and also a JTextField for user to type in the file name
   * intended to be saved as
   *
   * @return a JPanel that contains an area for displaying and editing text and the textField
   *         containing the current file name
   */
  @Override
  public JPanel getPageBody() {
    final JPanel body = new JPanel();
    body.setLayout(new MigLayout(Constants.NO_CONSTRAIN, Constants.GROW_CONSTRAIN,
        Constants.EMPTY_CONSTRAIN + Constants.EMPTY_CONSTRAIN + Constants.GROW_CONSTRAIN));

    body.add(getFileNameLabel(), Constants.CELL_0_0_POSITION);

    setDefaultFileNameField();
    body.add(this.currentFileName, Constants.CELL_0_1_POSITION + Constants.COMMA
        + LEFT_ALIGNMET_CONSTRAIN_CELL);

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
    final JLabel file_name_label = new JLabel(Constants.FILE_NAME_LABEL);
    file_name_label.setHorizontalAlignment(SwingConstants.LEFT);
    return file_name_label;
  }

  /**
   * this method set the file_name_field as default setting (when editPage is called when creating a
   * new wiki note)
   *
   * @post the file_name_field is instantiated as the default setting for new wiki note creation
   */
  private void setDefaultFileNameField() {
    this.currentFileName = new JTextField();
    this.currentFileName.setText(FILENAME_FIELD_TEXT);
    this.currentFileName.setColumns(FILENAME_FIELD_LENGTH);
  }

  /**
   * this method returns a Scrollable JEditorPane containing the content of the wiki note allowed to
   * edit
   *
   * @return a JScrollPane containing a JEditorPane which contains wiki note to edit
   */
  private JScrollPane getContentInScrollablePane() {
    final JScrollPane scrollPane = new JScrollPane();
    this.textArea = new JEditorPane();
    scrollPane.setViewportView(this.textArea);

    // when EditPage object is instantiated from PreviewPage
    if (isFromPreviewPage()) {
      setContentFromPreview();
    }

    // when EditPage object is instantiated from editing an exiting wiki note from search results
    if (isFromViewPage()) {
      setContentFromView();
    }
    return scrollPane;
  }

  /**
   * this method check if this EditPage is instantiated from PreviewPage
   *
   * @return true if this EditPage is instantiated from PreviewPage, false otherwise
   */
  private boolean isFromPreviewPage() {
    return this.newTextArea != null;
  }

  /**
   * this method check if this EditPage is instantiated from ViewRawPage or ViewRenderedPage
   *
   * @return true if this EditPage is instantiated from ViewRawPage or ViewRenderedPage, false
   *         otherwise
   */
  private boolean isFromViewPage() {
    return this.file != null;
  }

  /**
   * this method set the content of the JEditorPane with the content from PreviewPage <post> the
   * textArea, JEditorPane, has its content set as the content from the PreviewPage
   */
  private void setContentFromPreview() {
    this.currentFileName.setText(this.oldFileName);
    this.textArea.setText(this.newTextArea.getText());
  }

  /**
   * this method set the content of the JEditorPane with the content from the ViewRawPage or
   * ViewRenderedPage <post>the textArea, JEditorPane, has its content set as the content from the
   * ViewRawPage or ViewRenderedPage
   *
   * @throws IOException when BufferedReader fails to read the file's content
   * @throws FileNotFoundException when FileReader fails to find the file
   */
  private void setContentFromView() {
    final UIFrame mainUI = UIFrame.getInstance();
    this.currentFileName.setText(this.oldFileName);
    FileReader reader;
    try {
      reader = new FileReader(this.file);
      final BufferedReader buffer = new BufferedReader(reader);
      try {
        this.textArea.read(buffer, null);
        buffer.close();
      } catch (final IOException e) {
        mainUI.displayMessage(ERROR_LOADING_WIKINOTE_TEXT);
        e.printStackTrace(); // > use logger
      }
    } catch (final FileNotFoundException e1) {
      mainUI.displayMessage(ERROR_FINDING_WIKINOTE);
      e1.printStackTrace(); // use logger
    }
  }

  /**
   * this method returns a JPanel that contains a CreateNewButton, a SaveButton, a PreviewButton and
   * a CancelButton
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

    final SaveButton saveButton =
        new SaveButton(this.oldFileName, this.currentFileName, this.textArea);
    bottom.add(saveButton);

    final PreviewButton previewButton =
        new PreviewButton(this.oldFileName, this.currentFileName, this.textArea);
    bottom.add(previewButton);

    final CancelButton cancelButton = new CancelButton();
    bottom.add(cancelButton);

    return bottom;
  }

  /**
   * @return the String based on page type
   */
  @Override
  public String toString() {
    return EDIT_PAGE;
  }

}
