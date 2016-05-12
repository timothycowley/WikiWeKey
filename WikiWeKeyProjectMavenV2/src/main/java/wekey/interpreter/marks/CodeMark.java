package wekey.interpreter.marks;

import java.util.List;

/**
 * CodeMark's primary purpose is to remove Marks from code blocks
 *
 *
 * @author Tim
 *
 */
public class CodeMark implements Mark {
  private String markType = "code";
  private int positionIndex;
  private String kind;

  private static final int ABSENT = -1;
  private static final String ENDKIND = "end";
  private static final String STARTKIND = "start";
  private static final String CODEMARKEND = "</pre>";
  private static final String CODEMARKSTART = "<pre>";



  /**
   * Empty Constructor for creating instances for reading markdown
   */
  public CodeMark() {}

  /**
   * Constructor
   *
   * @param position the index this Mark appears within the text string being analyzed
   * @param kind the kind of the Mark, either STARTKIND which starts a code block or ENDKIND which
   *        ends a code block
   */
  public CodeMark(int position, String kind) {
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
   * returns the index that this Mark appears at within the text string being analyzed
   *
   * @return this positionIndex
   */
  @Override
  public int getPosition() {
    return this.positionIndex;
  }

  /**
   * returns the kind of the Mark, either STARTKIND which starts a code block or ENDKIND which ends
   * a code block
   *
   * @return this kind
   */
  @Override
  public String getKind() {
    return this.kind;
  }


  /**
   * Given a substring of a text, adds a CodeMark of the correct kind to a list of already found
   * Marks if it matches a CodeMark recognition sequence
   *
   * @param input a substring of the text analyzed
   * @param i the index of the start of the substring within the text being analyzed
   * @return the list of found Marks with a CodeMark added if the substring matches a CodeMark
   *         recognition sequence
   */
  @Override
  public List<Mark> readMarkdown(String input, List<Mark> marklist, int i) {
    if (input.startsWith(CODEMARKSTART)) {
      CodeMark newMark = new CodeMark(i, STARTKIND);
      marklist.add(newMark);
    } else if (input.startsWith(CODEMARKEND)) {
      CodeMark newMark = new CodeMark(i, ENDKIND);
      marklist.add(newMark);
    }
    return marklist;
  }

  /**
   * When this is a STARTKIND removes all Marks between this and the following CodeMark ENDKIND and
   * returns a MarkAnalysisResult with the updated List of Marks
   *
   * @param markList the List of Marks in this block of text
   * @param index the position of this Mark in the list of Marks
   */
  @Override
  public MarkAnalysisResult analyzeMarkList(List<Mark> markList, int index) {
    int endIndex = findEndCodeListIndex(markList, index);
    boolean hasMatchingEnd = endCodeMarkInList(markList, index);
    if ((this.kind.equals(STARTKIND)) && (hasMatchingEnd)) {
      for (int i = index + 1; i < endIndex; i++) {
        markList.remove(i);
        endIndex--;
        i--;
      }
    }
    MarkAnalysisResult result = new MarkAnalysisResult(markList);
    return result;
  }

  /**
   * Returns the index of the next CodeMark ENDKIND in a List of Marks starting at the given index,
   * if no CodeMark ENDKIND exists, returns ABSENT
   *
   * @param marklist a List of Marks
   * @param index the index to start the search at
   * @return the index of the next CodeMark ENDKIND in a List of Marks starting at the given index,
   *         returns ABSENT if not found
   */
  private int findEndCodeListIndex(List<Mark> marklist, int index) {
    for (int i = index; i < marklist.size(); i++) {
      if ((marklist.get(i).getMarkType().equals(this.markType))
          && (marklist.get(i).getKind().equals(ENDKIND))) {
        return i;
      }
    }
    return ABSENT;
  }

  /**
   * Returns true if CodeMark ENDKIND is in a List of Marks starting at the given index, if no
   * CodeMark ENDKIND exists, returns false
   *
   * @param marklist a List of Marks
   * @param index the index to start the search at
   * @return true iff CodeMark ENDKIND in list, false otherwise
   */
  private boolean endCodeMarkInList(List<Mark> marklist, int index) {
    for (int i = index; i < marklist.size(); i++) {
      if ((marklist.get(i).getMarkType().equals(this.markType))
          && (marklist.get(i).getKind().equals(ENDKIND))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the substring of the text block starting at this Mark's position index to the next
   * Mark's position index as HTML. If this Mark is the last Mark, returns this Mark to the end of
   * the string
   *
   * @param input the text block being analyzed which contains the marks in markList
   * @param markList a list of Marks in input
   * @return the substring from the text block starting at this Mark's position index to the next
   *         Mark's position index as HTML. If this Mark is the last Mark, returns the substring
   *         from this Mark's position to the end of input
   */
  @Override
  public String toHTML(String input, List<Mark> markList) {
    int substringEnd = input.length();
    if (markList.size() > 1) {
      substringEnd = markList.get(1).getPosition();
    }
    return input.substring(this.positionIndex, substringEnd);
  }

}
