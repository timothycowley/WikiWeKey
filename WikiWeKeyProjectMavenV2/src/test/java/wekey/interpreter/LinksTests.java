package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class LinksTests {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  @Test
  public void test1() {
    Assert.assertEquals("failed to generate simple link",
        "<html><body><p><a href=http://link>hi</a>textAt End</p></body></html>",
        mt.convertToHTML("[hi](http://link)textAt End"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("failed to generate simple link",
        "<html><body><p><a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML("[hi](http://link)"));
  }

  @Test
  public void test3() {
    Assert.assertEquals("failed to ignore extra start",
        "<html><body><p>[<a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML("[[hi](http://link)"));
  }

  @Test
  public void test4() {
    org.junit.Assert.assertEquals("failed to ignore extra start",
        "<html><body><p><a href=http://[link>hi</a></p></body></html>",
        mt.convertToHTML("[hi](http://[link)"));
  }

  @Test
  public void test5() {
    Assert.assertEquals("failed to ignore extra start",
        "<html><body><p><a href=http://link>hi</a>[</p></body></html>",
        mt.convertToHTML("[hi](http://link)["));
  }

  @Test
  public void test6() {
    Assert.assertEquals("failed to ignore extra middle",
        "<html><body><p>](<a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML("]([hi](http://link)"));
  }

  @Test
  public void test7() {
    Assert.assertEquals("failed to ignore extra middle",
        "<html><body><p><a href=http://](link>hi</a></p></body></html>",
        mt.convertToHTML("[hi](http://](link)"));
  }

  @Test
  public void test8() {
    Assert.assertEquals("failed to ignore extra middle",
        "<html><body><p><a href=http://link>hi</a>](</p></body></html>",
        mt.convertToHTML("[hi](http://link)]("));
  }

  @Test
  public void test9() {
    Assert.assertEquals("failed to ignore extra end",
        "<html><body><p>)<a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML(")[hi](http://link)"));
  }

  @Test
  public void test10() {
    Assert.assertEquals("failed to ignore extra end",
        "<html><body><p><a href=http://link>hi)</a></p></body></html>",
        mt.convertToHTML("[hi)](http://link)"));
  }

  @Test
  public void test11() {
    Assert.assertEquals("failed to ignore extra end",
        "<html><body><p><a href=http://link>hi</a>)</p></body></html>",
        mt.convertToHTML("[hi](http://link))"));
  }
  // TODO tests that it ignores extra link ends,

}
