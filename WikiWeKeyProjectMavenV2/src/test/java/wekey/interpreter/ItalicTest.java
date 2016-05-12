package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ItalicTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // unit tests for italic
  @Test
  public void test1() {
    Assert.assertEquals("failed simple italic", "<html><body><p><em>strike</em></p></body></html>",
        mt.convertToHTML("*strike*"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("failed italic at header end",
        "<html><body><h1><em>strike</em></h1></body></html>", mt.convertToHTML("#*strike*"));
  }

  @Test
  public void test3() {
    Assert.assertEquals("failed italic in middle",
        "<html><body><p>text<em>strike</em>test</p></body></html>",
        mt.convertToHTML("text*strike*test"));
  }

  @Test
  public void test4() {
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<em>strike start end</em>test</p></body></html>",
        mt.convertToHTML("text*strike start\r\nend*test"));
  }

  @Test
  public void test5() {
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<em>strike start end</em>test</p></body></html>",
        mt.convertToHTML("text*strike start\r\nend*test"));
  }

  @Test
  public void test6() {
    Assert.assertEquals("failed to ignore 2nd start strike",
        "<html><body><p><em>first *second end1</em> end2*</p></body></html>",
        mt.convertToHTML("*first *second end1* end2*"));
  }

  @Test
  public void test7() {
    Assert.assertEquals("two starts, should use first start and end",
        "<html><body><p><em>first *second end</em></p></body></html>",
        mt.convertToHTML("*first *second end*"));
  }
}
