package wekey.interpreter.blockanalyzers;

import java.util.List;

/**
 * Classes that use the TextBlockAnalyzer Interface identify and interpret Markdown Block Elements
 *
 * @author Tim
 *
 */
public interface TextBlockAnalyzer {

  /**
   * Given a list of Strings split on line breaks of the text of a wiki file, an index that the text
   * block of this type starts at, and a list of text blocks encountered so far, it constructs a
   * text block until it reaches a line that does not belong or the end of the list at which point
   * it wraps the string with appropriate HTML tags and packages it into a TextBlockAnalysisResult
   * along with an adjusted index value to account for lines included in the text block
   * 
   * @param input a list of Strings split on linebreaks of the text of a wiki file
   * @param index the index within the list of strings that this text block type starts at
   * @param output a list of text blocks encountered so far
   * @return the full text block element of this type wrapped in the appropriate HTML tags and added
   *         to output packaged into a TextBlockAnalysisResult along with an index adjustment to
   *         account for lines added to the text block
   */
  public TextBlockAnalyzerResult interpret(List<String> input, int index, List<String> output);

  /**
   * Determines if a line could be a valid text block element of this type
   * 
   * @param input a Markdown formated String
   * @return true if input could be a valid text block element of this type, false otherwise
   */
  public Boolean isThisTextBlockType(String input);
}
