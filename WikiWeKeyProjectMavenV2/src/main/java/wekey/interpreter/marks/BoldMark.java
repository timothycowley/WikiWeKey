package wekey.interpreter.marks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoldMark extends BoldMarkSuper {

  // constants
  private static final String BOLDBOTHREGEX = "^[^ ]\\*\\*[^ ]";
  private static final String BOLDENDREGEX = "^[^ ]\\*\\*";
  private static final String BOLDSTARTREGEX = "^.\\*\\*[^ ]";

  // Patterns identifying BoldMarks
  Pattern boldStart = Pattern.compile(BOLDSTARTREGEX);
  Pattern boldEnd = Pattern.compile(BOLDENDREGEX);
  Pattern boldBoth = Pattern.compile(BOLDBOTHREGEX);

  /**
   * The empty constructor of BoldMark. Used for generating instances for reading markdown strings
   */
  public BoldMark() {
    setMarkType("bold");
  }

  /**
   * The full constructor for BoldMark takes an int representing the index of the start of the mark
   * within the text and a kind which is either STARTKIND or ENDKIND indicating if it is an opening
   * or closing BoldMark
   *
   * @param position an int corresponding to the index of the start of the mark within the text
   * @param kind either STARTKIND or ENDKIND indicating if the mark opens or closes bold
   *
   *        //> no Javadoc for afterWhiteSpace
   * 
   *        //> -1
   */
  public BoldMark(final int position, final String kind, final boolean afterWhiteSpace) {
    setPisition(position);
    setKind(kind);
    setMarkType("bold");
    setAfterWhiteSpace(afterWhiteSpace);
  }

  /**
   * Given a substring of a text block, a list of all marks encountered so far, and the index of the
   * current position in the text block, determines what BoldMark to add to the Mark list, if any,
   * and returns it.
   *
   * <pre>
   * Pre: a substring of a text block, a list of all marks encountered so far, and the index of the current position within the text
   * </pre>
   *
   * <pre>
   * Post: returns the list of Mark either unchanged or with a BoldMark added the kind of which is determined by any neighboring whitespace.
   * </pre>
   *
   * @param currentSubstring a substring of a text block
   * @param markList a list of all marks encountered so far
   * @param i the index of the current position in the full text block
   * @return markList with a Mark added if a Mark of the recognition site for this Mark type exists
   *         at index i
   */
  @Override
  public List<Mark> readMarkdown(final String currentSubstring, final List<Mark> markList,
      final int i) {
    final Matcher boldStartMatcher = this.boldStart.matcher(currentSubstring);
    final Matcher boldEndMatcher = this.boldEnd.matcher(currentSubstring);
    final Matcher boldBothMatcher = this.boldBoth.matcher(currentSubstring);
    if ((boldBothMatcher.find(0) == true)) {
      final BoldMark newMark = new BoldMark(i + 1, BOTHKIND, false);
      markList.add(newMark);
    } else if (boldStartMatcher.find(0)) {
      final BoldMark newMark = new BoldMark(i + 1, STARTKIND, true);
      markList.add(newMark);
    } else if (boldEndMatcher.find(0)) {
      final BoldMark newMark = new BoldMark(i + 1, ENDKIND, false);
      markList.add(newMark);
    }
    return markList;
  }

  /**
   * Given a list of Marks and the index of this Mark within that list, uses the context relative to
   * the other marks to determine what kind (STARTKIND or ENDKIND) to be, or if it should be
   * interpreted as a ItalicMark instead.
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
   * @return a list of Marks where this Mark is altered to reflect the context of the other Marks
   *
   */
  @Override
  public MarkAnalysisResult analyzeMarkList(final List<Mark> markList, final int index) {
    final String previousBoldKind = kindOfLastBoldSuper(markList.subList(0, index));
    if ((this.getKind().equals(STARTKIND)) && (previousBoldKind.equals(STARTKIND))) {
      final ItalicMark newMark = determineItalicReplacement(markList, index);
      markList.set(index, newMark);
    } else if ((this.getKind().equals(ENDKIND)) && (previousBoldKind.equals(ENDKIND))) {
      final ItalicMark newMark = determineItalicReplacement(markList, index);
      markList.set(index, newMark);
    } else if ((this.getKind().equals(BOTHKIND)) && (previousBoldKind.equals(ENDKIND))) {
      this.setKind(STARTKIND);
      markList.set(index, this);
    } else if ((this.getKind().equals(BOTHKIND)) && (previousBoldKind.equals(STARTKIND))) {
      this.setKind(ENDKIND);
      markList.set(index, this);
    }
    final MarkAnalysisResult result = new MarkAnalysisResult(markList);
    return result;
  }

  /**
   * Converts this BoldMark to an ItalicMark based on the kind of the previous ItalicMark
   *
   * <pre>
   * Pre: a markList containing this Mark and the index of this Mark within the list
   * </pre>
   *
   * <pre>
   * Post: an ItalicMark with the same Position index of this Mark (or offset by 1) and is either a start or an end depending on the previous ItalicMark
   * </pre>
   *
   * @param markList a list of Marks containing this Mark
   * @param index the index of this Mark within the list of Marks
   * @return an ItalicMark with the same Position index of this Mark (or offset by 1) and is either
   *         a STARTKIND or an ENDKIND depending on the previous ItalicMark
   */
  private ItalicMark determineItalicReplacement(final List<Mark> markList, final int index) {
    final ItalicMark italic = new ItalicMark();
    final String previousItalicKind =
        italic.kindOfLastEmphasisOfThisType(markList.subList(0, index));
    if ((previousItalicKind.equals(STARTKIND)) && this.getAfterWhiteSpace()) {
      final ItalicMark newMark = new ItalicMark(this.getPosition() + 1, ENDKIND);
      return newMark;
    } else if (previousItalicKind.equals(STARTKIND)) {
      final ItalicMark newMark = new ItalicMark(this.getPosition(), ENDKIND);
      return newMark;
    } else {
      final ItalicMark newMark = new ItalicMark(this.getPosition(), STARTKIND);
      return newMark;
    }
  }

  /**
   * Given a list of Marks if any of the Bold Marks are unclosed, they are converted to Italic
   * Marks, the kind of which is determined by the preceding ItalicMark kind
   *
   * @param markList a list of Marks
   * @return a list of Marks with any BoldMark STARTKIND not followed by a BoldMark ENDKIND is
   *         replaced with an ItalicMark, the kind of which is determined by the preceding
   *         ItalicMark
   */
  public List<Mark> removeUnclosedMarkStarts(final List<Mark> markList) {
    final int indexFirstUnclosed = indexOfFirstUnclosedBold(markList);
    for (int i = indexFirstUnclosed; ((-1 < i) && (i < markList.size())); i++) {
      final Mark currentMark = markList.get(i);
      if ((currentMark.getMarkType().equals(getMarkType()))
          && (currentMark.getKind().equals(STARTKIND))) {
        final BoldMark currentBold = (BoldMark) currentMark;
        final ItalicMark italic = new ItalicMark();
        final String lastItalicKind = italic.kindOfLastEmphasisOfThisType(markList.subList(0, i));
        if (lastItalicKind.equals(ENDKIND)) {
          final ItalicMark newItalic = new ItalicMark(currentBold.getPosition(), STARTKIND);
          markList.set(i, newItalic);
        } else if (lastItalicKind.equals(STARTKIND) && currentBold.getAfterWhiteSpace()) {
          final ItalicMark newItalic = new ItalicMark(currentBold.getPosition() + 1, ENDKIND);
          markList.set(i, newItalic);
        } else {
          final ItalicMark newItalic = new ItalicMark(currentBold.getPosition(), ENDKIND);
          markList.set(i, newItalic);
        }
      }
    }
    return markList;
  }
}
