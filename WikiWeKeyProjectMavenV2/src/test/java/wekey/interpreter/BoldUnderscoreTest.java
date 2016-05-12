package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class BoldUnderscoreTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // unit tests for bold underscore
  @Test
  public void test1() {
    Assert.assertEquals("failed simple bold",
        "<html><body><p><strong>strike</strong></p></body></html>", mt.convertToHTML("__strike__"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("failed bold at header end",
        "<html><body><h1><strong>strike</strong></h1></body></html>",
        mt.convertToHTML("#__strike__"));

  }

  @Test
  public void test3() {
    Assert.assertEquals("failed bold in middle",
        "<html><body><p>text<strong>strike</strong>test</p></body></html>",
        mt.convertToHTML("text__strike__test"));
  }

  @Test
  public void test4() {
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text__strike start\r\nend__test"));
  }

  @Test
  public void test5() {
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text__strike start\r\nend__test"));
  }

  @Test
  public void test6() {
    Assert.assertEquals("failed simple bold with spaces",
        "<html><body><p><strong>bold</strong> </p></body></html>", mt.convertToHTML(" __bold__ "));
  }

  @Test
  public void test7() {
    Assert.assertEquals("failed to remove unclosed bold starts from end",
        "<html><body><p><strong>text</strong> __text <em>text</em></p></body></html>",
        mt.convertToHTML("__text__ __text *text*"));
  }

  @Test
  public void test8() {
    Assert.assertEquals("failed to remove unusable ends",
        "<html><body><p><strong>text</strong> text__</p></body></html>",
        mt.convertToHTML("__text__ text__"));
  }
}
