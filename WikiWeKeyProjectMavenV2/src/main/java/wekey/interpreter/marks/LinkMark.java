package wekey.interpreter.marks;

import java.util.List;

public class LinkMark implements Mark {
  private String markType = "link";
  private int positionIndex;
  private String kind;

  private static final String LINKENDSEQUENCE = ")";
  private static final String LINKMIDDLESEQUENCE = "](";
  private static final String LINKSTARTSEQUENCE = "[";
  private static final String MIDDLEKIND = "middle";
  private static final String ENDKIND = "end";
  private static final String STARTKIND = "start";
  private static final int LINKMIDDLESIZE = 2;
  private static final String GREATERTHAN = ">";
  private static final String HTMLLINKEND = "</a>";
  private static final String HTMLLINKSTART = "<a href=";
  private static final String EMPTYSTRING = "";
  private static final int ABSENT = -1;

  /**
   * Empty constructor for creating instances for reading markdown
   */
  public LinkMark() {}

  /**
   * Constructor
   *
   * @param position the index of the mark within the text analyzed
   * @param kind the kind of mark: STARTKIND means it identifies the start of a link, MIDDLEKIND
   *        separates the text displayed from the link address, ENDKIND identifies the end of a link
   */
  public LinkMark(int position, String kind) {
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
   * returns the positionIndex, the index of the mark within the text analyzed
   *
   * @return this positionIndex
   */
  @Override
  public int getPosition() {
    return positionIndex;
  }

  /**
   * returns the kind of mark: STARTKIND means it identifies the start of a link, MIDDLEKIND
   * separates the text displayed from the link address, ENDKIND identifies the end of a link
   *
   * @return this kind
   */
  @Override
  public String getKind() {
    return kind;
  }

  /**
   * Determines if a Link identification sequence is found at the index provided and if so, adds a
   * Link of the appropriate kind to the list of Marks
   *
   * @param input a substring of the text being analyzed in which to identify any marks
   * @param marklist a list of Marks found so far in the text being analyzed
   * @param i the current index within the text being analyzed
   * @return markList either unchanged or with a LinkMark added, the kind of which is dependent on
   *         the recognition sequence
   */
  @Override
  public List<Mark> readMarkdown(String input, List<Mark> marklist, int i) {
    if (input.startsWith(LINKSTARTSEQUENCE)) {
      LinkMark newMark = new LinkMark(i, STARTKIND);
      marklist.add(newMark);
    } else if (input.startsWith(LINKMIDDLESEQUENCE)) {
      LinkMark newMark = new LinkMark(i, MIDDLEKIND);
      marklist.add(newMark);
    } else if (input.startsWith(LINKENDSEQUENCE)) {
      LinkMark newMark = new LinkMark(i, ENDKIND);
      marklist.add(newMark);
    }
    return marklist;
  }

  /**
   * Finds the last LinkMark within the given list of Marks and returns it's kind. If there are no
   * LinkMarks present it returns ENDKIND.
   *
   * @param markList any List of Marks
   * @return the kind of the last LinkMark in the list, if any, otherwise returns ENDKIND
   */
  private String kindOfLastLink(List<Mark> markList) {
    for (int i = markList.size() - 1; i >= 0; i--) {
      if (markList.get(i).getMarkType().equals(this.markType)) {
        return markList.get(i).getKind();
      }
    }
    return ENDKIND;
  }

  /**
   * Finds the last LinkMark within the given list of Marks and returns the index of that Mark
   * within the list of Marks
   *
   * @param markList any List of Marks
   * @return the index of the last LinkMark in the list, if any, otherwise returns ABSENT
   */
  private int indexOfLastLink(List<Mark> markList) {
    for (int i = markList.size() - 1; i >= 0; i--) {
      if (markList.get(i).getMarkType().equals(this.markType))
        return i;
    }
    return ABSENT;
  }

  /**
   * Returns the index of the first LinkMark in a list of Marks starting at the 2nd element of the
   * list (index 1)
   *
   * @param markList any List of Marks
   * @return the index of the first LinkMark occurring after the 1st element or ABSENT if there are
   *         no LinkMarks after the 1st element
   */
  private int indexOfNextLink(List<Mark> markList) {
    for (int i = 1; i < markList.size(); i++) {
      if (markList.get(i).getMarkType().equals(this.markType))
        return i;
    }
    return ABSENT;
  }

  /**
   * Takes a List of Marks and removes any LinkMarks that cannot be interpreted. Also removes any
   * Marks within the Links
   *
   * @param marklist a List of Marks containing this Mark
   * @param index the list index of this Mark within the list of Marks
   * @return a MarkAnalysisResult containing the resulting List of Marks and an offset value which
   *         indicates how the length of the list has changed prior to and including the current
   *         position (Ex. if two Marks are removed offset = -2).
   */
  @Override
  public MarkAnalysisResult analyzeMarkList(List<Mark> marklist, int index) {
    int previousLinkIndex = indexOfLastLink(marklist.subList(0, index));
    String lastLinkKind = ENDKIND;
    if (index > 0) {
      lastLinkKind = kindOfLastLink(marklist.subList(0, index));
    }
    int offset = 0;
    if ((this.kind.equals(STARTKIND)) && (lastLinkKind.equals(MIDDLEKIND))) {
      marklist.remove(index);
      offset--;
    } else if ((this.kind.equals(STARTKIND)) && (lastLinkKind.equals(STARTKIND))) {
      marklist.remove(previousLinkIndex);
      offset--;
    } else if ((this.kind.equals(MIDDLEKIND)) && (!lastLinkKind.equals(STARTKIND))) {
      marklist.remove(index);
      offset--;
    } else if ((this.kind.equals(MIDDLEKIND)) && (lastLinkKind.equals(STARTKIND))) {
      for (int i = previousLinkIndex + 1; i < (index + offset); i++) {
        marklist.remove(i);
        offset--;
      }
    } else if ((this.kind.equals(ENDKIND)) && (!lastLinkKind.equals(MIDDLEKIND))) {
      marklist.remove(index);
      offset--;
    } else if ((this.kind.equals(ENDKIND)) && (lastLinkKind.equals(MIDDLEKIND))) {
      for (int i = previousLinkIndex + 1; i < (index + offset); i++) {
        marklist.remove(i);
        offset--;
      }
    }
    while (!kindOfLastLink(marklist).equals(ENDKIND)) {
      int lastLinkIndex = indexOfLastLink(marklist);
      marklist.remove(lastLinkIndex);
    }
    MarkAnalysisResult result = new MarkAnalysisResult(marklist, offset);
    return result;
  }

  /**
   * Given a block of text, and a list of Marks, if this is a start ListMark, returns the markdown
   * link and text before the next mark in HTML format
   *
   * @param input a string representing a block of text
   * @param marklist a list of all valid Marks within input
   * @return if this is the STARTKIND the string up to the ENDKIND mark converted to an HTML link.
   *         If this is ENDKIND, returns the substring between this Mark and the next.
   *
   */
  @Override
  public String toHTML(String input, List<Mark> marklist) {
    if (this.kind.equals(STARTKIND)) {
      int nextLinkIndex = indexOfNextLink(marklist.subList(0, marklist.size()));
      String textShown =
          input.substring(this.positionIndex + 1, marklist.get(nextLinkIndex).getPosition());
      int nextNextLinkIndex =
          indexOfNextLink(marklist.subList(nextLinkIndex, marklist.size())) + nextLinkIndex;
      String actualLink =
          input.substring(marklist.get(nextLinkIndex).getPosition() + LINKMIDDLESIZE,
              marklist.get(nextNextLinkIndex).getPosition());
      int endString = input.length();
      if ((nextNextLinkIndex + 1) < marklist.size()) {
        endString = marklist.get(nextNextLinkIndex + 1).getPosition();
      }
      String textafterlink =
          input.substring(marklist.get(nextNextLinkIndex).getPosition() + 1, endString);
      return HTMLLINKSTART + actualLink + GREATERTHAN + textShown + HTMLLINKEND + textafterlink;
    } else {
      return EMPTYSTRING;
    }
  }
}
