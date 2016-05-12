package wekey.search;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import wekey.ui.pages.UIFrame;

public class Search {
  private static final String CANNOT_FIND_FOLDER_ERROR =
      "Error: Cannot find the folder containing Wiki Notes"; // error message find folder cannot be
                                                             // found
  private String matchString;
  private List<File> matchedFiles = new ArrayList<File>();
  private static final String PATTERN1 = "(?i:.*?";
  private static final String PATTERN2 = ".*?)";
  private UIFrame mainUI = UIFrame.getInstance();
  private static final String PATTERN_ERROR =
      "you typed full address and serached for keyword try searching with address";

  /**
   * Search for a given String and returns all the wiki pages which contains the given String in its
   * title
   * 
   * @param matchString : String, given String to match the wiki page
   * @return All the wiki pages which contains the given String in its title
   * 
   *         <pre>
   * pre: A non empty String to search for
   * </pre>
   * 
   *         <pre>
   * post: All the wiki pages which contains
   * the given String in its title
   * </pre>
   */
  public List<File> getMatchingFiles(String matchString) {
    this.matchString = matchString.trim();
    if (this.matchString.isEmpty()) {
      return matchedFiles;
    }
    Path targetFolder = mainUI.getWikiFilePath();
    File folder = targetFolder.toFile();
    if (folder.exists()) {
      checkSubFolders(folder);
    } else {
      mainUI.displayMessage(CANNOT_FIND_FOLDER_ERROR);
    }
    return matchedFiles;
  }

  /**
   * Search for a given String and returns all the wiki pages in the given folder which contains the
   * given String in its title
   * 
   * @param folder : File, sub-folder to search for wiki pages
   */
  private void checkSubFolders(File folder) {
    try {
      for (File subfolder : folder.listFiles()) {
        if (subfolder.exists() && subfolder.isDirectory()) {
          checkSubFolders(subfolder);
        } else if (subfolder.getName().matches(PATTERN1 + matchString + PATTERN2)) {
          matchedFiles.add(subfolder);
        }
      }
    } catch (PatternSyntaxException e) {
      mainUI.displayMessage(PATTERN_ERROR);
    }
  }
}
