package wekey.interpreter.marks;

import java.util.List;

public abstract class BoldMarkSuper implements Mark {
  // variables
  private String markType;
  private int positionIndex;
  private String kind;
  private boolean afterWhiteSpace;

  // constants
  protected static final String BOTHKIND = "both";
  protected static final String STARTKIND = "start"; // A BoldUnderscoreMark's kind is either
  protected static final String ENDKIND = "end"; // or ENDKIND
  protected static final int ABSENT = -1; // index of -1 indicates value not found
  protected static final String HTMLBOLDSTART = "<strong>"; // HTML for starting bold
  protected static final String HTMLBOLDEND = "</strong>"; // HTML for ending bold
  protected static final int MARKSIZE = 2; // the # of characters that need to be deleted to remove

  /**
   * returns the kind, either STARTKIND or ENDKIND, indicating if it is an opening or closing
   * BoldMark
   *
   * @return this kind
   */
  @Override
  public String getKind() {
    return this.kind;
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
   * returns the index where this BoldMark starts within the text
   *
   * @return this positionIndex
   */
  @Override
  public int getPosition() {
    return this.positionIndex;
  }


  /**
   * returns the boolean afterWhitespace, which is true if this mark occurs directly after
   * whitespace and false otherwise
   *
   * @return this afterWhitespace
   */
  public boolean getAfterWhiteSpace() {
    return this.afterWhiteSpace;
  }


  /**
   * setter for positionIndex variable
   *
   * <post> post: positionIndex updated to the given position integer </post>
   *
   * //> there is not HTML tag with the name post, I think you mean
   *
   * <pre>
   *
   * //> -1
   */
  public void setPisition(final int position) { // > Odd name for your setter :)
    // > -1
    this.positionIndex = position;
  }

  /**
   * setter for kind variable
   *
   * <post> post: kind updated to the given kind </post>
   *
   * //> same mistake here on the tag name
   */
  public void setKind(final String kind) {
    this.kind = kind;
  }

  /**
   * setter for markType variable
   *
   * <post> post: markType updated to the given markType </post> //> same mistake here on the tag
   * name
   * 
   */
  public void setMarkType(final String mark) {
    this.markType = mark;
  }

  /**
   * setter for afterWhiteSpace variable <post> post: afterWhiteSpace boolean updated to the given
   * boolean </post>
   *
   * @param afterWhiteSpace //> missing javadoc here.
   */
  public void setAfterWhiteSpace(final boolean afterWhiteSpace) {
    this.afterWhiteSpace = afterWhiteSpace;
  }

  /**
   * Abstract method that, given a substring of a text block, a list of all marks encountered so
   * far, and the index of the current position in the text block, determines what BoldMark to add
   * to the Mark list, if any, and returns it.
   *
   * @param currentSubstring a substring of a text block
   * @param markList a list of all marks encountered so far
   * @param i the index of the current position in the full text block
   * @return markList with a Mark added if a Mark of the recognition site for this Mark type exists
   *         at index i
   */
  @Override
  public abstract List<Mark> readMarkdown(String input, List<Mark> markList, int i);

  /**
   * Abstract method, given a list of Marks and the index of this Mark within that list, uses the
   * context relative to the other Marks to determine what kind (STARTKIND or ENDKIND) to be, or if
   * it should be interpreted as one of the ItalicMark instead.
   *
   * @param markList a list of Marks containing this Mark
   * @param index the index of this Mark within the list of Marks
   * @return a list of Marks where this Mark is altered to reflect the context of the other marks
   *
   */
  @Override
  public abstract MarkAnalysisResult analyzeMarkList(List<Mark> marklist, int index);

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
   * @return the string between this mark and the next rendered as HTML, this Mark is converted to
   *         an HTML tag
   */
  @Override
  public String toHTML(final String currentSubstring, final List<Mark> markList) {
    int endIndex = currentSubstring.length();
    if (markList.size() > 1) {
      endIndex = markList.get(1).getPosition();
    }
    if (this.kind.equals(STARTKIND)) {
      return HTMLBOLDSTART + currentSubstring.substring((this.positionIndex + MARKSIZE), endIndex);
    } else {
      return HTMLBOLDEND + currentSubstring.substring(this.positionIndex + MARKSIZE, endIndex);
    }
  }

  /**
   * Determines if a BoldMark ENDKIND exists in a list of Marks
   *
   * @param markList a list of Marks
   * @return true iff markList contains a BoldMark, false otherwise
   */
  public boolean endBoldMarkSuperInList(final List<Mark> markList) {
    for (int i = 0; i < markList.size(); i++) {
      if (markList.get(i).getMarkType().equals(this.markType)
          && markList.get(i).getKind().equals(ENDKIND)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Given a list of Marks returns the index of the first unclosed BoldMark in the list. If there
   * are no unclosed marks, returns ABSENT
   *
   * <pre>
   * Pre: no unclosed BoldMarks exist before the last BoldMark ENDKIND
   * </pre>
   *
   * @param markList a list of Marks
   * @return the index of the first BoldMark STARTKIND where BoldMark ENDKIND is not found in any of
   *         the preceding elements.
   */
  public int indexOfFirstUnclosedBold(final List<Mark> markList) {
    final int lastEnd = indexOfLastBoldEnd(markList);
    final boolean hasEndBoldMark = endBoldMarkSuperInList(markList);
    if (hasEndBoldMark) {
      for (int i = lastEnd + 1; i < markList.size(); i++) {
        if (markList.get(i).getMarkType().equals(this.markType)) {
          return i;
        }
      }
    } else {
      for (int i = 0; i < markList.size(); i++) {
        if (markList.get(i).getMarkType().equals(this.markType)) {
          return i;
        }
      }
    }
    return ABSENT;
  }

  /**
   * Given a list of Marks returns the index within the list of the last BoldMark ENDKIND, if no
   * such mark exists, returns ABSENT
   *
   * @param markList a list of Marks
   * @return the index of the last BoldMark ENDKIND, if no such Mark exists, returns ABSENT
   */
  public int indexOfLastBoldEnd(final List<Mark> markList) {
    for (int i = markList.size() - 1; i >= 0; i--) {
      final Mark currentMark = markList.get(i);
      if ((currentMark.getMarkType().equals(this.markType))
          && (currentMark.getKind().equals(ENDKIND))) {
        return i;
      }
    }
    return ABSENT;
  }

  /**
   * Returns the kind of the last sub-class of BoldMarkSuper in the given list of marks, returns
   * ENDKIND if no sub-class of BoldMarkSuper present
   *
   * <pre>
   * Pre: a list of Marks
   * </pre>
   *
   * <pre>
   * Post: the kind of the last sub-class of BoldMarkSuper in the list, or ENDKIND if the list contains no sub-class of BoldMarkSuper
   * </pre>
   *
   * @param markList a list of Marks
   * @return the kind of the last sub-class of BoldMarkSuper mark in the given list of marks, or
   *         ENDKIND if no sub-class of BoldMarkSuper present
   */
  public String kindOfLastBoldSuper(final List<Mark> markList) {
    for (int i = markList.size() - 1; i >= 0; i--) {
      if (markList.get(i).getMarkType().equals(this.markType)) {
        return markList.get(i).getKind();
      }
    }
    return ENDKIND;
  }
}
