package wekey.interpreter.marks;

import java.util.List;

/**
 * This Class identifies and interprets markdown automatic links and renders them as HTML links must
 * be web addresses (to link to a file path, use LinkMark instead)
 *
 * @author Tim
 *
 */
public class AutoLinkMark implements Mark {

  // variables
  private final String markType = "autolink";
  private int positionIndex;
  private String kind;

  // constants
  private static final int ABSENT = -1;
  private static final String EMPTYSTRING = "";
  private static final String HTML_END_LINK = "</a>";
  private static final String HTML_START_LINK = "<a href=";
  private static final String REGEX_HTML_BODY_CLOSE = ".*</body>.*";
  private static final String REGEX_HTML_BODY_OPEN = ".*<body>.*";
  private static final String REGEX_HTML_UNORDEREDLIST_CLOSE = ".*</ul>.*";
  private static final String REGEX_HTML_LIST_ELEMENT_CLOSE = ".*</li>.*";
  private static final String REGEX_HTML_CLOSE = ".*</html>.*";
  private static final String REGEX_HTML_PRE_CLOSE = ".*</pre>.*";
  private static final String REGEX_HTML_CODE_CLOSE = ".*</code>.*";
  private static final String REGEX_HTML_ORDEREDLIST_OPEN = ".*<ol>.*";
  private static final String REGEX_HTML_ORDEREDLIST_CLOSE = ".*</ol>.*";
  private static final String REGEX_HTML_HEADER6_CLOSE = ".*</h6>.*";
  private static final String REGEX_HTML_HEADER5_CLOSE = ".*</h5>.*";
  private static final String REGEX_HTML_HEADER4_CLOSE = ".*</h4>.*";
  private static final String REGEX_HTML_HEADER3_CLOSE = ".*</h3>.*";
  private static final String REGEX_HTML_HEADER2_CLOSE = ".*</h2>.*";
  private static final String REGEX_HTML_HEADER1_CLOSE = ".*</h1>.*";
  private static final String REGEX_HTML_PARAGRAPH_CLOSE = ".*</p>.*";
  private static final String REGEX_HTML_UNORDEREDLIST_OPEN = ".*<ul>.*";
  private static final String REGEX_HTML_LIST_ELEMENT_OPEN = ".*<li>.*";
  private static final String REGEX_HTML_OPEN = ".*<html>.*";
  private static final String REGEX_HTML_PRE_OPEN = ".*<pre>.*";
  private static final String REGEX_HTML_CODE_OPEN = ".*<code>.*";
  private static final String REGEX_HTML_HEADER6_OPEN = ".*<h6>.*";
  private static final String REGEX_HTML_HEADER5_OPEN = ".*<h5>.*";
  private static final String REGEX_HTML_HEADER4_OPEN = ".*<h4>.*";
  private static final String REGEX_HTML_HEADER3_OPEN = ".*<h3>.*";
  private static final String REGEX_HTML_HEADER2_OPEN = ".*<h2>.*";
  private static final String REGEX_HTML_HEADER1_OPEN = ".*<h1>.*";
  private static final String REGEX_HTML_PARAGRAPH_OPEN = ".*<p>.*";
  private static final String REGEX_WHITESPACE = ".*\\s.*";
  private static final String STARTKIND = "start";
  private static final String ENDKIND = "end";
  private static final String GREATERTHAN = ">";
  private static final String AUTOLINKSTARTSEQ = "<http";

  /**
   * The empty constructor of AutoLinkMark. Used for generating instances for reading markdown
   * strings
   */
  public AutoLinkMark() {}

  /**
   * Constructor of AutoLinkMark
   *
   * @param position the position within the text being analyzed of this Mark
   * @param kind the kind of this Mark, either STARTKIND: indicating a potential start of an
   *        automatic link or ENDKIND: indicating a potential automatic link end
   */
  public AutoLinkMark(final int position, final String kind) {
    this.positionIndex = position;
    this.kind = kind;
  }

  /**
   * Getter for the markType, which is the class of this Mark
   *
   * @return this markType
   */
  @Override
  public String getMarkType() {
    return this.markType;
  }

  /**
   * Getter for the positionIndex, which is the index of the Mark within the text being analyzed
   *
   * @return this positionIndex
   */
  @Override
  public int getPosition() {
    return this.positionIndex;
  }

  /**
   * Getter for kind, which is either STARTKIND: indicating a potential start of an automatic link
   * or ENDKIND: indicating a potential end of an automatic link
   *
   * @return this kind
   */
  @Override
  public String getKind() {
    return this.kind;
  }

