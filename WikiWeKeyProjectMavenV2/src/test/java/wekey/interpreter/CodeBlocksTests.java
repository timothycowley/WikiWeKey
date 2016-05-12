package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class CodeBlocksTests {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  @Test
  public void test1() {
    Assert.assertEquals("failed to find simple codeblock of multiple lines",
        "<html><body><pre><code>line1\r\nline2\r\nline3\r\n</code></pre></body></html>",
        mt.convertToHTML("    line1\r\n    line2\r\n    line3\r\n"));
  }

  @Test
  public void test2() {
    Assert.assertEquals("failed to find simple codeblock",
        "<html><body><pre><code>line1\r\n</code></pre></body></html>",
        mt.convertToHTML("    line1\r\n"));
  }

  @Test
  public void test3() {
    Assert
        .assertEquals(
            "failed to ignore markdown within codeblock",
            "<html><body><pre><code>codeblock with *italic* and **bold** marks\r\n</code></pre></body></html>",
            mt.convertToHTML("    codeblock with *italic* and **bold** marks"));
  }

  @Test
  public void test4() {
    Assert
        .assertEquals(
            "failed to ignore markdown within codeblock 2",
            "<html><body><pre><code>code **bold**  and*italic* and\r\nstrike~~through~~\r\n</code></pre></body></html>",
            mt.convertToHTML("    code **bold**  and*italic* and\r\n    strike~~through~~"));
  }

  @Test
  public void test5() {
    Assert
        .assertEquals(
            "failed to render codeblock properly",
            "<html><body><pre><code>startcode \r\n**continue** code \r\n~~continue~~ code \r\n</code></pre><p>regular text</p></body></html>",
            mt.convertToHTML("    startcode \r\n    **continue** code \r\n    ~~continue~~ code \r\nregular text"));
  }
}
