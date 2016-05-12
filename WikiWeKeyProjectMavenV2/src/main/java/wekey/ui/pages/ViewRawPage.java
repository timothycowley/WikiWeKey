package wekey.ui.pages;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import wekey.ui.buttons.CreateNewButton;
import wekey.ui.buttons.EditButton;
import wekey.ui.buttons.RenderButton;
import wekey.ui.entities.Constants;

public class ViewRawPage implements Page {
  private static final String VIEW_RAW_PAGE = "View Raw Page"; // name of ViewRawPage
  private static final String ERROR_LOADING_WIKINOTE_TEXT =
      "Error: program failed to load the Wiki Note"; // error message when failed to load content
  private static final String ERROR_FINDING_WIKINOTE =
      "Error: program failed to find the Wiki Note"; // error message when failed to find the file


  private File file;

  /**
   * constructor to instantiate the ViewRawPage object, it takes in a file and displays its raw
   * content in its body JPanel
   * 
   * @param: file, a file object specifying the directory of the file to be read in
   * 
   *         <pre>
   * file.exists is true
   * </pre>
   */
  public ViewRawPage(File file) {
    this.file = file;
  }

  /**
   * Returns a JPanel that contains a scrollable JEditorPane to display the raw text content of a
   * file
   * 
   * <pre>
   * None
   * </pre>
   * 
   * @return a JPanel that contains an area for displaying the raw content of the file read in
   */
  @Override
  public JPanel getPageBody() {
    JPanel body = new JPanel();
    body.setLayout(new MigLayout(Constants.NO_CONSTRAIN, Constants.GROW_CONSTRAIN,
        Constants.EMPTY_CONSTRAIN + Constants.GROW_CONSTRAIN));

    body.add(getFileNameLabel());

    body.add(getContentInScrollablePane(), Constants.CELL_0_1_POSITION + Constants.COMMA
        + Constants.GROW_CONSTRAIN_CELL);

    return body;
  }

  /**
   * this method returns JLabel containing the name of the file specified
   * 
   * @return a JLabel showing the name of the file chosen
   */
  private JLabel getFileNameLabel() {
    JLabel file_name_label =
        new JLabel(Constants.FILE_NAME_LABEL
            + file.getName().replaceFirst(Constants.EXTENSION_EXPRESSION, Constants.EMPTY_STRING));
    file_name_label.setHorizontalAlignment(SwingConstants.LEFT);
    return file_name_label;
  }

  /**
   * this method returns a Scrollable JEditorPane containing the content of the wiki note specified.
   * Content of the wiki note is in raw wiki markdown language.
   * 
   * @return a JScrollPane containing a JEditorPane which contains raw wiki mark-down content of the
   *         file
   */
  private JScrollPane getContentInScrollablePane() {
    JScrollPane scrollPane = new JScrollPane();
    JEditorPane textArea = new JEditorPane();

    textArea.setEditable(false);
    scrollPane.setViewportView(textArea);
    loadAndDisplayFile(textArea);
    return scrollPane;
  }

  /**
   * This method tries to load and display the raw wiki markdown language on JEditorPane
   * 
   * <pre>
   * textArea != null
   * </pre>
   * 
   * <post> the content of textArea, JEditorPane, is set to be the content of the wiki note in raw
   * wiki markdown language
   * 
   * @param textArea, JEditorPane where the content of the wiki note goes
   * @throws IOException when BufferedReader fails to read the file's content
   * @throws FileNotFoundException when FileReader fails to find the file
   */
  private void loadAndDisplayFile(JEditorPane textArea) {
    UIFrame mainUI = UIFrame.getInstance();
    FileReader reader;
    try {
      reader = new FileReader(file);
      BufferedReader buffer = new BufferedReader(reader);
      try {
        textArea.read(buffer, null);
        buffer.close();
      } catch (IOException e) {
        mainUI.displayMessage(ERROR_LOADING_WIKINOTE_TEXT);
        e.printStackTrace();
      }

    } catch (FileNotFoundException e1) {
      mainUI.displayMessage(ERROR_FINDING_WIKINOTE);
      e1.printStackTrace();
    }
  }

  /**
   * Returns a JPanel that contains a CreateNewButton, a SaveButton, a EditButton and a
   * RenderlButton
   * 
   * @return a JPanel that contains multiple buttons for different tasks
   */
  @Override
  public JPanel getPageBotttom() {
    JPanel bottom = new JPanel();
    FlowLayout fl_buttom = (FlowLayout) bottom.getLayout();
    fl_buttom.setAlignment(FlowLayout.LEFT);

    CreateNewButton createNewButton = new CreateNewButton();
    bottom.add(createNewButton);

    EditButton editButton = new EditButton(file);
    bottom.add(editButton);

    RenderButton renderButton = new RenderButton();
    bottom.add(renderButton);

    return bottom;
  }

  /**
   * @return the String based on page type
   */
  @Override
  public String toString() {
    return VIEW_RAW_PAGE;
  }
}
