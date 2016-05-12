package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class HeaderTest {
    MarkdownInterpreter mt = new MarkdownInterpreter();

    @Test
    public void test1() {
        Assert.assertEquals("failed to add header", "<html><body><h1>header1</h1></body></html>", mt.convertToHTML("#header1"));
    }

    @Test
    public void test2() {
        Assert.assertEquals("failed should count up to header 6", "<html><body><h6>header6</h6></body></html>", mt.convertToHTML("######header6"));
    }

    @Test
    public void test3() {
        Assert.assertEquals("failed should count up to header 6 and treat additional # as regular text", "<html><body><h6>#header6+extra</h6></body></html>",
                mt.convertToHTML("#######header6+extra"));
    }

    @Test
    public void test4() {
        Assert.assertEquals("failed should remove trailing # for headers", "<html><body><h1>trailing pounds</h1></body></html>",
                mt.convertToHTML("#trailing pounds#"));
    }

    @Test
    public void test5() {
        Assert.assertEquals("failed test to ensure that all trailing # are removed for headers regardless of length",
                "<html><body><h1>trailing pounds</h1></body></html>", mt.convertToHTML("#trailing pounds#######"));
    }

    @Test
    public void test6() {
        Assert.assertEquals("failed test to ensure # is ignored if not at start of line", "<html><body><p># after space</p></body></html>",
                mt.convertToHTML(" # after space"));
    }

    @Test
    public void test7() {
        Assert.assertEquals("failed test to ensure trailing # is not removed if there are elements after it on the line",
                "<html><body><h1>space after # </h1></body></html>", mt.convertToHTML("#space after # "));
    }

    @Test
    public void test8() {
        Assert.assertEquals("failed test to ensure trailing # is only removed for headers", "<html><body><p>no header #</p></body></html>",
                mt.convertToHTML("no header #"));
    }

    @Test
    public void test9() {
        Assert.assertEquals("failed test to ensure whitespace after header tag is removed", "<html><body><h1>spaces after tag</h1></body></html>",
                mt.convertToHTML("#     spaces after tag"));
    }

    @Test
    public void test10() {
        Assert.assertEquals("failed test that headers interrupting paragraphs split to two paragraphs",
                "<html><body><p>paragraph1</p><h1>header</h1><p>paragraph2</p></body></html>", mt.convertToHTML("paragraph1\r\n#header\r\nparagraph2"));
    }

    @Test
    public void test11() {
        Assert.assertEquals("failed to ignore blank lines", "<html><body><h1>hi</h1></body></html>", mt.convertToHTML("\r\n\r\n\r\n\r\n\r\n#hi"));
    }
}
