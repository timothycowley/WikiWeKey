package wekey.ui.buttons.actionlisteners;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import wekey.ui.pages.UIFrame;
import wekey.ui.pages.ViewRenderedPage;

public class SearchHyperLinkedListener implements HyperlinkListener {


  private static final String WIKINOTE_PATH_NOT_EXIST =
      "Error: this wiki note path does not exist, please check again!";
  private static final String URL_DISPLAY_ERROR =
      "Error: Problem opening the link, please check if the URL Syntax is right.";

  /**
   * upon clicking on the hyperlink, the wiki page link clicked on will be displayed in a
   * ViewRenderedPage
   *
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void hyperlinkUpdate(final HyperlinkEvent event) {
    final UIFrame mainUI = UIFrame.getInstance();
    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      if (event.getURL() == null) {
        handleWikiNoteLink(event, mainUI);
      } else {
        handleNetworkURL(event, mainUI);
      }
    }
  }

  /**
   * This method helps to handle the case when the hyperlink is directing to an wiki note link on
   * the local machine
   *
   * @param: event, ActionEvent when mouse button down is true
   * @param: mainUI, the UIFrame of the program
   * @throws URISyntaxException when specified URL cannot be opened properly in the browser
   * @throws URISyntaxException when specified URL cannot be properly read
   *
   *
   *         //> URISyntaxException is a checked exception, the method does not have a throws or a
   *         try-catch to deal with this exception. In fact I am pretty sure the code below does not
   *         even throw the exception.
   *
   *
   *         //> -1
   *
   *
   */
  public void handleWikiNoteLink(final HyperlinkEvent event, final UIFrame mainUI) {
    final File targetFile = new File(event.getDescription());
    if (targetFile.exists()) {
      mainUI.updateHistoryNDisplay(new ViewRenderedPage(targetFile));
    } else {
      mainUI.displayMessage(WIKINOTE_PATH_NOT_EXIST);
    }
  }

  /**
   * This method helps to handle the case when the hyperlink is directing to an external link
   * (webpage) and opens it up on browser if available
   *
   * @param: event, ActionEvent when mouse button down is true
   * @param: mainUI, the UIFrame of the program
   * @throws URISyntaxException when specified URL cannot be opened properly in the browser
   * @throws URISyntaxException when specified URL cannot be properly read
   */
  private void handleNetworkURL(final HyperlinkEvent event, final UIFrame mainUI) {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(event.getURL().toURI());
      } catch (IOException | URISyntaxException e) {
        mainUI.displayMessage(URL_DISPLAY_ERROR);
        e.printStackTrace(); // > again this would print on the console, use Logger.
      }
    }
  }
}
