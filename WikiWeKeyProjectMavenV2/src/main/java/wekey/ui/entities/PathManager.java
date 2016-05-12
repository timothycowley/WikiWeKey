package wekey.ui.entities;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import wekey.ui.pages.UIFrame;

public class PathManager {
  private static final String ERROR_UNABLE_TO_CREATE_FOLDER =
      "Error: Unable to create system folders in the current directory.";
  private final Path savePath;
  private static final String LOG_FILE_NAME = "logfile.log";
  private static final String APP_FOLDER = "application";
  private static final String LOG_FOLDER = "logs";
  private static final String WIKI_FOLDER = "WikiNotes";
  private static FileSystem f = FileSystems.getDefault();

  public PathManager() {
    final Path p = f.getPath(Constants.EMPTY_STRING);
    final Path wikiNotes = p.toAbsolutePath().resolve(WIKI_FOLDER);
    final Path folder = p.toAbsolutePath().resolve(APP_FOLDER + f.getSeparator() + LOG_FOLDER);
    checkExists(wikiNotes);
    configLogs(folder);
    this.savePath = wikiNotes;
  }

  /**
   * this method check if the specified folder exists in the given path, create one if it does not
   * exist.
   *
   * @return
   */
  protected void checkExists(final Path folderPath) {
    if (!Files.exists(folderPath)) {
      try {
        Files.createDirectories(folderPath);
      } catch (final IOException e) {
        final UIFrame mainUI = UIFrame.getInstance();
        mainUI.displayMessage(ERROR_UNABLE_TO_CREATE_FOLDER);
        e.printStackTrace(); // > use logger
      }
    }
  }

  /**
   * this method check if the log folder exists in the given path, create one if it does not exist
   * and configure logger settings
   *
   * @param folder
   */
  protected void configLogs(final Path folder) {
    checkExists(folder);
    System.setProperty(LOG_FILE_NAME, folder.toString() + f.getSeparator() + LOG_FILE_NAME);
  }

  /**
   * this method returns the directory of the WikiNotes folder in the local machine
   *
   * @return Path which specifies the location of the WikiNotes folder in the local machine
   */
  public Path getWikiFilePath() {
    return this.savePath;
  }
}
