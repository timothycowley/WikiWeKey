package wekey.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wekey.interpreter.blockanalyzers.BlockQuoteAnalyzer;
import wekey.interpreter.blockanalyzers.CodeBlockAnalyzer;
import wekey.interpreter.blockanalyzers.HeaderAnalyzer;
import wekey.interpreter.blockanalyzers.HorizontalRule;
import wekey.interpreter.blockanalyzers.ListAnalyzer;
import wekey.interpreter.blockanalyzers.ParagraphAnalyzer;
import wekey.interpreter.blockanalyzers.TextBlockAnalyzerResult;
import wekey.interpreter.marks.AutoLinkMark;
import wekey.interpreter.marks.BoldMark;
import wekey.interpreter.marks.BoldUnderscoreMark;
import wekey.interpreter.marks.CodeMark;
import wekey.interpreter.marks.ItalicMark;
import wekey.interpreter.marks.ItalicUnderscoreMark;
import wekey.interpreter.marks.LinkMark;
import wekey.interpreter.marks.Mark;
import wekey.interpreter.marks.MarkAnalysisResult;
import wekey.interpreter.marks.StrikethroughMark;

// > Somewhat complex solution. Lots of substring and indexes floating around.
// > Could use some refactoring as well.

/**
 * MarkdownInterpreter contains methods to interpret the Wiki MarkUp Language Markdown as HTML
 * formated text
 *
 */
public class MarkdownInterpreter implements Interpreter {

  private final static String STARTDOC = "<html><body>";
  private final static String ENDDOC = "</body></html>";
  private final static String ESCAPELESSTHAN = "&lt;";
  private final static String ESCAPEAMP = "&amp;";
  private final static String SPACE = " ";
  private final static String TAB = "\t";
  private final static String AMP = "&";
  private final static String LESSTHAN = "<";
  private final static String SPLITREGEX = "[\r][\n]"; // > I see so you are dealing with both.
  private final static String EMPTYSTRING = "";

  // Initialize MarkReaders
  // TODO Planned on refactoring to make better use of this so that all Mark classes appear only
  // here

  // > why are these fields not initialized with a constructor?
  // > -1
  StrikethroughMark strike = new StrikethroughMark();
  BoldMark bold = new BoldMark();
  ItalicMark italic = new ItalicMark();
  BoldUnderscoreMark boldUnderscore = new BoldUnderscoreMark();
  ItalicUnderscoreMark italicUnderscore = new ItalicUnderscoreMark();
  LinkMark link = new LinkMark();
  CodeMark code = new CodeMark();
  AutoLinkMark alink = new AutoLinkMark();
  List<Mark> readerMarks = Arrays.asList(this.alink, this.code, this.link, this.strike, this.bold,
      this.italic, this.boldUnderscore, this.italicUnderscore);

  /**
   * Takes Markdown text string and returns an HTML text string
   *
   * <pre>
   * Pre: Markdown formatted string with line breaks marked with "\r\n"
   * </pre>
   *
   * <pre>
   * Post: HTML formatted string corresponding to input string
   * </pre>
   *
   * @param input a String in Markdown format
   * @return a String in HTML format corresponding to input
   */
  @Override
  public String convertToHTML(final String input) {
    // > This looks like the method that deals with a whole file since STARTDOC and ENDDOC are
    // injected.
    // > Javadoc should make that clear.
    String htmlFormated = STARTDOC;
    List<String> textBlocks = generateLists(input);
    textBlocks = splitTextByBlocks(textBlocks, false);
    for (final String aTextBlock : textBlocks) {
      List<Mark> markList = makeMarkList(aTextBlock);
      markList = interpretMarkList(markList);
      markList = removeUnclosedStarts(markList);
      String resultTextBlock = markListToHTML(aTextBlock, markList);
      resultTextBlock = escapeSpecial(resultTextBlock);
      htmlFormated = htmlFormated + resultTextBlock;
    }
    htmlFormated = htmlFormated + ENDDOC;
    return htmlFormated;
  }

  /**
   * Converts a string into a list of strings split on the line breaks
   *
   * @param input any string
   * @return a list of strings consisting of the input string split on the line breaks.
   */
  private List<String> generateLists(final String input) {
    final String[] tokens = input.split(SPLITREGEX);
    // > this regexp will split the line but also
    // > remove the newline markers. Perhaps this
    // > behaviour is contributing to one of the
    // > bugs, no. 4

    final List<String> stringResultList = new ArrayList<String>();
    for (final String atoken : tokens) {
      stringResultList.add(atoken);
    }
    return stringResultList;
  }

