package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class italicUnderscoreTest {
    MarkdownInterpreter mt = new MarkdownInterpreter();

    // unit tests for italic underscore
    @Test
    public void test1() {
        Assert.assertEquals("failed simple italic", "<html><body><p><em>strike</em></p></body></html>", mt.convertToHTML("_strike_"));
    }

    @Test
    public void test2() {
        Assert.assertEquals("failed italic at header end", "<html><body><h1><em>strike</em></h1></body></html>", mt.convertToHTML("#_strike_"));
    }

    @Test
    public void test3() {
        Assert.assertEquals("failed italic in middle", "<html><body><p>text<em>strike</em>test</p></body></html>", mt.convertToHTML("text_strike_test"));
    }

    @Test
    public void test4() {
        Assert.assertEquals("failed strikeout spans lines", "<html><body><p>text<em>strike start end</em>test</p></body></html>",
                mt.convertToHTML("text_strike start\r\nend_test"));
    }

    @Test
    public void test5() {
        Assert.assertEquals("failed strikeout spans lines", "<html><body><p>text<em>strike start end</em>test</p></body></html>",
                mt.convertToHTML("text_strike start\r\nend_test"));
    }

    @Test
    public void test6() {
        Assert.assertEquals("failed to ignore 2nd start strike", "<html><body><p><em>first _second end1</em> end2_</p></body></html>",
                mt.convertToHTML("_first _second end1_ end2_"));
    }
}
