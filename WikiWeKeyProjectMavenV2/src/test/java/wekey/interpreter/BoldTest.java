package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class BoldTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // unit tests for bold
  @Test
  public void test1() {
    Assert.assertEquals("failed simple bold",
        "<html><body><p><strong>strike</strong></p></body></html>", mt.convertToHTML("**strike**"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("failed bold at header end",
        "<html><body><h1><strong>strike</strong></h1></body></html>",
        mt.convertToHTML("#**strike**"));
  }

  @Test
  public void test3() {
    Assert.assertEquals("failed bold in middle",
        "<html><body><p>text<strong>strike</strong>test</p></body></html>",
        mt.convertToHTML("text**strike**test"));
  }

  @Test
  public void test4() {
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text**strike start\r\nend**test"));
  }

  @Test
  public void test5() {
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text**strike start\r\nend**test"));
  }

  @Test
  public void test6() {
    Assert
        .assertEquals("failed to ignore bold without close",
            "<html><body><p>**boldwithout end</p></body></html>",
            mt.convertToHTML("**boldwithout end"));
  }

  @Test
  public void test7() {
    Assert.assertEquals("failed to convert from bold end to italic at correct index",
        "<html><body><p><em>text</em>* </p></body></html>", mt.convertToHTML("*text** "));
  }
}
