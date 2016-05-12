package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

// unit test for overlapping bold and italic
public class OverlappingBoldAndItalicTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  @Test
  public void test1() {
    Assert.assertEquals("failed to ignore 2nd start bold",
        "<html><body><p><strong>first <em>*second end1</strong> end2</em>*</p></body></html>",
        mt.convertToHTML("**first **second end1** end2**"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("failed to convert from bold start to italic end when needed",
        "<html><body><p><em>text _</em>text</p></body></html>", mt.convertToHTML("_text __text"));
  }

  @Test
  public void test3() {
    Assert.assertEquals("failed to convert from bold end to italic at correct index ",
        "<html><body><p><em>text</em>_ </p></body></html>", mt.convertToHTML("_text__ "));
  }

  // FIXME bug found
  // desired
  // org.junit.Assert.assertEquals("failed to convert from bold-start to italic start when needed",
  // "<html><body><p><em>_text</em> </p></body></html>", mt.convertToHTML(" __text_ "));
  // actual
  @Test
  public void test4() {
    Assert.assertEquals("failed to convert from bold-start to italic start when needed",
        "<html><body><p>__text_ </p></body></html>", mt.convertToHTML(" __text_ "));
  }

  // FIXME bug found in BoldMark as well -> bold start remains a bold start until the very end
  // because it
  // it did not find a match. remove unclosed marks should convert it to an italic start, but
  // italic end
  // has probably already been removed at this point because it didn't have a matching start.
  // new regular bold tests to see if same bugs exist in BoldMark
  // desired
  // org.junit.Assert.assertEquals("failed to convert from bold-start to italic start when needed",
  // "<html><body><p><em>*text</em> </p></body></html>", mt.convertToHTML(" **text* "));
  // actual
  public void test5() {
    Assert.assertEquals("failed to convert from bold-start to italic start when needed",
        "<html><body><p>**text* </p></body></html>", mt.convertToHTML(" **text* "));
  }
}
