package wekey.interpreter;

import org.junit.Assert;


/**
 * This class contains all the interpreter tests, it allows for testing of everything at once, which
 * is useful after making changes to the interpreter code to ensure all features still function
 * properly, for individual tests refer to the other test classes
 *
 */
public class MarkdownInterpreterTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  @org.junit.Test
  public void test() {

    // unit tests for rendering paragraphs
    Assert.assertEquals(
        "failed when there is no marks, should add html tags and start a paragraph",
        "<html><body><p>plain text</p></body></html>", mt.convertToHTML("plain text"));
    Assert.assertEquals("failed to remove leading spaces at start of new paragraph",
        "<html><body><p>leading spaces</p></body></html>", mt.convertToHTML(" 	leading spaces"));
    Assert.assertEquals("failed test to determine if next line remains in same paragraph",
        "<html><body><p>line one  line2</p></body></html>", mt.convertToHTML("line one \r\nline2"));
    Assert.assertEquals(
        "failed test to ensure leading spaces are included on second line of paragraph",
        "<html><body><p>line one   line2</p></body></html>",
        mt.convertToHTML("line one \r\n line2"));
    Assert.assertEquals("failed test to ensure new paragraph is started after empty line",
        "<html><body><p>paragraph one</p><p>paragraph two</p></body></html>",
        mt.convertToHTML("paragraph one\r\n\r\nparagraph two"));
    Assert.assertEquals(
        "failed test to ensure lines with nothing but spaces also seperate paragraphs",
        "<html><body><p>paragraph one</p><p>paragraph two</p></body></html>",
        mt.convertToHTML("paragraph one\r\n   \r\nparagraph two"));
    Assert.assertEquals(
        "failed test to ensure lines with nothing but tabs also seperate paragraphs",
        "<html><body><p>paragraph one</p><p>paragraph two</p></body></html>",
        mt.convertToHTML("paragraph one\r\n		\r\nparagraph two"));
    Assert.assertEquals("failed to ignore blank lines",
        "<html><body><p>hi</p><p>bye</p></body></html>",
        mt.convertToHTML("hi\r\n\r\n\r\n\r\n\r\nbye"));

    // unit tests for rendering headers
    Assert.assertEquals("failed to add header", "<html><body><h1>header1</h1></body></html>",
        mt.convertToHTML("#header1"));
    Assert.assertEquals("failed should count up to header 6",
        "<html><body><h6>header6</h6></body></html>", mt.convertToHTML("######header6"));
    Assert.assertEquals(
        "failed should count up to header 6 and treat additional # as regular text",
        "<html><body><h6>#header6+extra</h6></body></html>",
        mt.convertToHTML("#######header6+extra"));
    Assert
        .assertEquals("failed should remove trailing # for headers",
            "<html><body><h1>trailing pounds</h1></body></html>",
            mt.convertToHTML("#trailing pounds#"));
    Assert.assertEquals(
        "failed test to ensure that all trailing # are removed for headers regardless of length",
        "<html><body><h1>trailing pounds</h1></body></html>",
        mt.convertToHTML("#trailing pounds#######"));
    Assert.assertEquals("failed test to ensure # is ignored if not at start of line",
        "<html><body><p># after space</p></body></html>", mt.convertToHTML(" # after space"));
    Assert
        .assertEquals(
            "failed test to ensure trailing # is not removed if there are elements after it on the line",
            "<html><body><h1>space after # </h1></body></html>",
            mt.convertToHTML("#space after # "));
    Assert.assertEquals("failed test to ensure trailing # is only removed for headers",
        "<html><body><p>no header #</p></body></html>", mt.convertToHTML("no header #"));
    Assert.assertEquals("failed test to ensure whitespace after header tag is removed",
        "<html><body><h1>spaces after tag</h1></body></html>",
        mt.convertToHTML("#     spaces after tag"));
    Assert.assertEquals("failed test that headers interrupting paragraphs split to two paragraphs",
        "<html><body><p>paragraph1</p><h1>header</h1><p>paragraph2</p></body></html>",
        mt.convertToHTML("paragraph1\r\n#header\r\nparagraph2"));
    Assert.assertEquals("failed to ignore blank lines", "<html><body><h1>hi</h1></body></html>",
        mt.convertToHTML("\r\n\r\n\r\n\r\n\r\n#hi"));

    // unit tests for escaped text
    Assert.assertEquals("<p>&alt; </p>", "<p>< </p>".replace("< ", "&alt; "));
    Assert.assertEquals("<p>&alt;   </p>", "<p><    </p>".replace("<    ", "&alt;   "));
    Assert.assertEquals("failed test to ensure < is escaped when followed by space",
        "<html><body><p>&lt; </p></body></html>", mt.convertToHTML("< "));
    Assert.assertEquals("failed test to ensure < is escaped when followed by a tab",
        "<html><body><p>&lt;	</p></body></html>", mt.convertToHTML("<	"));
    Assert.assertEquals("failed test to ensure < is not escaped when not followed with whitespace",
        "<html><body><p><a</p></body></html>", mt.convertToHTML("<a"));
    Assert.assertEquals("failed test to ensure < is escaped when at end of line",
        "<html><body><p>&lt;</p></body></html>", mt.convertToHTML("<"));
    Assert.assertEquals("failed test to ensure that & is escaped anywhere",
        "<html><body><p>hi&amp;bye</p></body></html>", mt.convertToHTML("hi&bye"));
    Assert.assertEquals("failed test to ensure that escapes happen in headers",
        "<html><body><h1>hi&amp;bye</h1></body></html>", mt.convertToHTML("#hi&bye"));
    Assert.assertEquals("failed test to ensure & doesn't get double escaped due to <",
        "<html><body><p>&lt; &amp;</p></body></html>", mt.convertToHTML("< &"));

    // unit tests for strikethrough
    Assert.assertEquals("failed simple strikeout",
        "<html><body><p><s>strike</s></p></body></html>", mt.convertToHTML("~~strike~~"));
    Assert.assertEquals("failed strikeout at header end",
        "<html><body><h1><s>strike</s></h1></body></html>", mt.convertToHTML("#~~strike~~"));
    Assert.assertEquals("failed strikeout in middle",
        "<html><body><p>text<s>strike</s>test</p></body></html>",
        mt.convertToHTML("text~~strike~~test"));
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<s>strike start end</s>test</p></body></html>",
        mt.convertToHTML("text~~strike start\r\nend~~test"));
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<s>strike start end</s>test</p></body></html>",
        mt.convertToHTML("text~~strike start\r\nend~~test"));
    Assert.assertEquals("failed to ignore 2nd start strike",
        "<html><body><p><s>first ~~second end1</s> end2~~</p></body></html>",
        mt.convertToHTML("~~first ~~second end1~~ end2~~"));
    Assert.assertEquals(
        "multiple starts and ends, should ignore extra starts and use first start and end",
        "<html><body><p><s>first ~~second end</s> ~~third ~~fourth</p></body></html>",
        mt.convertToHTML("~~first ~~second end~~ ~~third ~~fourth"));

    // unit tests for bold
    Assert.assertEquals("failed simple bold",
        "<html><body><p><strong>strike</strong></p></body></html>", mt.convertToHTML("**strike**"));
    Assert.assertEquals("failed bold at header end",
        "<html><body><h1><strong>strike</strong></h1></body></html>",
        mt.convertToHTML("#**strike**"));
    Assert.assertEquals("failed bold in middle",
        "<html><body><p>text<strong>strike</strong>test</p></body></html>",
        mt.convertToHTML("text**strike**test"));
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text**strike start\r\nend**test"));
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text**strike start\r\nend**test"));
    Assert
    .assertEquals("failed to ignore bold without close",
        "<html><body><p>**boldwithout end</p></body></html>",
        mt.convertToHTML("**boldwithout end"));
    Assert.assertEquals("failed to convert from bold end to italic at correct index",
        "<html><body><p><em>text</em>* </p></body></html>", mt.convertToHTML("*text** "));


    // unit tests for italic
    Assert.assertEquals("failed simple italic", "<html><body><p><em>strike</em></p></body></html>",
        mt.convertToHTML("*strike*"));
    Assert.assertEquals("failed italic at header end",
        "<html><body><h1><em>strike</em></h1></body></html>", mt.convertToHTML("#*strike*"));
    Assert.assertEquals("failed italic in middle",
        "<html><body><p>text<em>strike</em>test</p></body></html>",
        mt.convertToHTML("text*strike*test"));
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<em>strike start end</em>test</p></body></html>",
        mt.convertToHTML("text*strike start\r\nend*test"));
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<em>strike start end</em>test</p></body></html>",
        mt.convertToHTML("text*strike start\r\nend*test"));
    Assert.assertEquals("failed to ignore 2nd start strike",
        "<html><body><p><em>first *second end1</em> end2*</p></body></html>",
        mt.convertToHTML("*first *second end1* end2*"));
    Assert.assertEquals("two starts, should use first start and end",
        "<html><body><p><em>first *second end</em></p></body></html>",
        mt.convertToHTML("*first *second end*"));


    // unit tests for bold underscore
    Assert.assertEquals("failed simple bold",
        "<html><body><p><strong>strike</strong></p></body></html>", mt.convertToHTML("__strike__"));
    Assert.assertEquals("failed bold at header end",
        "<html><body><h1><strong>strike</strong></h1></body></html>",
        mt.convertToHTML("#__strike__"));
    Assert.assertEquals("failed bold in middle",
        "<html><body><p>text<strong>strike</strong>test</p></body></html>",
        mt.convertToHTML("text__strike__test"));
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text__strike start\r\nend__test"));
    Assert.assertEquals("failed bold spans lines",
        "<html><body><p>text<strong>strike start end</strong>test</p></body></html>",
        mt.convertToHTML("text__strike start\r\nend__test"));
    Assert.assertEquals("failed simple bold with spaces",
        "<html><body><p><strong>bold</strong> </p></body></html>", mt.convertToHTML(" __bold__ "));
    Assert.assertEquals("failed to remove unclosed bold starts from end",
        "<html><body><p><strong>text</strong> __text <em>text</em></p></body></html>",
        mt.convertToHTML("__text__ __text *text*"));
    Assert.assertEquals("failed to remove unusable ends",
        "<html><body><p><strong>text</strong> text__</p></body></html>",
        mt.convertToHTML("__text__ text__"));


    // unit tests for italic underscore
    Assert.assertEquals("failed simple italic", "<html><body><p><em>strike</em></p></body></html>",
        mt.convertToHTML("_strike_"));
    Assert.assertEquals("failed italic at header end",
        "<html><body><h1><em>strike</em></h1></body></html>", mt.convertToHTML("#_strike_"));
    Assert.assertEquals("failed italic in middle",
        "<html><body><p>text<em>strike</em>test</p></body></html>",
        mt.convertToHTML("text_strike_test"));
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<em>strike start end</em>test</p></body></html>",
        mt.convertToHTML("text_strike start\r\nend_test"));
    Assert.assertEquals("failed strikeout spans lines",
        "<html><body><p>text<em>strike start end</em>test</p></body></html>",
        mt.convertToHTML("text_strike start\r\nend_test"));
    Assert.assertEquals("failed to ignore 2nd start strike",
        "<html><body><p><em>first _second end1</em> end2_</p></body></html>",
        mt.convertToHTML("_first _second end1_ end2_"));

    // unit test for overlapping bold and italic
    Assert.assertEquals("failed to ignore 2nd start bold",
        "<html><body><p><strong>first <em>*second end1</strong> end2</em>*</p></body></html>",
        mt.convertToHTML("**first **second end1** end2**"));
    Assert.assertEquals("failed to convert from bold start to italic end when needed",
        "<html><body><p><em>text _</em>text</p></body></html>", mt.convertToHTML("_text __text"));
    Assert.assertEquals("failed to convert from bold end to italic at correct index ",
        "<html><body><p><em>text</em>_ </p></body></html>", mt.convertToHTML("_text__ "));
    // FIXME bug found
    // desired
    // org.junit.Assert.assertEquals("failed to convert from bold-start to italic start when needed",
    // "<html><body><p><em>_text</em> </p></body></html>", mt.convertToHTML(" __text_ "));
    // actual
    Assert.assertEquals("failed to convert from bold-start to italic start when needed",
        "<html><body><p>__text_ </p></body></html>", mt.convertToHTML(" __text_ "));
    // FIXME bug found in BoldMark as well -> bold start remains a bold start until the very end
    // because it
    // it did not find a match. remove unclosed marks should convert it to an italic start, but
    // italic end
    // has probably already been removed at this point because it didn't have a matching start.
    // new regular bold tests to see if same bugs exist in BoldMark
    // desired
    // org.junit.Assert.assertEquals("failed to convert from bold-start to italic start when needed",
    // "<html><body><p><em>*text</em> </p></body></html>", mt.convertToHTML(" **text* "));
    // actual
    Assert.assertEquals("failed to convert from bold-start to italic start when needed",
        "<html><body><p>**text* </p></body></html>", mt.convertToHTML(" **text* "));



    // unit tests for links
    Assert.assertEquals("failed to generate simple link",
        "<html><body><p><a href=http://link>hi</a>textAt End</p></body></html>",
        mt.convertToHTML("[hi](http://link)textAt End"));
    Assert.assertEquals("failed to generate simple link",
        "<html><body><p><a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML("[hi](http://link)"));
    Assert.assertEquals("failed to ignore extra start",
        "<html><body><p>[<a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML("[[hi](http://link)"));
    Assert.assertEquals("failed to ignore extra start",
        "<html><body><p><a href=http://[link>hi</a></p></body></html>",
        mt.convertToHTML("[hi](http://[link)"));
    Assert.assertEquals("failed to ignore extra start",
        "<html><body><p><a href=http://link>hi</a>[</p></body></html>",
        mt.convertToHTML("[hi](http://link)["));
    Assert.assertEquals("failed to ignore extra middle",
        "<html><body><p>](<a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML("]([hi](http://link)"));
    Assert.assertEquals("failed to ignore extra middle",
        "<html><body><p><a href=http://](link>hi</a></p></body></html>",
        mt.convertToHTML("[hi](http://](link)"));
    Assert.assertEquals("failed to ignore extra middle",
        "<html><body><p><a href=http://link>hi</a>](</p></body></html>",
        mt.convertToHTML("[hi](http://link)]("));
    Assert.assertEquals("failed to ignore extra end",
        "<html><body><p>)<a href=http://link>hi</a></p></body></html>",
        mt.convertToHTML(")[hi](http://link)"));
    Assert.assertEquals("failed to ignore extra end",
        "<html><body><p><a href=http://link>hi)</a></p></body></html>",
        mt.convertToHTML("[hi)](http://link)"));
    Assert.assertEquals("failed to ignore extra end",
        "<html><body><p><a href=http://link>hi</a>)</p></body></html>",
        mt.convertToHTML("[hi](http://link))"));
    // TODO tests that it ignores extra link ends,


    // tests for code blocks
    Assert.assertEquals("failed to find simple codeblock of multiple lines",
        "<html><body><pre><code>line1\r\nline2\r\nline3\r\n</code></pre></body></html>",
        mt.convertToHTML("    line1\r\n    line2\r\n    line3\r\n"));
    Assert.assertEquals("failed to find simple codeblock",
        "<html><body><pre><code>line1\r\n</code></pre></body></html>",
        mt.convertToHTML("    line1\r\n"));
    Assert
    .assertEquals(
        "failed to ignore markdown within codeblock",
        "<html><body><pre><code>codeblock with *italic* and **bold** marks\r\n</code></pre></body></html>",
        mt.convertToHTML("    codeblock with *italic* and **bold** marks"));
    Assert
    .assertEquals(
        "failed to ignore markdown within codeblock 2",
        "<html><body><pre><code>code **bold**  and*italic* and\r\nstrike~~through~~\r\n</code></pre></body></html>",
        mt.convertToHTML("    code **bold**  and*italic* and\r\n    strike~~through~~"));
    Assert
    .assertEquals(
        "failed to render codeblock properly",
        "<html><body><pre><code>startcode \r\n**continue** code \r\n~~continue~~ code \r\n</code></pre><p>regular text</p></body></html>",
        mt.convertToHTML("    startcode \r\n    **continue** code \r\n    ~~continue~~ code \r\nregular text"));


    // tests for lists
    Assert
    .assertEquals(
        "failed simple list",
        "<html><body><ul><li>list1 </li><li>list2</li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
        mt.convertToHTML("* list1 \r\n* list2\r\n* list3\r\n* list4\r\n\r\nregular text now\r\n#header now"));
    Assert
    .assertEquals(
        "failed multiple sublists dashes",
        "<html><body><ul><li>list1 </li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
        mt.convertToHTML("- list1 \r\n- list2\r\n  - list2a\r\n   - list2ai\r\n     - list2aii\r\n- list3\r\n- list4\r\n\r\nregular text now\r\n#header now"));
    Assert
    .assertEquals(
        "failed multiple sublists pluses",
        "<html><body><ul><li>list1 </li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
        mt.convertToHTML("+ list1 \r\n+ list2\r\n  + list2a\r\n   + list2ai\r\n     + list2aii\r\n+ list3\r\n+ list4\r\n\r\nregular text now\r\n#header now"));
    Assert
        .assertEquals(
            "failed orederd list",
            "<html><body><ol><li>list1 </li><li>list2<ol><li>list2a<ol><li>list2ai<ul><li>list2aii</li></ul></li></ol></li></ol></li><li>list3</li><li>list4</li></ol><p>regular text now</p><h1>header now</h1></body></html>",
            mt.convertToHTML("134. list1 \r\n1. list2\r\n  1. list2a\r\n   424. list2ai\r\n     + list2aii\r\n+ list3\r\n+ list4\r\n\r\nregular text now\r\n#header now"));
    Assert
    .assertEquals(
        "failed multiple sublists astrix",
        "<html><body><ul><li>list1 </li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
        mt.convertToHTML("* list1 \r\n* list2\r\n  * list2a\r\n   * list2ai\r\n     * list2aii\r\n* list3\r\n* list4\r\n\r\nregular text now\r\n#header now"));
    Assert
    .assertEquals(
        "failed complex example",
        "<html><body><ul><li>list1 <h1>line1addition </h1></li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
        mt.convertToHTML("* list1 \r\n#line1addition \r\n* list2\r\n  * list2a\r\n   * list2ai\r\n     * list2aii\r\n* list3\r\n* list4\r\n\r\nregular text now\r\n#header now"));


    // tests for automatic links:
    Assert.assertEquals("failed to find automatic link",
        "<html><body><p><a href=http://www.google.com>http://www.google.com</a></p></body></html>",
        mt.convertToHTML("<http://www.google.com>"));

    Assert.assertEquals("utilized an invalid end",
        "<html><body><p><http://www.google<p>.com></p></body></html>",
        mt.convertToHTML("<http://www.google<p>.com>"));
    Assert
        .assertEquals(
            "utilized wrong start",
            "<html><body><p><a href=http<http://www.google.com>http<http://www.google.com</a></p></body></html>",
            mt.convertToHTML("<http<http://www.google.com>"));
    Assert
        .assertEquals(
            "failed to use closest end",
            "<html><body><p><a href=http://www.google.com>http://www.google.com</a>>>some text></p></body></html>",
            mt.convertToHTML("<http://www.google.com>>>some text>"));
    Assert
        .assertEquals(
            "failed to work with multiple links",
            "<html><body><p><a href=http://www.google.com>http://www.google.com</a><a href=http://www.google.com>http://www.google.com</a></p></body></html>",
            mt.convertToHTML("<http://www.google.com><http://www.google.com>"));
    Assert
    .assertEquals(
        "failed to ignore internal marks",
        "<html><body><p>some **text before<a href=http://www.google**.com>http://www.google**.com</a></p></body></html>",
        mt.convertToHTML("some **text before<http://www.google**.com>"));


    // tests for block quotes
    Assert
        .assertEquals(
            "failed to identify block quote",
            "<html><body><blockquote><p>a basic blockquote of two lines</p></blockquote></body></html>",
            mt.convertToHTML(">a basic blockquote of\r\ntwo lines"));
    Assert
        .assertEquals(
            "failed to remove starting >",
            "<html><body><blockquote><p>a basic blockquote of two lines</p></blockquote></body></html>",
            mt.convertToHTML(">a basic blockquote of\r\n>two lines"));
    Assert
    .assertEquals(
        "failed to nest blockquotes",
        "<html><body><blockquote><p>a nested blockquote</p><blockquote><p>nested component</p></blockquote></blockquote></body></html>",
        mt.convertToHTML(">a nested blockquote\r\n>>nested component"));
    Assert
    .assertEquals(
        "failed to end at proper spot",
        "<html><body><blockquote><p>a basic blockquote with multiple lines</p></blockquote><p>followed by regular text</p></body></html>",
        mt.convertToHTML(">a basic blockquote\r\nwith\r\nmultiple\r\nlines\r\n\r\nfollowed by regular text"));


    // horizontal rule tests
    Assert.assertEquals("HR with astrix", "<html><body><hr /></body></html>",
        mt.convertToHTML(" * * * * * *"));
    Assert.assertEquals("HR with underscore", "<html><body><hr /></body></html>",
        mt.convertToHTML("___"));
    Assert.assertEquals("HR with dash", "<html><body><hr /></body></html>",
        mt.convertToHTML(" ----"));
    Assert.assertEquals("not an HR if only 2 elements", "<html><body><p>_ _</p></body></html>",
        mt.convertToHTML("_ _"));

  }
}
