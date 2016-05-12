package wekey.file.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class ReadFile {
  private static final Logger LOGGER = Logger.getLogger(FileStorage.class);
  private static final String FILE_NOT_FOUND = "file not found";

  /**
   * Given a file path return the contents of the file as string.
   *
   * <pre>
   * pre: file path exists.
   * </pre>
   *
   * <pre>
   * post: A string representation of all the contents of the file.
   * </pre>
   *
   * @param path : file path in the wiki database
   * @return: A string representation of all the contents of the file.
   * @throws Exception
   * @throws FileNotFoundException //> need javadoc comment here
   */
  public String readTextOfFile(final String path) throws Exception {
    final StringBuilder builder = new StringBuilder();
    try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8")) {
      int text = reader.read();
      while (text != -1) {
        builder.append((char) text);
        text = reader.read();
      }
    } catch (final RuntimeException e) {
      throw e; // > same comment here, redundant try-catch

    } catch (final Exception e) {
      // > this catch statement will catch all Exceptions that are not RuntimeExceptions.
      // > It can also catch a security exception, but this code will report that he file is not
      // found?
      // > I would expect a catch statement that only deals with the specific FileNotFoundException
      // to
      // > print the message file not found

      // > -1
      LOGGER.info(FILE_NOT_FOUND);
      throw new FileNotFoundException();
    }
    return builder.toString();
  }
}
