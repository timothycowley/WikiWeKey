package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class AutomaticLinkTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // tests for automatic links:
  @Test
  public void test1() {
    Assert.assertEquals("failed to find automatic link",
        "<html><body><p><a href=http://www.google.com>http://www.google.com</a></p></body></html>",
        mt.convertToHTML("<http://www.google.com>"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("utilized an invalid end",
        "<html><body><p><http://www.google<p>.com></p></body></html>",
        mt.convertToHTML("<http://www.google<p>.com>"));
  }

  @Test
  public void test3() {
    Assert
        .assertEquals(
            "utilized wrong start",
            "<html><body><p><a href=http<http://www.google.com>http<http://www.google.com</a></p></body></html>",
            mt.convertToHTML("<http<http://www.google.com>"));
  }

  @Test
  public void test4() {
    Assert
        .assertEquals(
            "failed to use closest end",
            "<html><body><p><a href=http://www.google.com>http://www.google.com</a>>>some text></p></body></html>",
            mt.convertToHTML("<http://www.google.com>>>some text>"));
  }

  @Test
  public void test5() {
    Assert
        .assertEquals(
            "failed to work with multiple links",
            "<html><body><p><a href=http://www.google.com>http://www.google.com</a><a href=http://www.google.com>http://www.google.com</a></p></body></html>",
            mt.convertToHTML("<http://www.google.com><http://www.google.com>"));
  }

  @Test
  public void test6() {
    Assert
        .assertEquals(
            "failed to ignore internal marks",
            "<html><body><p>some **text before<a href=http://www.google**.com>http://www.google**.com</a></p></body></html>",
            mt.convertToHTML("some **text before<http://www.google**.com>"));
  }
}