  /**
   * Given a substring of a text block, a list of all marks encountered so far, the index of the
   * current position, and the length of the block of text the substring is from, determines what
   * AutoLinkMark to add to the total list, if any and returns it.
   *
   * <pre>
   * Pre: a substring of a text block, a list of all Marks encountered so far, the index of the current position, and the length of the block of text
   * </pre>
   *
   * <pre>
   * Post: returns the list of Marks either unchanged or with a AutoLinkMark added
   * </pre>
   *
   * @param input a substring of a text block
   * @param markList a list of all marks encountered so far
   * @param i the index of the current position
   *
   *        //> there is no i parameter in this method
   *
   *        //> -1
   *
   *
   * @return markList with a Mark added if a Mark of the recognition site for this Mark type exists
   *         at index i
   */
  @Override
  public List<Mark> readMarkdown(final String input, final List<Mark> markList, final int index) {
    if (input.startsWith(AUTOLINKSTARTSEQ)) {
      String potentialAutoLinkString = "";
      int potentialEndIndex = 0;
      for (int i = 0; i < input.length(); i++) {
        if (input.substring(i).startsWith(GREATERTHAN)) {
          potentialAutoLinkString = input.substring(0, i + 1);
          potentialEndIndex = i + index;
          break;
        }
      }
      if (!isInvalidLink(potentialAutoLinkString) && !potentialAutoLinkString.equals("")) {
        final AutoLinkMark newStart = new AutoLinkMark(index, STARTKIND);
        markList.add(newStart);
        final AutoLinkMark newEnd = new AutoLinkMark(potentialEndIndex, ENDKIND);
        markList.add(newEnd);
      }
    }
    return markList;
  }

  /**
   * Given a list of Marks and the index of this Mark within that list, uses the context relative to
   * the other marks to determine what kind (STARTKIND or ENDKIND) to be, or if it should be ignored
   * removes it from the list of Marks
   *
   * <pre>
   * Pre: a list of Marks and the index of this Mark within the list (list must contain this Mark)
   * </pre>
   *
   * <pre>
   * Post: a list of Marks where this mark is reinterpreted
   * </pre>
   *
   * @param markList a list of Marks containing this Mark
   * @param index the index of this Mark within the list of Marks
   * @return a list of Marks where Marks are removed based on the context of the other Marks
   */
  @Override
  public MarkAnalysisResult analyzeMarkList(final List<Mark> markList, final int index) {
    if (this.kind.equals(ENDKIND)) {
      for (int i = index + 1; i < markList.size(); i++) {
        if (markList.get(i).getPosition() <= this.getPosition()) {
          markList.remove(i);
          i--;
        }
      }
    }
    final MarkAnalysisResult result = new MarkAnalysisResult(markList);
    return result;
  }


  /**
   * Given a string and a list of marks on that string, where this Mark is on the list. Returns a
   * String where the substring from this mark to the next is rendered as HTML
   *
   * <pre>
   * Pre: a string and a list of marks where this Mark is on the list
   * </pre>
   *
   * <pre>
   * Post: returns a string where the substring between this mark and the next is rendered as HTML
   * </pre>
   *
   * @param input a string of markdown text where this Mark is present
   * @param MarkList a list of markdown Marks where this Mark is present
   *
   *        //> the parameter name is markList not MarkList.
   * 
   *        //> -1
   * @return the string between this mark and the next rendered as HTML, this Mark is converted to
   *         an HTML tag if the content of the string indicates it is a valid link
   */
  @Override
  public String toHTML(final String input, final List<Mark> markList) {
    String result = EMPTYSTRING;
    if (this.kind.equals(STARTKIND)) {
      final int matchingEnd = findIndexOfFirstKind(markList, ENDKIND);
      final boolean isInList = kindInList(markList, ENDKIND);
      final Mark endMark = markList.get(matchingEnd);
      final int endStringIndex = endMark.getPosition();
      if (!isInList) {
        result = determineSubstringToRender(input, markList);
      } else {
        final String potentialLinkString = input.substring(this.positionIndex, endStringIndex + 1);
        if (isInvalidLink(potentialLinkString)) {
          result = determineSubstringToRender(input, markList);
        } else {
          final String linkString =
              potentialLinkString.substring(1, potentialLinkString.length() - 1);
          result = generateLink(linkString);
        }
      }
    } else if (this.kind.equals(ENDKIND)) {
      if (markList.size() > 1) {
        result = input.substring(this.positionIndex + 1, markList.get(1).getPosition());
      } else {
        result = input.substring(this.positionIndex + 1);
      }
    }
    return result;
  }

