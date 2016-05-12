// > Javadoc generation has multiple errors:
// > -10
// > javadoc generation has mutliple warnings:
// > -10

package wekey.ui.pages;

import java.awt.Font;
import java.nio.file.Path;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import wekey.ui.buttons.BackwardButton;
import wekey.ui.buttons.ForwardButton;
import wekey.ui.buttons.SearchButton;
import wekey.ui.entities.Constants;
import wekey.ui.entities.PageHistory;
import wekey.ui.entities.PathManager;

public class UIFrame {
  private static final float _0_5F = 0.5f; // alignment gap
  private static final int INITIAL_X_POSITION = 100;
  // initial X position for JFrame
  private static final int INITIAL_Y_POSITION = 100;
  // initial X position for JFrame
  private static final int INITIAL_X_LENGTH = 700;
  // initial X size for JFrame
  private static final int INITIAL_Y_LENGTH = 700;
  // initial Y size for JFrame
  private final static String TYPINGTEXT = "type title or wiki path here";
  // tip for search field
  private static final String TOP_ALIGNMET_CONSTRAIN_CELL = "aligny top";
  // constrain for element in a single MigLayout cell
  private static final String GROWX_CONSTRAIN_CELL = "growx";
  // constrain for element in a single MigLayout cell
  private static final String TEXT_FONT = "Tahoma";
  // font used for components in JFrame
  private static final int TEXT_SIZE = 11;
  // Text size for components in JFrame
  private static final int SEARCH_FIELD_LENGTH = 10;
  // default length for search_field
  private final static String[] SEARCHBYLIST = {"Keyword", "Address"};
  // drop-down menu options
  private static final String SEARCHBY_LABEL_TEXT = "search by";
  // searchby_label text
  private static final String SEARCH_DROP_DOWN_TIP = "choose search by keyword or exact address";
  // tip for drop-down menu

  private JFrame frame;
  private static UIFrame mainUI;
  private final PageHistory history;
  private final PathManager pathManager;

  /**
   * entry point to run the UIFrame class
   */
  public static void main(final String[] args) {
    final UIFrame test = UIFrame.getInstance();
    test.setFrameVisible();
    test.updateHistoryNDisplay(new HomePage());
  }

  /**
   * singleton pattern to instantiate the UIFrame class.
   *
   * @return a single UIFrame object upon calling, if UIFrame already exists, returns the same
   *         UIFrame object
   */
  public static UIFrame getInstance() {
    if (mainUI == null) {
      mainUI = new UIFrame();
    }
    return mainUI;
  }

  /**
   * private UIFrame constructor to prevent user from calling it directly, user has to construct
   * UIFrame object through getInstance() method.
   */
  private UIFrame() {
    this.history = new PageHistory();
    this.pathManager = new PathManager();
    setUpJFrame();
  }

  /**
   * make the JFrame in the UIFrame object visible
   *
   * <pre>
   * JFrame is present
   * </pre>
   *
   * <post> JFrame is set visible </post>
   */
  private void setFrameVisible() {
    this.frame.setVisible(true);
  }

  /**
   * add the newly given page to the page history in the UIFrame object and update the content of
   * the JFrame
   *
   * @param: newHistory, Page object, the new page to be added at the back of the PageHistory object
   *         in UIFrame object
   *
   *         <pre>
   * PageHistory object has been instantiated
   * </pre>
   *
   *         <post> the given page in the argument is added to the end of the page history</post>
   *         <post> JFrame's content is updated based on the history.getCurrent()</post>
   */
  public void updateHistoryNDisplay(final Page newHistory) {
    this.history.add(newHistory);
    updateFrameConent();
  }

  /**
   * method to update the JFrame content based on the content of the current display page within the
   * UIFrame object
   *
   * <pre>
   * history.getCurrent() exists
   * </pre>
   *
   * <post> the content in the JFrame is updated to display the content of the current page
   */
  private void updateFrameConent() {
    this.frame.getContentPane().removeAll();
    this.frame.getContentPane().add(
        getHead(),
        Constants.CELL_0_0_POSITION + Constants.COMMA + GROWX_CONSTRAIN_CELL + Constants.COMMA
            + TOP_ALIGNMET_CONSTRAIN_CELL);
    this.frame.getContentPane().add(this.history.getCurrent().getPageBody(),
        Constants.CELL_0_1_POSITION + Constants.COMMA + Constants.GROW_CONSTRAIN_CELL);
    this.frame.getContentPane().add(this.history.getCurrent().getPageBotttom(),
        Constants.CELL_0_2_POSITION);
    this.frame.getContentPane().validate();
    this.frame.getContentPane().repaint();
  }

  /**
   * helper method to create a new head JPanel for the JFrame in the object, the head contains the
   * tool bar providing browsing functionality
   *
   * @return a JPanel to locate at the top part of the JFrame in the object.
   */
  private JPanel getHead() {
    final JPanel head = new JPanel();
    head.setLayout(new MigLayout(Constants.NO_CONSTRAIN, Constants.GROW_CONSTRAIN,
        Constants.GROW_CONSTRAIN));
    head.add(getToolBar(), Constants.CELL_0_0_POSITION + Constants.COMMA + GROWX_CONSTRAIN_CELL);
    return head;
  }

