package wekey.ui.pages;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import wekey.search.Search;
import wekey.ui.buttons.CreateNewButton;
import wekey.ui.buttons.actionlisteners.SearchHyperLinkedListener;
import wekey.ui.entities.Constants;

public class SearchResultPage implements Page {


  private static final String NEW_LINE_STRING = "\n"; // new line String
  private static final String BR_TAG = "<br>"; // br tag
  private static final String CLOSE_OF_HYPERLINK_TAG = "</a>"; // close of hyperlink tag
  private static final String CLOSE_HREF = "\">"; // close of href tag
  private static final String HYPERLINK_HREF = "href=\""; // open of href tag
  private static final String START_OF_HYPERLINK = "<a "; // open of hyperlink tag
  private static final String CLOSED_HTML_TAG = "</html>"; // closed html tag
  private static final String CLOSED_BODY_TAG = "</body>"; // closed body tag
  private static final String CLOSED_HEADER_TAG = "</h1>"; // closed header tag
  private static final String BODY_TAG = "<body>"; // body tag
  private static final String HTML_TAG = "<html>"; // html tag
  private static final String HEADER_TAG = "<h1>"; // header tag
  private static final int RESULTAREAHEIGHT = 80; // initial search result area height
  private static final int RESULTAREAWIDTH = 106; // initial search result area width
  private static final String SEARCH_RESULT_PAGE = "Search Result Page"; // name of the
                                                                         // SearchResultPage
  final private static String SEARCH_HEADER = "Search Results for: "; // search header message
  final private static String NORESULT = "<html><br><br><center>" // no-result-found message
      + "<h1>Matching Wiki Page Does Not Exist</h1><h1>try "
      + "a new search or create a new wiki page</h1></center>" + CLOSED_HTML_TAG;
  private static final String ADDRESS = "Address"; // search method type: address
  private static final String KEYWORD = "Keyword"; // search method type: keyword

  private String selected_method;
  private String search_input;

  /**
   * constructor for SearchResultPage Object which takes in a list of search results and display
   * them in the body JPanel
   * 
   * @param: selected_method, a String representing the method of search
   * @param: input, a String of content to search
   */
  public SearchResultPage(String selected_method, String input) {
    this.selected_method = selected_method;
    this.search_input = input;
  }

  /**
   * Returns a JPanel that displays the matching wiki page results based on the search, return
   * no-result-found message when list of results is empty
   * 
   * @return a JPanel that displays the matching wiki page results based on the search
   */
  @Override
  public JPanel getPageBody() {
    JPanel body = new JPanel();
    body.setLayout(new MigLayout(Constants.NO_CONSTRAIN, Constants.GROW_CONSTRAIN,
        Constants.EMPTY_CONSTRAIN + Constants.GROW_CONSTRAIN));

    JScrollPane scrollPane = new JScrollPane();
    body.add(scrollPane, Constants.CELL_0_1_POSITION + Constants.COMMA
        + Constants.GROW_CONSTRAIN_CELL);

    JEditorPane textArea = new JEditorPane();
    textArea.setContentType(Constants.PANE_DISPLAY_FORMAT);
    textArea.setEditable(false);

    List<File> searchResults = getResultList();

    if (searchResults.isEmpty())
      textArea.setText(NORESULT);
    else
      textArea.setText(renderLinks(searchResults));
    textArea.setPreferredSize(new Dimension(RESULTAREAWIDTH, RESULTAREAHEIGHT));
    textArea.addHyperlinkListener(new SearchHyperLinkedListener());
    scrollPane.setViewportView(textArea);

    return body;
  }

  /**
   * this method takes in a list of String results and renders them in a HTML block in String
   * 
   * @returns the list of String results in proper HTML format
   */
  public String renderLinks(List<File> list) {
    StringBuilder sb = new StringBuilder();
    sb.append(HTML_TAG + BODY_TAG + HEADER_TAG + SEARCH_HEADER + search_input + CLOSED_HEADER_TAG);
    for (File address : list) {
      UIFrame mainUI = UIFrame.getInstance();
      Path targetFolder = mainUI.getWikiFilePath();
      Path filePath = address.toPath();
      sb.append(START_OF_HYPERLINK + HYPERLINK_HREF + address.getAbsolutePath() + CLOSE_HREF
          + targetFolder.relativize(filePath) + CLOSE_OF_HYPERLINK_TAG + BR_TAG + NEW_LINE_STRING);
    }
    sb.append(CLOSED_BODY_TAG + CLOSED_HTML_TAG);
    return sb.toString();
  }

  /**
   * Returns a JPanel that contains a CreateNewButton
   * 
   * <pre>
   * None
   * </pre>
   * 
   * @return a JPanel that contains only one create new button
   */
  @Override
  public JPanel getPageBotttom() {
    JPanel bottom = new JPanel();
    FlowLayout fl_buttom = (FlowLayout) bottom.getLayout();
    fl_buttom.setAlignment(FlowLayout.LEFT);

    CreateNewButton createNewButton = new CreateNewButton();
    bottom.add(createNewButton);

    return bottom;
  }

  /**
   * @return the String based on page type
   */
  @Override
  public String toString() {
    return SEARCH_RESULT_PAGE;
  }

  /**
   * this method helps to get the results of the search based on the search methods, by keyword or
   * by address.
   * 
   * <pre>
   * selected_method != null
   * </pre>
   * 
   * @return return a list of files based on the searching query.
   */
  private List<File> getResultList() {
    List<File> searchResults = new ArrayList<File>();
    if (selected_method.equals(KEYWORD)) {
      Search search = new Search();
      searchResults = search.getMatchingFiles(search_input);
    } else if (selected_method.equals(ADDRESS)) {
      File appointedFile = new File(search_input);
      if (appointedFile.exists()) {
        searchResults.add(appointedFile);
      }
    }
    return searchResults;
  }
}
