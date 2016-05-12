package wekey.ui.pages;


import javax.swing.JPanel;

/**
 * Page Interface that specifies every object implementing it must return a JPanel for the body and
 * bottom
 */
public interface Page {
  /**
   * this method returns a JPanel specifying the Page's body content
   *
   * //> need to use @return
   */
  public abstract JPanel getPageBody();

  /**
   * this method returns a JPanel specifying the Page's bottom content
   */
  public abstract JPanel getPageBotttom();
}