  /**
   * helper method to the JToolBar to be located in the JPanel, head. The toolbar contains a forward
   * and backward buttons to go back and forth in page history, a search bar to search for a wiki
   * page based on keyword or address, determined by the option from a dropdown menu.
   *
   * @return a JToolBar containing tools to provide functionality to go back and forth in history
   *         and searching capability for wiki pages.
   */
  private JToolBar getToolBar() {
    final JToolBar toolBar = new JToolBar();
    /**
     * backwards button to go to the previous page button is disabled when there are no wiki pages
     * in the application or this the the first page of the current session.
     */
    final BackwardButton backwardButton = new BackwardButton();
    toolBar.add(backwardButton);
    /**
     * forwards button to go to the forward page button is disabled when there are no wiki pages in
     * the application or this the the first page of the current session.
     */
    final ForwardButton forwardButton = new ForwardButton();
    toolBar.add(forwardButton);
    /**
     * empty field to type in the interested wiki title to search and click on the search button to
     * proceed
     */
    final JTextField search_field = makeSearchField();
    toolBar.add(search_field);

    final JComboBox<String> comboBox = new JComboBox<String>();
    final SearchButton btnSearch = new SearchButton(search_field, comboBox);
    toolBar.add(btnSearch);
    /**
     * drop down menu indicating based on which method to search for the wiki page
     */
    final JPanel inner_panel = makeDropDownMenu(comboBox);
    toolBar.add(inner_panel);

    return toolBar;
  }

  /**
   * helper method to set up the layout of the JFrame within the UIFrame object
   *
   * <pre>
   * the UIFrame class contains JFrame field
   * </pre>
   *
   * <post> the layout of the JFrame is determined
   */
  private void setUpJFrame() {
    this.frame = new JFrame();
    this.frame
        .setBounds(INITIAL_X_POSITION, INITIAL_Y_POSITION, INITIAL_X_LENGTH, INITIAL_Y_LENGTH);
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.getContentPane().setLayout(
        new MigLayout(Constants.NO_CONSTRAIN, Constants.GROW_CONSTRAIN, Constants.EMPTY_CONSTRAIN
            + Constants.GROW_CONSTRAIN + Constants.EMPTY_CONSTRAIN));
  }

  /**
   * create the search field for user to type in information to search for the interested wiki page
   *
   * @return a JTextField for user to type in the information to search for wiki page
   */
  private JTextField makeSearchField() {
    final JTextField search_field = new JTextField();
    search_field.setHorizontalAlignment(SwingConstants.LEFT);
    search_field.setFont(new Font(TEXT_FONT, Font.PLAIN, TEXT_SIZE));
    search_field.setColumns(SEARCH_FIELD_LENGTH);
    search_field.setToolTipText(TYPINGTEXT);
    return search_field;
  }

  /**
   * this method creates the JPanel containing a drop-down menu for user to pick the method for the
   * search, by keyword or by address.
   *
   * @return a JPanel containing a drop-down menu for user to pick the method for the search
   */
  private JPanel makeDropDownMenu(final JComboBox<String> comboBox) {
    final JPanel inner_panel = new JPanel();
    inner_panel.setLayout(new BoxLayout(inner_panel, BoxLayout.Y_AXIS));

    final JLabel searchby_label = new JLabel(SEARCHBY_LABEL_TEXT);
    searchby_label.setHorizontalAlignment(SwingConstants.LEFT);
    searchby_label.setFont(new Font(TEXT_FONT, Font.PLAIN, TEXT_SIZE));
    searchby_label.setAlignmentX(_0_5F);
    inner_panel.add(searchby_label);

    comboBox.setToolTipText(SEARCH_DROP_DOWN_TIP);
    comboBox.setFont(new Font(TEXT_FONT, Font.PLAIN, TEXT_SIZE));
    comboBox.setModel(new DefaultComboBoxModel<String>(SEARCHBYLIST));
    inner_panel.add(comboBox);
    return inner_panel;
  }

  /**
   * this method makes the UIFrame object to go the the previous page in history
   *
   * <pre>
   * history.hasPrevious() == true
   * </pre>
   *
   * <post> history.current() == history.getPrevious();
   */
  public void goPreviousPage() {
    this.history.getPrevious();
    updateFrameConent();
  }

  /**
   * this method makes the UIFrame object to go the the next page in history
   *
   * <pre>
   * history.hasNext() == true
   * </pre>
   *
   * <post> history.current() == history.getNext();
   */
  public void goNextPage() {
    this.history.getNext();
    updateFrameConent();
  }

  /**
   * this method displays a notification on the JFrame of the UIFrame object
   *
   * @param errorMessage : Any string, the error message to be displayed on the notification
   */
  public void displayMessage(final String errorMessage) {
    JOptionPane.showMessageDialog(this.frame, errorMessage);
  }

  /**
   * this method returns the path of the folder where Wiki notes are saved in
   *
   * <pre>
   * new folder is allowed to be created in the current folder of the executable program
   * </pre>
   *
   * @return folder path to store wiki notes in the local machine
   */
  public Path getWikiFilePath() {
    return this.pathManager.getWikiFilePath();
  }

  /**
   * this method returns whether there is a next page in history with respect to the current page.
   * True if there is one, false otherwise
   *
   * @return True if there is next page, false otherwise
   */
  public boolean hasNextPage() {
    return this.history.hasNext();
  }

  /**
   * this method returns whether there is a previous page in history with respect to the current
   * page. True if there is one, false otherwise
   *
   * @return True if there is a previous page, false otherwise
   */
  public boolean hasPreviousPage() {
    return this.history.hasPrevious();
  }
}
