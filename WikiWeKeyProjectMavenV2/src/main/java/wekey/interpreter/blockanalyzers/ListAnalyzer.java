package wekey.interpreter.blockanalyzers;

import java.util.ArrayList;
import java.util.List;

import wekey.interpreter.MarkdownInterpreter;

/**
 * ListAnalyzer class identifies list components and formats them into HTML lists
 *
 * @author Tim
 *
 */
public class ListAnalyzer implements TextBlockAnalyzer {

  private static final String BREAK_HTML = "<br />";
  private static final String NUMBERED_REGEX = "^\\s*[0-9]+\\.\\s";
  private static final String DASH_BULLET_REGEX = "^\\s*-\\s";
  private static final String PLUS_BULLET_REGEX = "^\\s*\\+\\s";
  private static final String STAR_BULLET_REGEX = "^\\s*\\*\\s";
  private static final String LIST_ELEMENT_OPEN = "<li>";
  private static final String LIST_ELEMENT_CLOSE = "</li>";
  private static final String ORDERED_LIST_HTML_OPEN = "<ol><li>";
  private static final String UNORDEREDLIST_HTML_OPEN = "<ul><li>";
  private static final String SPACE = " ";
  private static final String UNORDEREDLIST_HTML_CLOSE = "</li></ul>";
  private static final String ORDEREDLIST_HTML_CLOSE = "</li></ol>";
  private static final String EMPTYSTRING = "";
  private static final String STARLISTREGEX = "\\s*\\*\\s.*";
  private static final String PLUSLISTREGEX = "\\s*\\+\\s.*";
  private static final String DASHLISTREGEX = "\\s*-\\s.*";
  private static final String ORDEREDLISTREGEX = "\\s*[0-9]+\\.\\s.*";
  private static final String TWO_SPACE = "  ";

