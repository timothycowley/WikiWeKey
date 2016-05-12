package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ParagraphTest {
    MarkdownInterpreter mt = new MarkdownInterpreter();

    @Test
    public void test1() {
        Assert.assertEquals("failed when there is no marks, should add html tags and start a paragraph", "<html><body><p>plain text</p></body></html>",
                mt.convertToHTML("plain text"));
    }

    @Test
    public void test2() {
        Assert.assertEquals("failed to remove leading spaces at start of new paragraph", "<html><body><p>leading spaces</p></body></html>",
                mt.convertToHTML("  leading spaces"));
    }

    @Test
    public void test3() {
        Assert.assertEquals("failed test to determine if next line remains in same paragraph", "<html><body><p>line one  line2</p></body></html>",
                mt.convertToHTML("line one \r\nline2"));
    }

    @Test
    public void test4() {
        Assert.assertEquals("failed test to ensure leading spaces are included on second line of paragraph",
                "<html><body><p>line one     line2</p></body></html>", mt.convertToHTML("line one \r\n   line2"));
    }

    @Test
    public void test5() {
        Assert.assertEquals("failed test to ensure new paragraph is started after empty line",
                "<html><body><p>paragraph one</p><p>paragraph two</p></body></html>", mt.convertToHTML("paragraph one\r\n\r\nparagraph two"));
    }

    @Test
    public void test6() {
        Assert.assertEquals("failed test to ensure lines with nothing but spaces also seperate paragraphs",
                "<html><body><p>paragraph one</p><p>paragraph two</p></body></html>", mt.convertToHTML("paragraph one\r\n   \r\nparagraph two"));
    }

    @Test
    public void test7() {
        Assert.assertEquals("failed test to ensure lines with nothing but tabs also seperate paragraphs",
                "<html><body><p>paragraph one</p><p>paragraph two</p></body></html>", mt.convertToHTML("paragraph one\r\n       \r\nparagraph two"));
    }

    @Test
    public void test8() {
        Assert.assertEquals("failed to ignore blank lines", "<html><body><p>hi</p><p>bye</p></body></html>", mt.convertToHTML("hi\r\n\r\n\r\n\r\n\r\nbye"));
    }
}
