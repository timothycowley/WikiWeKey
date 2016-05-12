package wekey.interpreter.blockanalyzers;

import java.util.List;

public class TextBlockAnalyzerResult {
  private List<String> resultList;
  private int indexAdjustment;


  /**
   * Constructor
   *
   * @param resultList a list of lines
   * @param indexAdjustment is the new index after text lines are combined into strings for text
   *        blocks
   */
  public TextBlockAnalyzerResult(List<String> resultList, int indexAdjustment) {
    this.resultList = resultList;
    this.indexAdjustment = indexAdjustment;
  }

  /**
   * Getter for resultList
   *
   * @return this resultList
   */
  public List<String> getResultList() {
    return this.resultList;
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
