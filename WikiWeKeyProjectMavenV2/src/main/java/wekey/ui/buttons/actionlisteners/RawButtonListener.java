package wekey.ui.buttons.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import wekey.ui.pages.UIFrame;
import wekey.ui.pages.ViewRawPage;

public class RawButtonListener implements ActionListener {
  private final File file;

  /**
   * constructor to create a RawButtonListener instance
   *
   * @param: file, File object specifying the path of the wiki page to be rendered
   */
  public RawButtonListener(final File file) {
    this.file = file;
  }

  /**
   * upon clicking on it, the the UIFrame turns from ViewInterpretedPage to ViewRawPage, showing
   * original marked-up text of the wiki page
   *
   * @param: event, ActionEvent when mouse button down is true
   */
  @Override
  public void actionPerformed(final ActionEvent event) {
    final UIFrame mainUI = UIFrame.getInstance();
    mainUI.updateHistoryNDisplay(new ViewRawPage(this.file));
  }

}
