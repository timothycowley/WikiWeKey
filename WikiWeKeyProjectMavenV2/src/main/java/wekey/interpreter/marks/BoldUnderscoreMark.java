package wekey.interpreter.marks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoldUnderscoreMark extends BoldMarkSuper {

  // constants
  private static final String BOLDUNDERBOTHREGEX = "^[^ ]__[^ ]";
  private static final String BOLDUNDERENDREGEX = "^[^ ]__";
  private static final String BOLDUNDERSTARTREGEX = "^.__[^ ]";

  // Patterns identifying BoldUnderscoreMarks
  Pattern boldUnderscoreStart = Pattern.compile(BOLDUNDERSTARTREGEX);
  Pattern boldUnderscoreEnd = Pattern.compile(BOLDUNDERENDREGEX);
  Pattern boldUnderscoreBoth = Pattern.compile(BOLDUNDERBOTHREGEX);

  /**
   * The full constructor for BoldUnderscoreMark takes an int representing the index of the start of
   * the mark within the text and a kind which is either STARTKIND or ENDKIND indicating if it is an
   * opening or closing BoldUnderscoreMark
   *
   * @param position an int corresponding to the index of the start of the mark within the text
   * @param kind either STARTKIND or ENDKIND indicating if the mark opens or closes BoldUnderscore
   */
  public BoldUnderscoreMark(int position, String kind, boolean afterWhiteSpace) {
    setPisition(position);
    setKind(kind);
    setMarkType("boldUnderscore");
    setAfterWhiteSpace(afterWhiteSpace);
  }

  /**
   * The empty constructor of BoldUnderscoreMark. Used for generating instances for reading markdown
   * strings
   */
  public BoldUnderscoreMark() {
    setMarkType("boldUnderscore");
  }

  /**
   * Given a substring of a text block, a list of all marks encountered so far, and the index of the
   * current position within the text block determines what BoldUnderscoreMark kind to add to the
   * mark list, if any and returns it.
   *
   * <pre>
   * Pre: a substring of a text block, a list of all marks encountered so far, and the index of the current position in the text
   * </pre>
   *
   * <pre>
   * Post: returns totalMarkList either unchanged or with a BoldUnderscoreMark added the kind of which is determined by the neighboring whitespace
   * </pre>
   *
   * @param currentSubstring a substring of a text block
   * @param totalMarkList a list of all marks encountered so far
   * @param i the index of the current position
   * @return markList with a Mark added if a Mark of the recognition site for this Mark type exists
   *         at index i
   */
  @Override
  public List<Mark> readMarkdown(String currentSubstring, List<Mark> totalMarkList, int i) {
    Matcher boldUnderscoreStartMatcher = boldUnderscoreStart.matcher(currentSubstring);
    Matcher boldUnderscoreEndMatcher = boldUnderscoreEnd.matcher(currentSubstring);
    Matcher boldUnderscoreBothMatcher = boldUnderscoreBoth.matcher(currentSubstring);
    if ((boldUnderscoreBothMatcher.find(0) == true)) {
      BoldUnderscoreMark newMark = new BoldUnderscoreMark(i + 1, BOTHKIND, false);
      totalMarkList.add(newMark);
    } else if (boldUnderscoreStartMatcher.find(0)) {
      BoldUnderscoreMark newMark = new BoldUnderscoreMark(i + 1, STARTKIND, true);
      totalMarkList.add(newMark);
    } else if (boldUnderscoreEndMatcher.find(0)) {
      BoldUnderscoreMark newMark = new BoldUnderscoreMark(i + 1, ENDKIND, false);
      totalMarkList.add(newMark);
    }
    return totalMarkList;
  }

  /**
   * Given a list of Marks and the index of this Mark within that list, uses the context relative to
   * the other Marks to determine what kind (STARTKIND or ENDKIND) to be, or if it should be
   * interpreted as a ItalicUnderscoreMark instead.
   *
   * <pre>
   * Pre: a list of Marks and the index of this Mark within the list (list must contain this Mark)
   * </pre>
   *
   * <pre>
   * Post: a list of Marks where this mark is reinterpreted based on the context of the other Marks
   * </pre>
   *
   * @param markList a list of Marks containing this Mark
   * @param index the index of this Mark within the list of Marks
   * @return a list of Marks where this Mark is altered to reflect the context of the other marks
   *
   */
  @Override
  public MarkAnalysisResult analyzeMarkList(List<Mark> markList, int index) {
    String previousBoldUnderscoreKind = kindOfLastBoldSuper(markList.subList(0, index));
    if ((this.getKind().equals(STARTKIND)) && (previousBoldUnderscoreKind.equals(STARTKIND))) {
      ItalicUnderscoreMark newMark = determineItalicUnderscoreReplacement(markList, index);
      markList.set(index, newMark);
    } else if ((this.getKind().equals(ENDKIND)) && (previousBoldUnderscoreKind.equals(ENDKIND))) {
      ItalicUnderscoreMark newMark = determineItalicUnderscoreReplacement(markList, index);
      markList.set(index, newMark);
    } else if ((this.getKind().equals(BOTHKIND)) && (previousBoldUnderscoreKind.equals(ENDKIND))) {
      this.setKind(STARTKIND);
      markList.set(index, this);
    } else if ((this.getKind().equals(BOTHKIND)) && (previousBoldUnderscoreKind.equals(STARTKIND))) {
      this.setKind(ENDKIND);
      markList.set(index, this);
    }
    MarkAnalysisResult result = new MarkAnalysisResult(markList);
    return result;
  }

  /**
   * Converts this BoldUnderscoreMark to an ItalicUnderscoreMark based on the kind of the previous
   * ItalicUnderscoreMark
   *
   * <pre>
   * Pre: a markList containing this Mark and the index of this Mark within the list
   * </pre>
   *
   * <pre>
   * Post: an ItalicUnderscoreMark with the same Position index of this Mark (or offset by 1) and is either a start or an end depending on the previous ItalicUnderscoreMark
   * </pre>
   *
   * @param markList a list of Marks containing this Mark
   * @param index the index of this Mark within the list of Marks
   * @return an ItalicUnderscoreMark with the same Position index of this Mark (or offset by 1) and
   *         is either a start or an end depending on the previous ItalicUnderscoreMark
   */
  private ItalicUnderscoreMark determineItalicUnderscoreReplacement(List<Mark> markList, int index) {
    ItalicUnderscoreMark italic = new ItalicUnderscoreMark();
    String previousItalicUnderscoreKind =
        italic.kindOfLastEmphasisOfThisType(markList.subList(0, index));
    if ((previousItalicUnderscoreKind.equals(STARTKIND)) && (this.getAfterWhiteSpace())) {
      ItalicUnderscoreMark newMark = new ItalicUnderscoreMark(this.getPosition() + 1, ENDKIND);
      return newMark;
    } else if (previousItalicUnderscoreKind.equals(STARTKIND)) {
      ItalicUnderscoreMark newMark = new ItalicUnderscoreMark(this.getPosition(), ENDKIND);
      return newMark;
    } else {
      ItalicUnderscoreMark newMark = new ItalicUnderscoreMark(this.getPosition(), STARTKIND);
      return newMark;
    }
  }

  /**
   * Given a list of Marks if any of the BoldUnderscoreMarks are unclosed, they are converted to
   * ItalicUnderscoreMarks, the kind of which is determined by the preceding ItalicUnderscoreMark
   * kind
   *
   * @param markList a list of Marks
   * @return a list of Marks with any BoldUnderscoreMark STARTKIND not followed by a
   *         BoldUnderscoreMark ENDKIND replaced with an ItalicUnderscoreMark, the kind of which is
   *         determined by the preceding ItalicUnderscoreMark
   */
  public List<Mark> removeUnclosedMarkStarts(List<Mark> markList) {
    int indexFirstUnclosed = indexOfFirstUnclosedBold(markList);
    for (int i = indexFirstUnclosed; ((-1 < i) && (i < markList.size())); i++) {
      Mark currentMark = markList.get(i);
      if ((currentMark.getMarkType().equals(getMarkType()))
          && (currentMark.getKind().equals(STARTKIND))) {
        BoldUnderscoreMark currentBold = (BoldUnderscoreMark) currentMark;
        ItalicUnderscoreMark italic = new ItalicUnderscoreMark();
        String lastItalicKind = italic.kindOfLastEmphasisOfThisType(markList.subList(0, i));
        if (lastItalicKind.equals(ENDKIND)) {
          ItalicUnderscoreMark newItalic =
              new ItalicUnderscoreMark(currentBold.getPosition(), STARTKIND);
          markList.set(i, newItalic);
        } else if (lastItalicKind.equals(STARTKIND) && currentBold.getAfterWhiteSpace()) {
          ItalicUnderscoreMark newItalic =
              new ItalicUnderscoreMark(currentBold.getPosition() + 1, ENDKIND);
          markList.set(i, newItalic);
        } else {
          ItalicUnderscoreMark newItalic =
              new ItalicUnderscoreMark(currentBold.getPosition(), ENDKIND);
          markList.set(i, newItalic);
        }
      }
    }
    return markList;
  }
}
