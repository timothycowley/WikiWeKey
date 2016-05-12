package wekey.file.storage;

import org.junit.Test;

public class FileStorageTest {

  FileStorage storer = new FileStorage();

  @Test
  public void testEmptyText() throws Exception {
    storer.storeTextHelper("test1", "test1", "");
  }

  @Test
  public void testEditFileText() throws Exception {
    storer.storeTextHelper("test1", "test1", "this is the first edit");
  }

  @Test
  public void testEdit() throws Exception {
    storer.storeTextHelper("test1", "test2", "this is the second edit");
  }

  @Test
  public void testFileAlreayExists() throws Exception {
    storer.storeTextHelper("test1", "test2", "this is the second edit");
  }

  @Test
  public void subFolderTest() throws Exception {
    storer.storeTextHelper("subtest", "new/test/for/subfolder/test", "this is the second edit");
  }


  @Test
  public void emptyStringTest() throws Exception {
    storer.storeTextHelper("", "", "this is the second edit");
  }
}
