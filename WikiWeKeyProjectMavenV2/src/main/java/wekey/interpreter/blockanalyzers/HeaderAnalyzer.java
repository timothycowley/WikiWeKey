package wekey.interpreter.blockanalyzers;

import java.util.List;

public class HeaderAnalyzer implements TextBlockAnalyzer {

  private static final String MARKDOWNHEADER = "#";
  private static final int HEADERMAX = 6;
  private final static String GREATERTHAN = ">";
  private final static String WHITESPACEREGEX = "^\\s+";
  private final static String EMPTYSTRING = "";
  private final static String STARTHEADERTAG = "<h";
  private final static String STARTCLOSEHEADERTAG = "</h";


  /**
   * Takes a list of Strings where the String at the given index value starts with "#" and adds HTML
   * header tags where the header number corresponds to the number of # (1-6), adds it to the output
   * list, and packages it into a TextBlockAnalyzerResult along with the current index
   *
   * <pre>
   * pre: the first character in headertext must be "#"
   * </pre>
   *
   * <pre>
   * post: prefix "#"s removed up to the 6th and replaced with a HTML header tag where the number of
   * "#"s corresponds to the header number up to 6. And all suffix "#"s removed. String is closed
   * with the corresponding HTML header close tag and added to output list
   * </pre>
   *
   * @param input a list of Strings that contains a String that starts with "#"s
   * @param index the index value within the list of Strings of the String to be rendered as an HTML
   *        header
   * @param output a list of text block elements encountered so far
   * @return A TextBlockAnlalyzerResult where the String in input at the given index is formated as
   *         an HTML header and put in the output list, a list of text block elements encountered so
   *         far. The indexAdjustment of the TextBlockAnalyzerResult is set to be the same as the
   *         given index.
   */
  @Override
  public TextBlockAnalyzerResult interpret(List<String> input, int index, List<String> output) {
    String currentLine = input.get(index);
    int headervalue = 0;
    while (currentLine.startsWith(MARKDOWNHEADER) && (headervalue < HEADERMAX)) {
      headervalue++;
      currentLine = currentLine.substring(1);
    }
    while (currentLine.endsWith(MARKDOWNHEADER)) {
      currentLine = currentLine.substring(0, (currentLine.length() - 1));
    }
    currentLine = currentLine.replaceAll(WHITESPACEREGEX, EMPTYSTRING);
    String tag = STARTHEADERTAG + headervalue + GREATERTHAN;
    String endtag = STARTCLOSEHEADERTAG + headervalue + GREATERTHAN;
    output.add(tag + currentLine + endtag);

    TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, index);
    return result;
  }

  /**
   * Given a Markdown formated string, determines if it is this block element type
   *
   * @param input any Markdown formated string
   * @return true iff the string forms a valid header, false otherwise
   */
  @Override
  public Boolean isThisTextBlockType(String input) {
    return input.startsWith(MARKDOWNHEADER);
  }

}