  // HANDLING BLOCK ELEMENTS
  /**
   * Takes a list of strings corresponding to each line of markdown text and returns a list of
   * strings where each string corresponds to a full paragraph, header, code block, horizontal row,
   * or the elements of a list or block quote
   *
   * <pre>
   * Pre: A list of String containing each line of markdown text
   * </pre>
   *
   * <pre>
   * Post: Input list of String with some whitespace trimmed, lines appended into text blocks, and with text block HTML tags added
   * </pre>
   *
   * @param tokens a list of Strings corresponding to each line of markdown text
   *
   *        //> what is isListRecursion, how is it being used?
   *
   *        //> -1
   * @return a list of Strings corresponding to each text block, meaning each String is a full
   *         paragraph, header, code block, horizontal row, or elements of a list or block quote
   *
   */
  @Override
  public List<String> splitTextByBlocks(final List<String> tokens, final Boolean isListRecursion) {
    List<String> output = new ArrayList<String>();
    for (int i = 0; i < tokens.size(); i++) {
      final Boolean blankline = tokens.get(i).trim().isEmpty();
      final CodeBlockAnalyzer codeAnalyzer = new CodeBlockAnalyzer();
      final Boolean codeBlockTest = codeAnalyzer.isThisTextBlockType(tokens.get(i));
      final HorizontalRule horizontalRule = new HorizontalRule();
      final Boolean hr = horizontalRule.isThisTextBlockType(tokens.get(i));
      final HeaderAnalyzer headerAnalyzer = new HeaderAnalyzer();
      final Boolean headerstart = headerAnalyzer.isThisTextBlockType(tokens.get(i));
      final ListAnalyzer listAnalyzer = new ListAnalyzer();
      final Boolean listTest = listAnalyzer.isThisTextBlockType(tokens.get(i));
      final BlockQuoteAnalyzer blockQuoteAnalyzer = new BlockQuoteAnalyzer();
      final Boolean isBlockQuote = blockQuoteAnalyzer.isThisTextBlockType(tokens.get(i));
      final Boolean isPartOfListRecursion = listAnalyzer.isBulletedListComponent(tokens.get(i));
      if (blankline) {
        tokens.set(i, EMPTYSTRING);
      } else if (isBlockQuote) {
        final TextBlockAnalyzerResult blockQuoteResult =
            blockQuoteAnalyzer.interpret(tokens, i, output);
        output = blockQuoteResult.getResultList();
        i = blockQuoteResult.getIndexAdjustment();
      } else if (hr) {
        final TextBlockAnalyzerResult ruleResult = horizontalRule.interpret(tokens, i, output);
        output = ruleResult.getResultList();
        i = ruleResult.getIndexAdjustment();
      } else if (listTest) {
        final TextBlockAnalyzerResult listResult = listAnalyzer.interpret(tokens, i, output);
        output = listResult.getResultList();
        i = listResult.getIndexAdjustment();
      } else if (codeBlockTest && !isListRecursion) { // > this is the only case that check
                                                      // isListRecursion, why is that?
        final TextBlockAnalyzerResult codeResult = codeAnalyzer.interpret(tokens, i, output);
        output = codeResult.getResultList();
        i = codeResult.getIndexAdjustment();
      } else if (headerstart) {
        final TextBlockAnalyzerResult headerResult = headerAnalyzer.interpret(tokens, i, output);
        output = headerResult.getResultList();
        i = headerResult.getIndexAdjustment();
      } else if (isPartOfListRecursion) {
        output.add(tokens.get(i));
      } else {
        final ParagraphAnalyzer aparagraph = new ParagraphAnalyzer();
        final TextBlockAnalyzerResult paragraphResult = aparagraph.interpret(tokens, i, output);
        output = paragraphResult.getResultList();
        i = paragraphResult.getIndexAdjustment();
      }
    }
    return output;
  }

  // HANDLING SPAN ELEMENTS
  /**
   * Given a block of text, returns a list of Marks to be converted into HTML
   *
   * <pre>
   * Pre: a block of text
   * </pre>
   *
   * <pre>
   * Post: a list of all markdown Marks that can be converted to HTML in the block of text
   * </pre>
   *
   * @param textBlock any full block of text (e.g. paragraphs, headers)
   * @return a list of all markdown Marks in the block of text
   */
  private List<Mark> makeMarkList(final String textBlock) {
    List<Mark> resultList = new ArrayList<Mark>();
    for (int i = 0; i < textBlock.length(); i++) {
      for (final Mark reader : this.readerMarks) {
        // TODO have Interpreter skip ahead when a Mark is found to reduce amount of reading of
        // string
        resultList = reader.readMarkdown(textBlock.substring(i), resultList, i);
      }
    }
    return resultList;
  }

