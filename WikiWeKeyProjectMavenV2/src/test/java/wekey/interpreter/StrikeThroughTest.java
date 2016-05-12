package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class StrikeThroughTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // unit tests for strikethrough
  @Test
  public void test1() {
    Assert.assertEquals("failed simple strikeout",
        "<html><body><p><s>strike</s></p></body></html>", mt.convertToHTML("~~strike~~"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("failed strikeout at header end",
        "<html><body><h1><s>strike</s></h1></body></html>", mt.convertToHTML("#~~strike~~"));
  }

  @Test
  public void test3() {
    Assert.assertEquals("failed strikeout in middle",
        "<html><body><p>text<s>strike</s>test</p></body></html>",
        mt.convertToHTML("text~~strike~~test"));
  }

  @Test
  public void test4() {
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<s>strike start end</s>test</p></body></html>",
        mt.convertToHTML("text~~strike start\r\nend~~test"));
  }

  @Test
  public void test5() {
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<s>strike start end</s>test</p></body></html>",
        mt.convertToHTML("text~~strike start\r\nend~~test"));
  }

  @Test
  public void test6() {
    Assert.assertEquals("failed to ignore 2nd start strike",
        "<html><body><p><s>first ~~second end1</s> end2~~</p></body></html>",
        mt.convertToHTML("~~first ~~second end1~~ end2~~"));
  }

  @Test
  public void test7() {
    Assert.assertEquals(
        "multiple starts and ends, should ignore extra starts and use first start and end",
        "<html><body><p><s>first ~~second end</s> ~~third ~~fourth</p></body></html>",
        mt.convertToHTML("~~first ~~second end~~ ~~third ~~fourth"));
  }
}
