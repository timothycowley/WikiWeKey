package wekey.file.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JEditorPane;

import org.apache.log4j.Logger;

import wekey.ui.entities.Constants;
import wekey.ui.pages.UIFrame;
import wekey.ui.pages.ViewRenderedPage;

public class FileStorage {
  private static final Logger LOGGER = Logger.getLogger(FileStorage.class);
  private static final String FILE_SAVED_MESSAGE = "File saved!";
  private static final String OTHER_FAILURE_MESSAGE =
      "Saving Failed to save file under directory: ";
  private static final String SAVE_ERROR_EMPTY_STRING = "Save Error: File name cannot be empty";
  private static final String SAVE_ERROR_FILE_EXISTS =
      "Save Error: File name already exists, please choose a different name";
  private static final String MESSAGE1 = "cannot rename the file, ";
  private static final String MESSAGE2 =
      " already exists. Try saving again with different file name.";
  private static final String CREATE_DIR_ERROR =
      "Cannot create directory, file with the same name already exists. choose different directory name.";
  private UIFrame mainUI;

  /**
   * Stores the given text into a file with the given filename
   *
   * <pre>
   * pre: newFilename does not exist in the wiki database
   * </pre>
   *
   * <pre>
   * post: saves a file in wiki database with the given filename and which has
   * the given text
   * </pre>
   *
   * @param oldFilename : String specifying the old filename of the file on edit panel
   * @param newFilename : String specifying the current name of file in which you want to save the
   *        file as
   *
   *        //> the param name does not match the argument name.
   *
   * @param pane : JEditorPane that contains all the text
   * @throws Exception //> missing Javadoc here as well.
   *
   */

  public void storeText(final String oldFileName, final String newFileName, final JEditorPane pane)
      throws Exception {
    storeTextHelper(oldFileName, newFileName, pane.getText());
  }

  /**
   * This method determines if the file name, excluding its extension, to be saved is an empty
   * string returns true if yes, false otherwise
   *
   * @param newFileName : Any string, the file name intended to be saved
   *
   * @returns true if the file name is empty string, false otherwise
   */
  private Boolean isEmptyString(final String newFileName) {
    return newFileName.equals(Constants.EMPTY_STRING);
  }

  /**
   * This method determines if the file name is valid to be saved. When saving a file, a file name
   * is only valid to be saved when the file name does not already exist in the file destination
   * (new file creation) or when the file name matches with the old file name (edition of an
   * existing file).
   *
   * @param newFileName : Any string, the file name intended to be saved
   *
   * @param oldFileName : Any string, the original file name before any edition
   *
   * @param filePath : a Path object specifying the directory of the file to be saved in along with
   *        its file name and extension
   *
   * @returns true if the file name to be saved is valid, false otherwise
   */
  private Boolean isSavableName(final String newFileName, final String oldFileName,
      final Path filePath) {
    return oldFileName.equals(newFileName) || !Files.exists(filePath);
  }

  /**
   * Stores the given text into a file with the given filename
   *
   * <pre>
   * pre: newFilename does not exist in the wiki database
   * </pre>
   *
   * <pre>
   * post: saves a file in wiki database with the given filename and which has
   * the given text
   * </pre>
   *
   * @param oldFilename : old filename of the file on edit panel
   * @param newFilename : name of file in which you want to save the text
   * @param text : all the text on the edit panel
   * @throws Exception
   *
   */
  public void storeTextHelper(final String oldFileName, final String newFileName, final String text)
      throws Exception {
    this.mainUI = UIFrame.getInstance();
    final Path targetFolder = this.mainUI.getWikiFilePath();
    final Path filePath = targetFolder.resolve(newFileName);
    if (isEmptyString(newFileName)) {
      LOGGER.info(MESSAGE1 + newFileName + MESSAGE2);
      this.mainUI.displayMessage(SAVE_ERROR_EMPTY_STRING);
      return;
    }
    if (isSavableName(newFileName, oldFileName, filePath)) {
      try {
        Files.createDirectories(filePath.getParent());
        final File file = filePath.toFile();
        storeFile(file, text);
      } catch (final FileAlreadyExistsException e) {
        LOGGER.info(CREATE_DIR_ERROR);
        this.mainUI.displayMessage(CREATE_DIR_ERROR);
      }
    } else {
      LOGGER.info(SAVE_ERROR_FILE_EXISTS);
      this.mainUI.displayMessage(SAVE_ERROR_FILE_EXISTS);
    }
  }

  /**
   * Stores the given text into a file with the given filename
   *
   * <pre>
   * pre: NONE
   * </pre>
   *
   * <pre>
   * post: saves a file in wiki database with the given filePath and which has
   * the given text
   * </pre>
   *
   * @param filePath : full path of the location where file is to be saved
   * @param text : all the text on the edit panel
   * @throws IOException
   */
  private void storeFile(final File filePath, final String text) throws Exception {
    try (OutputStreamWriter writer =
        new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8")) {
      writer.write(text);
      writer.close();
      this.mainUI.displayMessage(FILE_SAVED_MESSAGE);
      this.mainUI.updateHistoryNDisplay(new ViewRenderedPage(filePath));
    } catch (final RuntimeException e) {
      throw e; // > this seems redundant catch, do nothing and rethrow
      // > -1
    } catch (final Exception e) {
      LOGGER.info(e.getMessage());
      this.mainUI.displayMessage(OTHER_FAILURE_MESSAGE + filePath);
    }
  }
}
