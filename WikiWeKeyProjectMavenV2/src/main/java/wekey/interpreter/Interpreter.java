package wekey.interpreter;

import java.util.List;

public interface Interpreter {


  /**
   * Takes a text string and returns an HTML text string
   *
   * <pre>
   * Pre: string with line breaks marked with "\r\n"
   * </pre>
   * 
   * //> but some OS do not use \r\n to indicate a newline, how does the code deal with those?
   *
   * <pre>
   * Post: HTML formatted string corresponding to input string
   * </pre>
   *
   * @param input a String
   * @return a String in HTML format corresponding to input
   */
  public String convertToHTML(String input);


  /**
   * Takes a list of strings corresponding to each line of text and returns a list of strings where
   * each string corresponds to a full text block element
   *
   * <pre>
   * Pre: A list of String containing each line of text
   * </pre>
   *
   * <pre>
   * Post: Input list of String with some formating and HTML tags added
   * </pre>
   *
   * @param tokens a list of Strings corresponding to each line of text
   *
   *        //> missing param for isListRecursion
   * @return a list of Strings corresponding to each text block element
   */
  public List<String> splitTextByBlocks(List<String> tokens, Boolean isListRecursion);

}
