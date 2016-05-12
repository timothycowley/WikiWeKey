package wekey.interpreter.blockanalyzers;

import java.util.ArrayList;
import java.util.List;

import wekey.interpreter.MarkdownInterpreter;

/**
 * BlockQuoteAnalyzer class identifies block quote components and formats them into HTML block
 * quotes
 *
 * @author Tim
 *
 */
public class BlockQuoteAnalyzer implements TextBlockAnalyzer {

  private static final String BLOCKQUOTEMARK = "^\\s*>";
  private static final String EMPTYSTRING = "";
  private static final String BLOCKQUOTESTARTHTML = "<blockquote>";
  private static final String BLOCKQUOTEENDHTML = "</blockquote>";
  private static final String BLOCKQUOTEREGEX = "^\\s{0,3}>.*";

  /**
   * Starting at the given index, formats the strings in the list of strings into an HTML blockquote
   * until a blank line is encountered within the list or a horizontal row is indicated. Packages
   * this into a TextBlockAnalyzerResult.
   *
   * @param input a list of strings
   * @param index the index at which the blockquote is to start
   * @param output a list of strings representing blocks of texts encountered so far
   * @return a TextBlockAnalyzerResult where the block quote lines of input are formated as an HTML
   *         block quote, added to output and put into the TextBlockAnalyzerResult as a resultList
   *         as well as an updated index to account for lines included in the block quote.
   */
  @Override
  public TextBlockAnalyzerResult interpret(List<String> input, int index, List<String> output) {
    List<String> intermediateResult = new ArrayList<String>();
    List<String> finalResult = new ArrayList<String>();
    HorizontalRule hr = new HorizontalRule();
    for (int i = index; i < input.size(); i++) {
      String currentLine = input.get(i);
      Boolean isBlankLine = currentLine.trim().isEmpty();
      Boolean isHR = hr.isThisTextBlockType(currentLine);
      if (isBlankLine || isHR) {
        finalResult = recurseOver(intermediateResult);
        for (String element : finalResult) {
          output.add(element);
        }
        TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, i);
        return result;
      } else {
        currentLine = currentLine.replaceAll(BLOCKQUOTEMARK, EMPTYSTRING);
        intermediateResult.add(currentLine);
      }
    }
    finalResult = recurseOver(intermediateResult);
    for (String element : finalResult) {
      output.add(element);
    }
    TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, input.size() - 1);
    return result;
  }

  /**
   * takes the lines of a block quote, checks them for text block elements, and returns a new list
   * of Strings with the lines formated into HTML text blocks
   *
   * @param intermediateResult a list of Strings representing lines that make up a blockquote
   * @return a new list of strings containing intermediateResult strings arranged into textblocks
   *         with HTML formating
   */
  private List<String> recurseOver(List<String> intermediateResult) {
    MarkdownInterpreter mi = new MarkdownInterpreter();
    List<String> recursionResult = new ArrayList<String>();
    recursionResult = mi.splitTextByBlocks(intermediateResult, true);
    recursionResult.set(0, BLOCKQUOTESTARTHTML + recursionResult.get(0));
    recursionResult.set(recursionResult.size() - 1, recursionResult.get(recursionResult.size() - 1)
        + BLOCKQUOTEENDHTML);
    return recursionResult;
  }

  /**
   * Given a Markdown formated string, determines if it is this block element type
   *
   * @param input any Markdown formated string
   * @return true iff the string forms a valid header, false otherwise
   */
  @Override
  public Boolean isThisTextBlockType(String input) {
    return input.matches(BLOCKQUOTEREGEX);
  }


}
