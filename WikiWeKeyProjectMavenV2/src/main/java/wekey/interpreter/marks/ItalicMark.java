package wekey.interpreter.marks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class identifies and interprets Markdown italic tags ("*" type) and renders them as HTML
 *
 * @author Tim
 *
 */
public class ItalicMark extends EmphasisMark {

  private static final String TYPE = "italic";
  private static final String START_REGEX = "^[^\\*]\\*[^ \\*]";
  private static final String END_REGEX = "^[^ \\*]\\*[^\\*]";
  private static final String BOTH_REGEX = "^[^ \\*]\\*[^ \\*]";
  private static final String START_HTML = "<em>";
  private static final String END_HTML = "</em>";
  private static final String STARTKIND = "start";
  private static final String ENDKIND = "end";
  private static final String BOTHKIND = "both";
  private static final int MARKSIZE = 1;

  /**
   * Full constructor, it sets the superclass variables to recognize this Mark type.
   *
   * //> no Javadoc param.
   *
   */
  public ItalicMark(final int position, final String kind) {
    super(position, kind);
    setMarkType(TYPE);
    setBothRegex(BOTH_REGEX);
    setStartRegex(START_REGEX);
    setEndRegex(END_REGEX);
    setHtmlStart(START_HTML);
    setHtmlEnd(END_HTML);
    setMarkSize(MARKSIZE);
  }

  /**
   * Empty constructor for creating an instance to read markdown, it sets the superclass variables
   * to recognize this Mark type.
   */
  public ItalicMark() {
    setMarkType(TYPE);
    setBothRegex(BOTH_REGEX);
    setStartRegex(START_REGEX);
    setEndRegex(END_REGEX);
    setHtmlStart(START_HTML);
    setHtmlEnd(END_HTML);
    setMarkSize(MARKSIZE);
  }

  // see superclass java doc
  @Override
  public List<Mark> readMarkdown(final String currentSubstring, final List<Mark> totalMarkList,
      final int i) {
    final Pattern startPattern = Pattern.compile(START_REGEX);
    final Pattern endPattern = Pattern.compile(END_REGEX);
    final Pattern bothPattern = Pattern.compile(BOTH_REGEX);
    final Matcher startMatcher = startPattern.matcher(currentSubstring);
    final Matcher endMatcher = endPattern.matcher(currentSubstring);
    final Matcher bothMatcher = bothPattern.matcher(currentSubstring);
    if (bothMatcher.find(0)) {
      final ItalicMark newMark = new ItalicMark(i + 1, BOTHKIND);
      totalMarkList.add(newMark);
    } else if (startMatcher.find(0)) {
      final ItalicMark newMark = new ItalicMark(i + 1, STARTKIND);
      totalMarkList.add(newMark);
    } else if (endMatcher.find(0)) {
      final ItalicMark newMark = new ItalicMark(i + 1, ENDKIND);
      totalMarkList.add(newMark);
    }
    return totalMarkList;
  }

}
