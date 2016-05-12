package wekey.interpreter.blockanalyzers;

import java.util.List;

public class HorizontalRule implements TextBlockAnalyzer {

  private static final String HORIZONTAL_RULE_HTML = "<hr />";
  private static final String ALL_UNDERSCORE_REGEX = "_{3}_*";
  private static final String ALL_DASH_REGEX = "-{3}-*";
  private static final String ALL_STAR_REGEX = "\\*{3}\\**";
  private static final String EMPTYSTRING = "";
  private final static String WHITESPACE = "\\s";

  @Override
  public TextBlockAnalyzerResult interpret(List<String> input, int index, List<String> output) {
    output.add(HORIZONTAL_RULE_HTML);
    TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, index);
    return result;
  }

  @Override
  public Boolean isThisTextBlockType(String input) {
    String testString = input.replaceAll(WHITESPACE, EMPTYSTRING);
    Boolean allStars = testString.matches(ALL_STAR_REGEX);
    Boolean allDash = testString.matches(ALL_DASH_REGEX);
    Boolean allUnderscore = testString.matches(ALL_UNDERSCORE_REGEX);
    return allStars || allDash || allUnderscore;
  }
}
