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
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import wekey.interpreter.MarkdownInterpreter;
import wekey.ui.buttons.CreateNewButton;
import wekey.ui.buttons.EditButton;
import wekey.ui.buttons.RawButton;
import wekey.ui.buttons.actionlisteners.SearchHyperLinkedListener;
import wekey.ui.entities.Constants;

public class ViewRenderedPage implements Page {
  private static final String RETURN = "\r\n"; // string to create a new line
  private static final String ERROR_LOADING_WIKINOTE_TEXT =
      "Error: program failed to load the Wiki Note"; // error message when failed to load content
  private static final String ERROR_FINDING_WIKINOTE =
      "Error: program failed to find the Wiki Note"; // error message when failed to find the file
  private static final String VIEW_RENDERED_PAGE = "View Rendered Page"; // name of ViewRenderedPage

  private File file;

  /**
   * constructor to instantiate the ViewRenderedPage object, it takes in a file and displays its
   * interpreted content in its body JPanel (HTML format)
   * 
   * @param: file, a file object specifying the directory of the file to be read in
   * 
   *         <pre>
   * file.exists is true
   * </pre>
   */
  public ViewRenderedPage(File file) {
    this.file = file;
  }


  /**
   * Returns a JPanel that contains a scrollable JEditorPane to display text content of a file after
   * interpretation
   * 
   * <pre>
   * None
   * </pre>
   * 
   * @return a JPanel that contains an area for displaying interpreted text
   */
  @Override
  public JPanel getPageBody() {
    UIFrame mainUI = UIFrame.getInstance();
    JPanel body = new JPanel();
    body.setLayout(new MigLayout(Constants.NO_CONSTRAIN, Constants.GROW_CONSTRAIN,
        Constants.EMPTY_CONSTRAIN + Constants.GROW_CONSTRAIN));

    body.add(getFileNameLabel(mainUI));

    body.add(getContentInScrollablePane(mainUI), Constants.CELL_0_1_POSITION + Constants.COMMA
        + Constants.GROW_CONSTRAIN_CELL);

    return body;
  }

  /**
   * this method returns JLabel containing the name of the file specified
   * 
   * @param mainUI, the UIFrame of the wikiwekey program
   * @return a JLabel showing the name of the file chosen
   */
  private JLabel getFileNameLabel(UIFrame mainUI) {
    Path targetFolder = mainUI.getWikiFilePath();
    Path filePath = file.toPath();
    JLabel file_name_label =
        new JLabel(Constants.FILE_NAME_LABEL + targetFolder.relativize(filePath).toString());
    file_name_label.setHorizontalAlignment(SwingConstants.LEFT);
    return file_name_label;
  }

  /**
   * this method returns a Scrollable JEditorPane containing the content of the wiki note specified.
   * Content of the wiki note is in HTML format.
   * 
   * <pre>
   * mainUI != null
   * </pre>
   * 
   * @param mainUI, the UIFrame of the wikiwekey program
   * @return a JScrollPane containing a JEditorPane which contains interpreted wiki mark-down
   *         content of the file
   * @throws IOException when BufferedReader fails to read the file's content
   * @throws FileNotFoundException when FileReader fails to find the file
   */
  private JScrollPane getContentInScrollablePane(UIFrame mainUI) {
    JScrollPane scrollPane = new JScrollPane();
    JEditorPane textArea = new JTextPane();
    textArea.setContentType(Constants.PANE_DISPLAY_FORMAT);

    loadAndInterpretFile(textArea, mainUI);
    textArea.setEditable(false);
    textArea.addHyperlinkListener(new SearchHyperLinkedListener());
    scrollPane.setViewportView(textArea);

    return scrollPane;
  }

  /**
   * This method tries to load and interpret the wiki markdown language into HTML
   * 
   * <pre>
   * textArea != null
   * </pre>
   * 
   * <pre>
   * mainUI != null
   * </pre>
   * 
   * <post> the content of textArea, JEditorPane, is set to be the content of the wiki note in HTML
   * format
   * 
   * @param textArea, JEditorPane where the content of the wiki note goes
   * @param mainUI, the UIFrame of the wikiwekey program
   * @throws IOException when BufferedReader fails to read the file's content
   * @throws FileNotFoundException when FileReader fails to find the file
   */
  private void loadAndInterpretFile(JEditorPane textArea, UIFrame mainUI) {
    String fileContent = getFileTextOnLines(mainUI);
    MarkdownInterpreter interpreter = new MarkdownInterpreter();
    String interpretedContent = interpreter.convertToHTML(fileContent);
    textArea.setText(interpretedContent);
  }

  /**
   * This method tries to load the file and get the file's content line by line as a single block of
   * String
   * 
   * <pre>
   * mainUI != null
   * </pre>
   * 
   * @param mainUI, the UIFrame of the wikiwekey program
   * @return a String containing the content of the file specified following the original format
   *         line by line
   * @throws IOException when BufferedReader fails to read the file's content
   * @throws FileNotFoundException when FileReader fails to find the file
   */
  private String getFileTextOnLines(UIFrame mainUI) {
    FileReader reader;
    StringBuffer stringBuffer = new StringBuffer();
    try {
      reader = new FileReader(file);
      BufferedReader buffer = new BufferedReader(reader);
      String line = null;
      try {
        while ((line = buffer.readLine()) != null) {
          stringBuffer.append(line).append(RETURN);
        }
        buffer.close();
      } catch (IOException e) {
        mainUI.displayMessage(ERROR_LOADING_WIKINOTE_TEXT);
        e.printStackTrace();
      }

    } catch (FileNotFoundException e) {
      mainUI.displayMessage(ERROR_FINDING_WIKINOTE);
      e.printStackTrace();
    }
    return stringBuffer.toString();
  }

  /**
   * Returns a JPanel that contains a CreateNewButton, a SaveButton, a EditButton and a RawButton
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

    RawButton rawButton = new RawButton(file);
    bottom.add(rawButton);
    return bottom;
  }

  /**
   * @return the String based on page type
   */
  @Override
  public String toString() {
    return VIEW_RENDERED_PAGE;
  }
}
