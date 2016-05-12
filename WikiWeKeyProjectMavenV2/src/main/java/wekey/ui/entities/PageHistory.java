package wekey.ui.entities;


import java.util.NoSuchElementException;

import wekey.ui.pages.EditPage;
import wekey.ui.pages.Page;
import wekey.ui.pages.PreviewPage;

public class PageHistory {

  private static final int MULTIPLE_TO_EXPAND_CAPACITY = 2;
  private static final int INITIAL_CAPACITY = 5;
  private Page[] pageList; // array storing pages
  // > code would have been easier if ArrayList was used or some other java.util type that allows
  // indexes.

  // > -1

  private int capacity; // array capacity
  private int currentIndex; // current page
  private int size; // number of valid pages in the history list

  /**
   * Constructor to create a PageHistory object
   */
  public PageHistory() {
    this.capacity = INITIAL_CAPACITY;
    this.currentIndex = 0;
    this.pageList = new Page[this.capacity];
    this.size = 0;
  }

  /**
   * This method returns the current Page in the history list to be displayed on UI
   *
   * @return the current page
   */
  public Page getCurrent() {
    return this.pageList[this.currentIndex];
  }

  /**
   * This method returns the previous page of the current page in the history list and set current
   * page as the page <post> currentPage() = previous page of currentPage() </post>
   *
   * @return the previous page of the current page
   */
  public Page getPrevious() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    if ((this.pageList[this.currentIndex - 1] instanceof EditPage)
        // > instanceof, is a bad smell. If the code needs to distinguish between pages the design
        // should accommodate for it.

        || (this.pageList[this.currentIndex - 1] instanceof PreviewPage)) {
      this.currentIndex--;
      return getPrevious();
    } else {
      return this.pageList[--this.currentIndex];
    }
  }


  /**
   * This method returns the next page of the current page in the history list and set current page
   * as the page <post> currentPage() = next page of currentPage() </post>
   *
   * @return the next page of the current page
   */
  public Page getNext() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    if ((this.pageList[this.currentIndex + 1] instanceof EditPage)
        || (this.pageList[this.currentIndex + 1] instanceof PreviewPage)) {
      this.currentIndex++;
      return getNext();
    } else {
      return this.pageList[++this.currentIndex];
    }
  }


  /**
   * Returns the number of Edit or Preview Pages after the current page
   *
   * @return the number of remaining unwanted pages after the current page
   */
  private int countRestUnwantedPage() {
    int count = 0;
    for (int i = this.currentIndex + 1; i < this.size; i++) {
      if ((this.pageList[i] instanceof EditPage) || (this.pageList[i] instanceof PreviewPage)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Add a new page element to the list <post> appends the given value to the end of the list</post>
   *
   * //> no pre-condition?
   *
   * //> -1
   *
   * //> no Javadoc param here
   */
  public void add(final Page n) {
    if (((this.size - 1) != this.currentIndex) && (this.size != 0)) {
      this.size = this.currentIndex + 1;
    }
    if (isEmpty()) {
      this.pageList[this.currentIndex] = n;
      this.size++;
    } else {
      this.pageList[++this.currentIndex] = n;
      this.size++;
    }
    if ((this.size + 1) == this.capacity) {
      this.capacity = this.capacity * MULTIPLE_TO_EXPAND_CAPACITY;
      final Page[] temp = new Page[this.capacity];
      for (int i = 0; i < this.size; i++) {
        temp[i] = this.pageList[i];
      }
      this.pageList = temp;
    }
  }

  /**
   * this method return true if there is a next page in the PageHistory object, false otherwise
   *
   * @returns whether there is a next page in history with respect to the current page. True if
   *          there is one, false otherwise
   *
   *
   *          //> returns is not a tag, return is
   *
   *          //> -1
   */
  public boolean hasNext() {
    return (this.currentIndex < (this.size - 1))
        && (countRestUnwantedPage() != (this.size - 1 - this.currentIndex));
  }

  /**
   * this method return true if there is a previous page in the PageHistory object, false otherwise
   *
   * @returns whether there is a previous page in history with respect to the current page. True if
   *          there is one, false otherwise
   */
  public Boolean hasPrevious() {
    return this.currentIndex >= 1;
  }

  /**
   * his method return true if the PageHistory object contains no Page, false otherwise
   *
   * @returns whether if the history is empty. True if there is no Page in the list, false otherwise
   */
  private boolean isEmpty() {
    return this.size == 0;
  }

  /**
   * Testing method.
   *
   * <pre>
   * !isEmpty()
   * </pre>
   *
   * @returns a String displaying all type of pages in the history list
   */
  public String printAllPages() {
    String all = "";
    all = all + this.pageList[0].toString();
    for (int i = 1; i < this.size; i++) {
      all = all + ", " + this.pageList[i].toString();
    }
    return all;
  }

}