  /**
   * Given a list of Marks, evaluates each Mark based on the context of other Marks to determine how
   * it should be interpreted. Ex: For example, if ** has text on both sides it could indicate
   * either the start or end of bold, this method looks to see what comes before and decides which,
   * if the Mark should be ignored, or even if it should be transformed into an ItalicMark
   *
   * <pre>
   * Pre: a list of Marks
   * </pre>
   *
   * <pre>
   * Post: the list of altered to clarify all ambiguities
   * </pre>
   *
   * @param markList a list of Marks with possibly ambiguous Marks or uninterpretable Marks
   * @return markList altered to clarify all ambiguities such that the HTML to be added can be
   *         determined solely on an individual Mark's properties
   */
  private List<Mark> interpretMarkList(List<Mark> markList) {
    for (int i = 0; i < markList.size(); i++) {
      final MarkAnalysisResult result = markList.get(i).analyzeMarkList(markList, i);
      markList = result.getMarkList();
      i = i + result.getIndexAdjustment();
    }
    return markList;
  }

  /**
   * Removes all unclosed Marks from a list of Marks
   *
   * <pre>
   * Pre: assumes there are no unclosed Marks prior to the last end Mark of each Mark type
   * </pre>
   *
   * @param markList a list of Marks
   * @return markList with all unclosed Marks removed
   */
  private List<Mark> removeUnclosedStarts(List<Mark> markList) {
    final ItalicMark italic = new ItalicMark();
    final ItalicUnderscoreMark italicUnderscore = new ItalicUnderscoreMark();
    final BoldMark bold = new BoldMark();
    final BoldUnderscoreMark boldUnderscore = new BoldUnderscoreMark();
    final StrikethroughMark strike = new StrikethroughMark();
    markList = bold.removeUnclosedMarkStarts(markList);
    markList = boldUnderscore.removeUnclosedMarkStarts(markList);
    markList = italic.removeUnclosedMarkStarts(markList);
    markList = italicUnderscore.removeUnclosedMarkStarts(markList);
    markList = strike.removeUnclosedMarkStarts(markList);
    return markList;
  }

  /**
   * Given a block of text and a list of all markdown Marks to be converted to HTML, it returns a
   * string representing the block of text with the Marks from the list converted into the
   * corresponding HTML
   *
   * <pre>
   * Pre: a block of text and a list of Marks corresponding to markdown marks within the text
   * </pre>
   *
   * <pre>
   * Post: returns a string where possible the marks are replaced by HTML tags
   * </pre>
   *
   * @param textBlock a string representing a block of text (e.g. paragraphs and headers)
   * @param marklist a list of all Marks within the textBlock to be converted to HTML
   * @return a string like textBlock, but with all the marks that correspond to ones on marklist
   *         replaced with the corresponding HTML
   */
  private String markListToHTML(final String textBlock, final List<Mark> marklist) {
    if (marklist.size() == 0) {
      return textBlock;
    }
    String result = textBlock.substring(0, marklist.get(0).getPosition());
    while (marklist.size() > 0) {
      result = result + marklist.get(0).toHTML(textBlock, marklist);
      marklist.remove(0);
    }
    return result;
  }

  // ESCAPING SPECIAL CHARACTERS
  /**
   *
   * Takes any string and returns the same string but with all "&" escaped and with
   * "<" escaped when occurring at the end of a line or before whitespace.
   * <pre>pre: any string</pre>
   * <pre>post: a string with all "&" replaced with "&amp;" and with "<" replaced with
   * "&lt; when it is followed by whitespace or at the end of a line"</pre>
   * @param textToEscape any String
   * @return a String with & and < escaped (when < is at the end of a line or
   * followed by whitespace)
   */
  private String escapeSpecial(String textToEscape) {
    textToEscape = textToEscape.replace(AMP, ESCAPEAMP);
    textToEscape = textToEscape.replaceAll(LESSTHAN + SPACE, ESCAPELESSTHAN + SPACE);
    textToEscape = textToEscape.replaceAll(LESSTHAN + TAB, ESCAPELESSTHAN + TAB);
    textToEscape = textToEscape.replace(LESSTHAN + LESSTHAN, ESCAPELESSTHAN + LESSTHAN);
    return textToEscape;
  }
}
