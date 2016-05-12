package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class HorizontalRuleTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  // unit tests for Horizontal Rule
  @Test
  public void test1() {
    Assert.assertEquals("HR with astrix", "<html><body><hr /></body></html>",
        mt.convertToHTML(" * * * * * *"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("HR with underscore", "<html><body><hr /></body></html>",
        mt.convertToHTML("___"));
  }

  @Test
  public void test3() {
    Assert.assertEquals("HR with dash", "<html><body><hr /></body></html>",
        mt.convertToHTML(" ----"));
  }

  @Test
  public void test4() {
    Assert.assertEquals("not an HR if only 2 elements", "<html><body><p>_ _</p></body></html>",
        mt.convertToHTML("_ _"));
  }
}
