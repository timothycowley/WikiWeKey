package wekey.interpreter.blockanalyzers;

import java.util.ArrayList;
import java.util.List;

/**
 * CodeQuoteAnalyzer class identifies block quote components and formats them into HTML block quotes
 *
 */
public class CodeBlockAnalyzer implements TextBlockAnalyzer {


  private static final String LINE_BREAK = "\r\n";
  private static final String EMPTYSTRING = "";
  private static final String FOUR_SPACE_REGEX = "^\\s{4}";
  private static final String CODE_HTML_CLOSE = "</code></pre>";
  private static final String CODE_HTML_OPEN = "<pre><code>";
  private static final String FOUR_SPACES = "    ";
  private final static String TAB_REGEX = "\\t.*";

  /**
   * Starting at the given index, formats the strings in the list of strings into an HTML code block
   * as long as the lines start with 4 indents. As soon as a line that is not indented 4 times is
   * encountered the code block is finished and put into a TextBlockAnalyzerResult along with an
   * index offset to adjust for the number of lines included.
   *
   * @param input a list of strings
   * @param index the index at which the code block is to start within a list of strings
   * @param output a list of strings representing blocks of texts encountered so far
   * @return a TextBlockAnalyzerResult where the block quote lines of input are formated as an HTML
   *         code block, added to output and put into the TextBlockAnalyzerResult as a resultList as
   *         well as an updated index to account for lines included in the code block.
   */
  @Override
  public TextBlockAnalyzerResult interpret(List<String> input, int index, List<String> output) {
    List<String> finalResult = new ArrayList<String>();
    for (int i = index; i < input.size(); i++) {
      String currentLine = input.get(i);
      Boolean isCodeBlock = isThisTextBlockType(currentLine) || currentLine.trim().isEmpty();
      String finalString = EMPTYSTRING;
      if (!isCodeBlock) {
        String finalStart = CODE_HTML_OPEN + finalResult.get(0);
        finalResult.set(0, finalStart);
        String finalEnd = finalResult.get(finalResult.size() - 1) + CODE_HTML_CLOSE;
        finalResult.set(finalResult.size() - 1, finalEnd);
        for (String element : finalResult) {
          finalString = finalString + element;
        }
        output.add(finalString);
        TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, i - 1);
        return result;
      } else {
        currentLine = currentLine.replaceAll(FOUR_SPACE_REGEX, EMPTYSTRING);
        finalResult.add(currentLine + LINE_BREAK);
      }
    }
    String finalStart = CODE_HTML_OPEN + finalResult.get(0);
    finalResult.set(0, finalStart);
    String finalEnd = finalResult.get(finalResult.size() - 1) + CODE_HTML_CLOSE;
    finalResult.set(finalResult.size() - 1, finalEnd);
    String finalString = EMPTYSTRING;
    for (String element : finalResult) {
      finalString = finalString + element;
    }
    output.add(finalString);
    TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, input.size() - 1);
    return result;
  }

  /**
   * Given a line of text returns true if it is a possible line of a code block
   *
   * <pre>
   * Pre: any string
   * </pre>
   *
   * <pre>
   * Post: true iff the string is a possible line of a code block
   * </pre>
   *
   * @param input any string
   * @return true iff the string starts with a tab or 4 spaces
   */
  @Override
  public Boolean isThisTextBlockType(String input) {
    return input.startsWith(FOUR_SPACES) || input.matches(TAB_REGEX);
  }
}
