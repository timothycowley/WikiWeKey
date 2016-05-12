package wekey.interpreter.marks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class identifies and interprets Markdown strikethrough tags and renders them as HTML
 *
 * @author Tim
 *
 */
public class StrikethroughMark extends EmphasisMark {

  private static final String TYPE = "strikethrough";
  private static final String STARTSTRIKEREGEX = "^.~~[^ ]";
  private static final String ENDSTRIKEREGEX = "^[^ ]~~";
  private static final String BOTHSTRIKEREGEX = "^[^ ]~~[^ ]";
  private static final String START_HTML = "<s>";
  private static final String END_HTML = "</s>";
  private static final String STARTKIND = "start"; // A Strikeout's kind is either STARTKIND
  private static final String ENDKIND = "end"; // or ENDKIND
  private static final String BOTHKIND = "both"; // or BOTHKIND
  private static final int MARKSIZE = 2;

  /**
   * Full constructor, it sets the superclass variables to recognize this Mark type.
   */
  public StrikethroughMark(int position, String kind) {
    super(position, kind);
    setMarkType(TYPE);
    setBothRegex(BOTHSTRIKEREGEX);
    setStartRegex(STARTSTRIKEREGEX);
    setEndRegex(ENDSTRIKEREGEX);
    setHtmlStart(START_HTML);
    setHtmlEnd(END_HTML);
    setMarkSize(MARKSIZE);
  }

  /**
   * Empty constructor for creating an instance to read markdown, it sets the superclass variables
   * to recognize this Mark type.
   */
  public StrikethroughMark() {
    setMarkType(TYPE);
    setBothRegex(BOTHSTRIKEREGEX);
    setStartRegex(STARTSTRIKEREGEX);
    setEndRegex(ENDSTRIKEREGEX);
    setHtmlStart(START_HTML);
    setHtmlEnd(END_HTML);
    setMarkSize(MARKSIZE);
  }

  // see superclass java doc
  @Override
  public List<Mark> readMarkdown(String currentSubstring, List<Mark> totalMarkList, int i) {
    Pattern strikeStart = Pattern.compile(STARTSTRIKEREGEX);
    Pattern strikeEnd = Pattern.compile(ENDSTRIKEREGEX);
    Pattern strikeBoth = Pattern.compile(BOTHSTRIKEREGEX);
    Matcher strikeStartMatcher = strikeStart.matcher(currentSubstring);
    Matcher strikeEndMatcher = strikeEnd.matcher(currentSubstring);
    Matcher strikeBothMatcher = strikeBoth.matcher(currentSubstring);
    if (strikeBothMatcher.find(0)) {
      StrikethroughMark newMark = new StrikethroughMark(i + 1, BOTHKIND);
      totalMarkList.add(newMark);
    } else if (strikeStartMatcher.find(0)) {
      StrikethroughMark newMark = new StrikethroughMark(i + 1, STARTKIND);
      totalMarkList.add(newMark);
    } else if (strikeEndMatcher.find(0)) {
      StrikethroughMark newMark = new StrikethroughMark(i + 1, ENDKIND);
      totalMarkList.add(newMark);
    }
    return totalMarkList;
  }

}
