package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class BlockQuoteTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // unit tests for Block Quote
  @Test
  public void test1() {
    Assert
        .assertEquals(
            "failed to identify block quote",
            "<html><body><blockquote><p>a basic blockquote of two lines</p></blockquote></body></html>",
            mt.convertToHTML(">a basic blockquote of\r\ntwo lines"));
  }

  @Test
  public void test2() {
    Assert
        .assertEquals(
            "failed to remove starting >",
            "<html><body><blockquote><p>a basic blockquote of two lines</p></blockquote></body></html>",
            mt.convertToHTML(">a basic blockquote of\r\n>two lines"));
  }

  @Test
  public void test3() {
    Assert
        .assertEquals(
            "failed to nest blockquotes",
            "<html><body><blockquote><p>a nested blockquote</p><blockquote><p>nested component</p></blockquote></blockquote></body></html>",
            mt.convertToHTML(">a nested blockquote\r\n>>nested component"));
  }

  @Test
  public void test4() {
    Assert
        .assertEquals(
            "failed to end at proper spot",
            "<html><body><blockquote><p>a basic blockquote with multiple lines</p></blockquote><p>followed by regular text</p></body></html>",
            mt.convertToHTML(">a basic blockquote\r\nwith\r\nmultiple\r\nlines\r\n\r\nfollowed by regular text"));
  }

}
