package wekey.file.storage;

import java.io.FileNotFoundException;

import org.junit.Test;

import wekey.ui.entities.PathManager;

public class ReadFileTest {

  PathManager manager = new PathManager();
  ReadFile reader = new ReadFile();

  @Test(expected = FileNotFoundException.class)
  public void fileDoesNotExistTest() throws Exception {
    reader.readTextOfFile("");
  }

  @Test
  public void readFileTest() throws Exception {
    FileStorage storer = new FileStorage();
    storer.storeTextHelper("readFileTest", "readFileTest", "first line");
    String text = reader.readTextOfFile("WikiNotes/readFileTest");
    System.out.println("file text: " + text);
  }
}