  /**
   * Given the input string and the MarkList finds the substring between this Mark and the next, or
   * the end of the String if there are no more marks
   *
   * @param input the String containing this Mark and the Marks in markList
   * @param markList a list of Marks containing this Marks and other Marks in input
   * @return the substring between this Mark and the next Mark in markList, if this Mark is the last
   *         mark, returns the substring between this Mark and the String end
   */
  private String determineSubstringToRender(final String input, final List<Mark> markList) {
    if (markList.size() > 1) {
      return input.substring(this.positionIndex, markList.get(1).getPosition());
    } else {
      return input.substring(this.positionIndex);
    }
  }

  /**
   * Finds the index of the first AutoLinkMark of a given kind in a List of Marks, if no
   * AutoLinkMark of that kind is present, returns ABSENT
   *
   * @param markList a List of Marks
   * @param kind the kind of AutoLinkMark being searched for (either STARTKIND or ENDKIND
   * @return the index of the first AutoLinkMark of the given kind, if none is present returns
   *         ABSENT
   */
  private int findIndexOfFirstKind(final List<Mark> markList, final String kind) {
    for (int i = 0; i < markList.size(); i++) {
      if ((markList.get(i).getMarkType().equals(this.markType))
          && (markList.get(i).getKind().equals(kind))) {

        return i;
      }
    }
    return ABSENT;
  }

  /**
   * Determines if an AutoLinkMark of a given kind exists in a List of Marks
   *
   * @param markList a List of Marks
   * @param kind the kind of AutoLinkMark being searched for (either STARTKIND or ENDKIND
   * @return true iff an AutoLinkMark of the given kind exists in the list, false otherwise
   */
  private boolean kindInList(final List<Mark> markList, final String kind) {
    for (int i = 0; i < markList.size(); i++) {
      if ((markList.get(i).getMarkType().equals(this.markType))
          && (markList.get(i).getKind().equals(kind))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Tests if a String does not make a valid link because it contains whitespace or certain HTML
   * elements
   *
   * @param testString any String to potentially be made a link
   * @return true if testString is not a valid link because it contains whitespace or HTML elements,
   *         false if testString is a valid link
   */
  private Boolean isInvalidLink(final String testString) {
    return testString.matches(REGEX_WHITESPACE) || testString.matches(REGEX_HTML_PARAGRAPH_OPEN)
        || testString.matches(REGEX_HTML_HEADER1_OPEN)
        || testString.matches(REGEX_HTML_HEADER2_OPEN)
        || testString.matches(REGEX_HTML_HEADER3_OPEN)
        || testString.matches(REGEX_HTML_HEADER4_OPEN)
        || testString.matches(REGEX_HTML_HEADER5_OPEN)
        || testString.matches(REGEX_HTML_HEADER6_OPEN) || testString.matches(REGEX_HTML_CODE_OPEN)
        || testString.matches(REGEX_HTML_PRE_OPEN) || testString.matches(REGEX_HTML_OPEN)
        || testString.matches(REGEX_HTML_LIST_ELEMENT_OPEN)
        || testString.matches(REGEX_HTML_UNORDEREDLIST_OPEN)
        || testString.matches(REGEX_HTML_PARAGRAPH_CLOSE)
        || testString.matches(REGEX_HTML_HEADER1_CLOSE)
        || testString.matches(REGEX_HTML_HEADER2_CLOSE)
        || testString.matches(REGEX_HTML_HEADER3_CLOSE)
        || testString.matches(REGEX_HTML_HEADER4_CLOSE)
        || testString.matches(REGEX_HTML_HEADER5_CLOSE)
        || testString.matches(REGEX_HTML_HEADER6_CLOSE)
        || testString.matches(REGEX_HTML_CODE_CLOSE) || testString.matches(REGEX_HTML_PRE_CLOSE)
        || testString.matches(REGEX_HTML_CLOSE)
        || testString.matches(REGEX_HTML_LIST_ELEMENT_CLOSE)
        || testString.matches(REGEX_HTML_UNORDEREDLIST_CLOSE)
        || testString.matches(REGEX_HTML_BODY_OPEN) || testString.matches(REGEX_HTML_BODY_CLOSE)
        || testString.matches(REGEX_HTML_ORDEREDLIST_OPEN)
        || testString.matches(REGEX_HTML_ORDEREDLIST_CLOSE);
  }

  /**
   * Given a valid link String, converts it into an HTML link
   *
   * @param input a valid link String
   * @return the string formated as an HTML link
   */
  private String generateLink(final String input) {
    return HTML_START_LINK + input + GREATERTHAN + input + HTML_END_LINK;
  }
}
