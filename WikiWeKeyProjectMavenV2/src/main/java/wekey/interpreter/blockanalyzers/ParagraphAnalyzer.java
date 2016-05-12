package wekey.interpreter.blockanalyzers;

import java.util.List;

/**
 * ParagraphAnalyzer class identifies paragraph components and formats them into HTML paragraphs
 *
 */
public class ParagraphAnalyzer implements TextBlockAnalyzer {

  private static final String BREAK_HTML = "<br />";
  private static final String BLOCK_QUOTE_REGEX = "\\s*>.*";
  private static final String HEADERSTART_MARKDOWN = "#";
  private static final String LEADING_WHITESPACE_REGEX = "^\\s*";
  private static final String SPACE = " ";
  private static final String TWO_SPACE = "  ";
  private static final String PARAGRAPH_HTML_OPEN = "<p>";
  private static final String PARAGRAPH_HTML_CLOSE = "</p>";
  private static final String EMPTYSTRING = "";

  /**
   * Starting at the given index, formats the strings in the list of strings into an HTML paragraph
   * until another text block type is encountered. The resulting components are put in a textblock
   * analysis is finished and put into a TextBlockAnalyzerResult along with an index offset to
   * adjust for the number of lines included.
   *
   * @param input a list of strings
   * @param index the index at which the paragraph is to start
   * @param output a list of strings representing blocks of texts encountered so far
   * @return a TextBlockAnalyzerResult where the paragraph lines of input are formated as an HTML
   *         paragraphs, added to output and put into the TextBlockAnalyzerResult as a resultList as
   *         well as an updated index to account for lines included in the paragraph.
   */
  @Override
  public TextBlockAnalyzerResult interpret(List<String> input, int index, List<String> output) {
    String finalResult = EMPTYSTRING;
    for (int i = index; i < input.size(); i++) {
      HorizontalRule hr = new HorizontalRule();
      String currentLine = input.get(i);
      Boolean isBlankLine = currentLine.trim().isEmpty();
      Boolean isBlockQuote = currentLine.matches(BLOCK_QUOTE_REGEX);
      Boolean isHeader = currentLine.startsWith(HEADERSTART_MARKDOWN);
      Boolean isHR = hr.isThisTextBlockType(currentLine);
      if (isBlankLine || isHR || isHeader || isBlockQuote) {
        finalResult =
            PARAGRAPH_HTML_OPEN + finalResult.replaceAll(LEADING_WHITESPACE_REGEX, EMPTYSTRING)
            + PARAGRAPH_HTML_CLOSE;
        output.add(finalResult);
        TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, i - 1);
        return result;
      } else if (finalResult.isEmpty()) {
        finalResult = finalResult + currentLine;
      } else {
        if (finalResult.endsWith(TWO_SPACE)) {
          finalResult = finalResult + BREAK_HTML + currentLine;
        } else {
          finalResult = finalResult + SPACE + currentLine;
        }
      }
    }
    finalResult =
        PARAGRAPH_HTML_OPEN + finalResult.replaceAll(LEADING_WHITESPACE_REGEX, EMPTYSTRING)
        + PARAGRAPH_HTML_CLOSE;
    output.add(finalResult);
    TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, input.size() - 1);
    return result;
  }

  // anything that is not another block element is by default a paragraph
  @Override
  public Boolean isThisTextBlockType(String input) {
    return true;
  }
}
