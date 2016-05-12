package wekey.interpreter.marks;

import java.util.List;

/**
 * MarkAnalysisResult contains the results of the analyzeMarkList method. These are the markList and
 * an adjustment to the index
 *
 * @author Tim
 *
 */
public class MarkAnalysisResult {
  private List<Mark> markList;
  private int indexAdjustment = 0;

  /**
   * Constructor for when index does not need updating
   *
   * @param markList a list of Marks
   */
  public MarkAnalysisResult(List<Mark> markList) {
    this.markList = markList;
  }

  /**
   * Constructor
   *
   * @param markList a list of Marks
   * @param indexAdjustment how much the index needs to be adjusted due to the addition or removal
   *        of Marks
   */
  public MarkAnalysisResult(List<Mark> markList, int indexAdjustment) {
    this.markList = markList;
    this.indexAdjustment = indexAdjustment;
  }

  /**
   * Getter for markList
   *
   * @return this markList
   */
  public List<Mark> getMarkList() {
    return this.markList;
  }

  /**
   * Getter for indexAdjustment
   *
   * @return this indexAdjustment
   */
  public int getIndexAdjustment() {
    return this.indexAdjustment;
  }
}
