package wekey.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ListTest {
  MarkdownInterpreter mt = new MarkdownInterpreter();

  @Test
  public void test1() {
    Assert
        .assertEquals(
            "failed simple list",
            "<html><body><ul><li>list1 </li><li>list2</li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
            mt.convertToHTML("* list1 \r\n* list2\r\n* list3\r\n* list4\r\n\r\nregular text now\r\n#header now"));
  }

  @Test
  public void test2() {
    Assert
        .assertEquals(
            "failed multiple sublists with dashes",
            "<html><body><ul><li>list1 </li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
            mt.convertToHTML("- list1 \r\n- list2\r\n  - list2a\r\n   - list2ai\r\n     - list2aii\r\n- list3\r\n- list4\r\n\r\nregular text now\r\n#header now"));
  }

  @Test
  public void test3() {
    Assert
        .assertEquals(
            "failed multiple sublists with pluses",
            "<html><body><ul><li>list1 </li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
            mt.convertToHTML("+ list1 \r\n+ list2\r\n  + list2a\r\n   + list2ai\r\n     + list2aii\r\n+ list3\r\n+ list4\r\n\r\nregular text now\r\n#header now"));
  }

  @Test
  public void test4() {
    Assert
        .assertEquals(
            "failed ordered lists",
            "<html><body><ol><li>list1 </li><li>list2<ol><li>list2a<ol><li>list2ai<ul><li>list2aii</li></ul></li></ol></li></ol></li><li>list3</li><li>list4</li></ol><p>regular text now</p><h1>header now</h1></body></html>",
            mt.convertToHTML("134. list1 \r\n1. list2\r\n  1. list2a\r\n   424. list2ai\r\n     + list2aii\r\n+ list3\r\n+ list4\r\n\r\nregular text now\r\n#header now"));
  }

  @Test
  public void test5() {
    Assert
        .assertEquals(
            "failed multiple sublists with astrix",
            "<html><body><ul><li>list1 </li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
            mt.convertToHTML("* list1 \r\n* list2\r\n  * list2a\r\n   * list2ai\r\n     * list2aii\r\n* list3\r\n* list4\r\n\r\nregular text now\r\n#header now"));
  }

  @Test
  public void test6() {
    Assert
        .assertEquals(
            "failed complex example",
            "<html><body><ul><li>list1 <h1>line1addition </h1></li><li>list2<ul><li>list2a<ul><li>list2ai<ul><li>list2aii</li></ul></li></ul></li></ul></li><li>list3</li><li>list4</li></ul><p>regular text now</p><h1>header now</h1></body></html>",
            mt.convertToHTML("* list1 \r\n#line1addition \r\n* list2\r\n  * list2a\r\n   * list2ai\r\n     * list2aii\r\n* list3\r\n* list4\r\n\r\nregular text now\r\n#header now"));
  }
}