  /**
   * Starting at the given index, formats the strings in the list of strings into an HTML lists
   * until a horizontal row is indicated or until a blank line followed by another text block
   * element is encountered this into a TextBlockAnalyzerResult.
   *
   * @param input a list of strings
   * @param index the index at which the list is to start
   * @param output a list of strings representing blocks of texts encountered so far
   * @return a TextBlockAnalyzerResult where the list lines of input are formated as an HTML list,
   *         added to output and put into the TextBlockAnalyzerResult as a resultList as well as an
   *         updated index to account for lines included in the lists.
   */
  @Override
  public TextBlockAnalyzerResult interpret(List<String> input, int index, List<String> output) {
    List<String> currentListElement = new ArrayList<String>();
    List<String> thisRecursion = new ArrayList<String>();
    List<String> finalResult = new ArrayList<String>();
    HorizontalRule hr = new HorizontalRule();
    Boolean isOrderedList = false;
    int spaces = 0;
    for (int i = index; i < input.size(); i++) {
      String currentLine = input.get(i);
      Boolean isBlankLine = currentLine.trim().isEmpty();
      Boolean isList = isThisTextBlockType(currentLine);
      Boolean isHR = hr.isThisTextBlockType(currentLine);
      if (isHR) {
        thisRecursion = recurseOver(currentListElement);
        for (String element : thisRecursion) {
          finalResult.add(element);
        }
        addListEndHTML(finalResult, isOrderedList);
        for (String element : finalResult) {
          output.add(element);
        }
        TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, i - 1);
        return result;
      } else if (isBlankLine) {
        thisRecursion = recurseOver(currentListElement);
        for (String element : thisRecursion) {
          finalResult.add(element);
        }
        addListEndHTML(finalResult, isOrderedList);
        for (String element : finalResult) {
          output.add(element);
        }
        TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, i - 1);
        return result;
      } else if (isList && (i == index)) {
        isOrderedList = testIfOrderedList(currentLine);
        spaces = countSpaces(currentLine);
        String beginListHTML = UNORDEREDLIST_HTML_OPEN;
        if (isOrderedList) {
          beginListHTML = ORDERED_LIST_HTML_OPEN;
        }
        currentLine = beginListHTML + removeListMarkdown(currentLine);
        currentLine = addBreakIfNeeded(currentLine);
        currentListElement.add(currentLine);
      } else if (testIfListOnSameLevel(currentLine, spaces) && (i != index)) {
        thisRecursion = recurseOver(currentListElement);
        for (String element : thisRecursion) {
          finalResult.add(element);
        }
        finalResult.set(finalResult.size() - 1, finalResult.get(finalResult.size() - 1)
            + LIST_ELEMENT_CLOSE);
        currentListElement.clear();
        currentLine = LIST_ELEMENT_OPEN + removeListMarkdown(currentLine);
        currentLine = addBreakIfNeeded(currentLine);
        currentListElement.add(currentLine);
      } else {
        currentListElement.add(currentLine);
      }
    }
    thisRecursion = recurseOver(currentListElement);
    for (String element : thisRecursion) {
      finalResult.add(element);
    }
    addListEndHTML(finalResult, isOrderedList);
    for (String element : finalResult) {
      output.add(element);
    }
    TextBlockAnalyzerResult result = new TextBlockAnalyzerResult(output, input.size());
    return result;
  }

  /**
   * Given a bulleted/numbered element of a list, adds a linebreak if it has two or more spaces at
   * the end
   *
   * @param listLine a line of a list that isn't part of a nested block element
   * @return the line with a HTML line break added if the line has two or more spaces at the end
   */
  private String addBreakIfNeeded(String listLine) {
    if (listLine.endsWith(TWO_SPACE)) {
      listLine = listLine + BREAK_HTML;
    }
    return listLine;
  }

  /**
   * Given a list of Strings to be included into a list and a Boolean where true indicates the list
   * is ordered and false unordered, adds the correct closing tags to the last string according to
   * the list type
   *
   * @param resultList a list of strings composing an HTML formated list missing the closing HTML
   *        list tags
   * @param isOrderedList a Boolean where true indicates the list is ordered and false unordered
   * @return resultList with HTML list closing tags added to the last element according to the list
   *         type
   */
  private List<String> addListEndHTML(List<String> resultList, Boolean isOrderedList) {
    int endIndex = resultList.size() - 1;
    String lastElement = resultList.get(endIndex);
    if (isOrderedList) {
      resultList.set(endIndex, lastElement + ORDEREDLIST_HTML_CLOSE);
    } else {
      resultList.set(endIndex, lastElement + UNORDEREDLIST_HTML_CLOSE);
    }
    return resultList;
  }

  /**
   * Determines the number of spaces at the beginning of a string
   *
   * @param input any string
   * @return the number of spaces at the beginning of that string
   */
  private int countSpaces(String input) {
    int count = 0;
    while (input.startsWith(SPACE) && (input.length() > 1)) {
      input = input.substring(1, input.length());
      count++;
    }
    return count;
  }

  /**
   * returns true iff the starting sequence of a line indicates it could belong to a list, false
   * otherwise
   *
   * @param line a string without line breaks to be tested if it belongs to a list
   * @return true if the starting sequence of line indicates it could belong to a list, false
   *         otherwise
   */
  @Override
  public Boolean isThisTextBlockType(String line) {
    HorizontalRule hr = new HorizontalRule();
    return !hr.isThisTextBlockType(line)
        && (line.matches(STARLISTREGEX) || line.matches(PLUSLISTREGEX)
            || (line.matches(DASHLISTREGEX)) || line.matches(ORDEREDLISTREGEX));
  }

  /**
   * returns true iff the starting sequence of a line indicates it could belong to an ordered list,
   * false otherwise
   *
   * @param line a string without line breaks to be tested if it belongs to an ordered list
   * @return true if the starting sequence of line indicates it could belong to an ordered list,
   *         false otherwise
   */
  private Boolean testIfOrderedList(String line) {
    return line.matches(ORDEREDLISTREGEX);
  }

  /**
   * Removes the markdown tag from a list
   *
   * @param input a String that is one of the bulleted or numbered elements of a list
   * @return input without leading whitespace and without the markdown list tag (*, +, -, [0-9].)
   */
  private String removeListMarkdown(String input) {
    if (input.matches(STARLISTREGEX)) {
      input = input.replaceAll(STAR_BULLET_REGEX, EMPTYSTRING);
    } else if (input.matches(PLUSLISTREGEX)) {
      input = input.replaceAll(PLUS_BULLET_REGEX, EMPTYSTRING);
    } else if (input.matches(DASHLISTREGEX)) {
      input = input.replaceAll(DASH_BULLET_REGEX, EMPTYSTRING);
    } else {
      input = input.replaceAll(NUMBERED_REGEX, EMPTYSTRING);
    }
    return input;
  }

  /**
   * Returns true if the given string represents a bulleted or numbered list component of the same
   * level indicated by the indent
   *
   * @param line a string without line breaks to be tested if it belongs to a list at a given level
   *        of nesting
   * @param indent an indent value that determines the level of nesting
   * @return true if the starting sequence of line indicates it could belong to a list and if it's
   *         indent matches indent, false otherwise
   */
  private Boolean testIfListOnSameLevel(String line, int indent) {
    return (indent == countSpaces(line)) && isThisTextBlockType(line);
  }

  /**
   * takes the lines of a list, checks them for text block elements, and returns a new list of
   * Strings with the lines formated into HTML text blocks
   *
   * @param intermediateResult a list of Strings representing lines that make up a list
   * @return a new list of strings containing intermediateResult strings arranged into text blocks
   *         with HTML formating
   */
  private List<String> recurseOver(List<String> intermediateResult) {
    MarkdownInterpreter mi = new MarkdownInterpreter();
    List<String> recursionResult = new ArrayList<String>();
    recursionResult = mi.splitTextByBlocks(intermediateResult, true);
    return recursionResult;
  }

  /**
   * Takes a string and determines if it has HTML tags that identify it as a list component
   *
   * @param input any string
   * @return true iff the string starts with an HTML tag identifying it as a list component, false
   *         otherwise
   */
  public Boolean isBulletedListComponent(String input) {
    return input.startsWith(LIST_ELEMENT_OPEN) || input.startsWith(UNORDEREDLIST_HTML_OPEN)
        || input.startsWith(ORDERED_LIST_HTML_OPEN);
  }


}
