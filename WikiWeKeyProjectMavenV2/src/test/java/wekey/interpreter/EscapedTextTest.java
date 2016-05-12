package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class EscapedTextTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // unit tests for escaped text
  @Test
  public void test1() {
    Assert.assertEquals("<p>&alt; </p>", "<p>< </p>".replace("< ", "&alt; "));
  }

  @Test
  public void test2() {
    Assert.assertEquals("<p>&alt;   </p>", "<p><    </p>".replace("<    ", "&alt;   "));
  }

  @Test
  public void test5() {
    Assert.assertEquals("failed test to ensure < is escaped when followed by space",
        "<html><body><p>&lt; </p></body></html>", mt.convertToHTML("< "));

  }

  @Test
  public void test6() {
    Assert.assertEquals("failed test to ensure < is escaped when followed by a tab",
        "<html><body><p>&lt;    </p></body></html>", mt.convertToHTML("<    "));
  }

  @Test
  public void test7() {
    Assert.assertEquals("failed test to ensure < is not escaped when not followed with whitespace",
        "<html><body><p><a</p></body></html>", mt.convertToHTML("<a"));
  }

  @Test
  public void test8() {
    Assert.assertEquals("failed test to ensure < is escaped when at end of line",
        "<html><body><p>&lt;</p></body></html>", mt.convertToHTML("<"));

  }

  @Test
  public void test9() {
    Assert.assertEquals("failed test to ensure that & is escaped anywhere",
        "<html><body><p>hi&amp;bye</p></body></html>", mt.convertToHTML("hi&bye"));
  }

  @Test
  public void test10() {
    Assert.assertEquals("failed test to ensure that escapes happen in headers",
        "<html><body><h1>hi&amp;bye</h1></body></html>", mt.convertToHTML("#hi&bye"));
  }

  @Test
  public void test11() {
    Assert.assertEquals("failed test to ensure & doesn't get double escaped due to <",
        "<html><body><p>&lt; &amp;</p></body></html>", mt.convertToHTML("< &"));
  }
}
