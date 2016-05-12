package wekey.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import wekey.ui.buttons.CreateNewButton;

public class HomePage implements Page {

  private final static String HOME_PAGE = "Home Page"; // toString name of homePage
  private final static String FONT = "Tahoma"; // font used in this page
  private final static int HEADINGFONT = 45; // size of the heading font
  private final static String WELCOMEMSG =
      "<HTML><center>WikiWeKey\r\n<H1>welcome back to your personal wiki<H1></HTML>"; // message
  // displayed
  // when there
  // is file in
  // WikiNote
  // folder
  private final static String NOWIKIWELCOMEMSG =
      "<HTML><center>WikiWeKey\r\n<H1>your personal wiki<H1>\r\n" // message displayed when there is
      // no file in WikiNote folder
      + "<H3><font color=\"red\">You don't have a wiki page.</H3>"
      + " \r\n<H3><font color=\"red\">Click 'Create New' button to "
      + "create a new wiki page</H3></center>\r\n</HTML>";

  /**
   * constructor of the HomePage object, it is the first page user will see when a UIFrame object is
   * instantiated and run.
   */
  public HomePage() {}

  /**
   * Returns a JPanel that displays message to ask for user to create the first wiki page if there
   * is no wikipage in the file system, else displays welcome again message
   *
   * <pre>
   * //> no closing tag for this pre 
   * 
   *  //> -1
   * no one manually added stuff into the WikiNote folder
   * @return a JPanel that displays message based on the availability of any wikipages in the wikinote folder
   */
  @Override
  public JPanel getPageBody() {
    final JPanel body = new JPanel();
    body.setLayout(new BorderLayout(0, 0));
    body.add(getHomePageContent());
    return body;
  }

  /**
   * return a JLabel containing the content of the home page
   *
   * <pre>
   * no one manually added stuff into the WikiNote folder
   * @return a JLabel that displays message based on the availability of any wikipages in the file system
   */
  private JLabel getHomePageContent() {
    final JLabel heading = new JLabel();
    heading.setForeground(Color.BLUE);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    setContent(heading);
    heading.setFont(new Font(FONT, Font.BOLD | Font.ITALIC, HEADINGFONT));
    return heading;
  }

  /**
   * return a JLabel containing the content of the home page based on if there is anything in the
   * WikiNote folder, if yes return a message saying no wiki note is available, else return a
   * message saying welcome back
   *
   * <pre>
   * no one manually added stuff into the WikiNote folder
   * <post> a JLabel that displays message based on the availability of any wikipages in the WikiNote folder
   * @param heading, JLabel, content to be displayed on the HomePage
   */
  private void setContent(final JLabel heading) {
    final UIFrame mainUI = UIFrame.getInstance();
    final Path folderPath = mainUI.getWikiFilePath();
    final File folder = folderPath.toFile();
    if (folder.list().length > 0) {
      heading.setText(WELCOMEMSG);
    } else {
      heading.setText(NOWIKIWELCOMEMSG);
    }
  }

  /**
   * Returns a JPanel that contains a CreateNewButton
   *
   * @return a JPanel that contains only one create new button
   */
  @Override
  public JPanel getPageBotttom() {
    final JPanel bottom = new JPanel();
    final FlowLayout fl_buttom = (FlowLayout) bottom.getLayout();
    fl_buttom.setAlignment(FlowLayout.LEFT);

    final CreateNewButton createNewButton = new CreateNewButton();
    bottom.add(createNewButton);

    return bottom;
  }

  /**
   * @return the String based on page type
   */
  @Override
  public String toString() {
    return HOME_PAGE;
  }
}
