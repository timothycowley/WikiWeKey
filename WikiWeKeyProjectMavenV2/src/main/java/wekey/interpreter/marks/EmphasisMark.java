package wekey.interpreter.marks;

import java.util.List;

public abstract class EmphasisMark implements Mark {

  // variables
  public String markType;
  public int positionIndex;
  public String kind;
  public String bothKindRegex;
  public String startKindRegex;
  public String endKindRegex;
  public String htmlStart;
  public String htmlEnd;
  public int markSize;

  // constants
  private static final String BOTHKIND = "both";
  private static final String STARTKIND = "start"; // A Emphasis's kind is either STARTKIND
  private static final String ENDKIND = "end"; // or ENDKIND
  private static final int ABSENT = -1; // index of -1 indicates value not found


  // CONSTRUCTORS
  /**
   * The full constructor for EmphasisMark takes an int representing the index of the start of the
   * mark within the text and a kind which is either STARTKIND or ENDKIND indicating if it is an
   * opening or closing EmphasisMark
   *
   * @param position an int corresponding to the index of the start of the mark within the text
   * @param kind either STARTKIND or ENDKIND indicating if the mark opens or closes EmphasisMark
   */
  public EmphasisMark(final int position, final String kind) {
    this.positionIndex = position;
    this.kind = kind;
  }

  /**
   * The empty constructor of EmphasisMark. Used for generating instances for reading markdown
   * strings
   */
  public EmphasisMark() {}

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
   * Getter for positionIndex the index where this Emphasis starts within the text
   *
   * @return this positionIndex
   */
  @Override
  public int getPosition() {
    return this.positionIndex;
  }

  /**
   * Getter for kind returns the kind, either STARTKIND or ENDKIND, indicating if it is an opening
   * or closing EmphasisMark
   *
   * @return this kind
   */
  @Override
  public String getKind() {
    return this.kind;
  }

  public void setMarkType(final String type) {
    this.markType = type;
  }

  public void setStartRegex(final String regex) {
    this.startKindRegex = regex;
  }

  public void setEndRegex(final String regex) {
    this.endKindRegex = regex;
  }

  public void setBothRegex(final String regex) {
    this.bothKindRegex = regex;
  }

  public void setHtmlEnd(final String end) {
    this.htmlEnd = end;
  }

  public void setHtmlStart(final String start) {
    this.htmlStart = start;
  }

  public void setMarkSize(final int size) {
    this.markSize = size;
  }

  /**
   * Given a substring of a text block, a list of all Marks encountered so far, and the index of the
   * current position in the text block, determines what EmphasisMark of this type to add to the
   * list, if any and returns it.
   *
   * <pre>
   * Pre: a substring of a text block, a list of all marks encountered so far, and the index of the current position
   * </pre>
   *
   * <pre>
   * Post: returns the Mark list either unchanged or with a EmphasisMark added the kind of which is determined by neighboring whitespace
   * </pre>
   *
   * @param currentSubstring a substring of a text block
   * @param markList a list of all Marks encountered so far //> no parameter with the name markList
   *        //> -1
   * @param i the index of the current position within the text block string
   * @return markList either unchanged or with an EmphasisMark of this type added, the kind of which
   *         is determined by neighboring whitespace
   */
  @Override
  public abstract List<Mark> readMarkdown(String currentSubstring, List<Mark> totalMarkList, int i);

  /**
   * Given a list of Marks determines the list index value of the last EmphasisMark of this type in
   * the list
   *
   * <pre>
   * Pre: an list of Marks
   * </pre>
   *
   * <pre>
   * Post: returns the index value of the Last EmphasisMark in the Mark list
   * </pre>
   *
   * @param markList a list of Marks containing this Mark
   * @return the index value in markList of the last EmphasisMark
   */
  public int indexOfLastEmphasisOfThisType(final List<Mark> markList) {
    for (int i = markList.size() - 1; i >= 0; i--) {
      if (markList.get(i).getMarkType().equals(this.markType)) {
        return i;
      }
    }
    return ABSENT;
  }

  /**
   * Returns the kind of the last EmphasisMark of this type in the given list of Marks, returns
   * ENDKIND if no EmphasisMarks of this type present
   *
   * <pre>
   * Pre: a list of Marks
   * </pre>
   *
   * <pre>
   * Post: the kind of the last EmphasisMark of this type in the list, or ENDKIND if the list contains no EmphasisMarks of this type
   * </pre>
   *
   * @param markList a list of Marks
   * @return the kind of the last EmphasisMark of this type in the given list of Marks, or ENDKIND
   *         if no EmphasisMarks of this type present
   */
  public String kindOfLastEmphasisOfThisType(final List<Mark> markList) {
    for (int i = markList.size() - 1; i >= 0; i--) {
      if (markList.get(i).getMarkType().equals(this.markType)) {
        return markList.get(i).getKind();
      }
    }
    return ENDKIND;
  }

  /**
   * Given a list of Marks and the index of this Mark within that list, uses the context relative to
   * the other Marks to determine what kind (STARTKIND or ENDKIND) to be, or if it should be removed
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
   */
  @Override
  public MarkAnalysisResult analyzeMarkList(final List<Mark> markList, final int index) {
    final String previousKind = kindOfLastEmphasisOfThisType(markList.subList(0, index));
    int offset = 0;
    if ((this.kind.equals(STARTKIND)) && (previousKind.equals(STARTKIND))) {
      markList.remove(index);
      offset--;
    } else if ((this.kind.equals(ENDKIND)) && (previousKind.equals(ENDKIND))) {
      markList.remove(index);
      offset--;
    } else if ((this.kind.equals(BOTHKIND)) && (previousKind.equals(ENDKIND))) {
      this.kind = STARTKIND;
      markList.set(index, this);
    } else if ((this.kind.equals(BOTHKIND)) && (previousKind.equals(STARTKIND))) {
      this.kind = ENDKIND;
      markList.set(index, this);
    }
    final MarkAnalysisResult result = new MarkAnalysisResult(markList, offset);
    return result;
  }

  /**
   * Given a list of Marks, removes any unclosed Emphasis starts of this type
   *
   * @param markList a list of Marks
   * @return a list of Marks where all Emphasis starts of this type without matching ends are
   *         removed
   */
  public List<Mark> removeUnclosedMarkStarts(final List<Mark> markList) {
    while (kindOfLastEmphasisOfThisType(markList).equals(STARTKIND)) {
      final int lastIndex = indexOfLastEmphasisOfThisType(markList);
      markList.remove(lastIndex);
    }
    return markList;
  }

  /**
   * Given a string and a list of marks on that string, where this Mark is on the list. Returns a
   * String where the substring from this Mark to the next is rendered as HTML
   *
   * <pre>
   * Pre: a string and a list of Marks where this Mark is on the list
   * </pre>
   *
   * <pre>
   * Post: returns a string where the substring between this Mark and the next is rendered as HTML
   * </pre>
   *
   * @param currentSubstring a string of markdown text where this Mark is present
   * @param markList a list of markdown Marks where this Mark is present
   * @return the string between this Mark and the next rendered as HTML, this Mark is converted to
   *         an HTML tag
   */
  @Override
  public String toHTML(final String currentSubstring, final List<Mark> markList) {
    int endIndex = currentSubstring.length();
    if (markList.size() > 1) {
      endIndex = markList.get(1).getPosition();
    }
    if (this.kind.equals(STARTKIND)) {
      return this.htmlStart
          + currentSubstring.substring(this.positionIndex + this.markSize, endIndex);
    } else {
      return this.htmlEnd
          + currentSubstring.substring(this.positionIndex + this.markSize, endIndex);
    }
  }
}
