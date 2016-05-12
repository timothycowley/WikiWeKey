package wekey.interpreter.marks;

import java.util.List;

/**
 * The Mark Interface provides the methods for In Line Marks
 *
 */
public interface Mark {

  /**
   * The getter for positionIndex: the index this Mark appears at in the String representation of
   * the text that contains the markdown
   *
   * @return positionIndex for this Mark
   */
  public int getPosition();

  /**
   * The getter for the kind of this Mark. The kind of the Mark is dependent on the class, and
   * servers to distinguish different kinds of the same Mark class (examples for some marks include
   * starting kinds and ending kinds)
   *
   * @return kind for this Mark
   */
  public String getKind();

  // > The design relies on specific Strings for the "types" of marks. Then the code has if
  // > statements with string equality methods. It would have been better to create on this
  // > interface an equalType method that takes in another Mark and returns true if it is the same
  // > and false otherwise. The all those if statements that repeat throughout the code are
  // > encapsulated in each class' equalType method.


  // > -2



  /**
   * The getter for the type of Mark, the type of Mark is a String that identifies the class of the
   * Mark
   *
   * @return Type for this Mark
   */
  public String getMarkType();

  /**
   * Scans a string for markdown Marks and constructs an in-order list of those Marks
   *
   * @param input the string to scan for markdown Marks
   * @param markList list of Marks found so far in input
   * @param i the current index of the String being read
   * @return markList with a Mark added if a Mark of the recognition site for this Mark type exists
   *         at index i
   */
  public List<Mark> readMarkdown(String input, List<Mark> markList, int i);

  /**
   * Analyzes a list of Marks and based on the context of the other Marks determines if this Mark or
   * others are interpretable and if not, removes them from the list and returns a MarkAnalysis
   * result containing the updated Mark list and an index offset to account for Mark removals
   *
   * @param markList a List of Marks that contains this Mark
   *
   *        //> the parameter name is marklist not markList :)
   *
   * @param index the position this Mark exists at within the String the markList represents
   * @return a MarkAnalysisResult containing the updated markList and an index offset to account for
   *         any deleted Marks
   */
  public MarkAnalysisResult analyzeMarkList(List<Mark> marklist, int index);

  /**
   * Converts a substring associated with this Mark to HTML formated text such that when this method
   * is called on all Marks the entire input is converted to HTML
   *
   * @param input a String representation of the text to be converted to HTML
   * @param marklist a List of Marks in input
   * @return a substring of input represented by this Mark formated as HTML
   */
  public String toHTML(String input, List<Mark> marklist);



}
