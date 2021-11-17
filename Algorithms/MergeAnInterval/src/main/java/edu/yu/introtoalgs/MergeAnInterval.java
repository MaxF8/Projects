package edu.yu.introtoalgs;

import java.util.*;

/** Implements the "Add an Interval To a Set of Intervals" semantics defined in
 * the requirements document.
 * 
 * @author Avraham Leff 
 */

public class MergeAnInterval {

  /** An immutable class, holds a left and right integer-valued pair that
   * defines a closed interval
   *
   * IMPORTANT: students may not modify the semantics of the "left", "right"
   * instance variables, nor may they use any other constructor signature.
   * Students may (are encouraged to) add any other methods that they choose,
   * bearing in mind that my tests will ONLY DIRECTLY INVOKE the constructor
   * and the "merge" method.
   */
  public static class Interval implements Comparable<Interval>{
    public final int left;
    public final int right;
    
    /** Constructor
     * 
     * @param left the left endpoint of the interval, may be negative
     * @param right the right endpoint of the interval, may be negative
     * @throws IllegalArgumentException if left is >= right
     */
    public Interval(int l, int r) 
    {
        if (l >= r)
        throw new IllegalArgumentException();

        this.left = l;
        this.right = r;
    }

    @Override
    public int compareTo(Interval o) 
    {
      int newIntervalLeft = this.left;
      int newIntervalRight = this.right;
      int intervalLeft = o.left;
      int intervalRight = o.right;
     
      if (newIntervalLeft >= intervalRight)
      {
        return 1;
      }
      else if (newIntervalRight <= intervalLeft)
      {
        return -1;
      }
        return 0;
    }
    
    @Override 
    public boolean equals(Object interval) 
    {
      if (interval != null && this != null)
      {
          if (interval instanceof Interval)
          {   
              Interval intervalTest = (Interval) interval;

              if (intervalTest.left == this.left && intervalTest.right == this.right)
              {
                  return true;
              }
          }
      }
      return false;
    }
    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + this.left;
        hash = 31 * hash + this.right;
        return hash;
    }
  } // Interval class

  /** Merges the new interval into an existing set of disjoint intervals.
   *
   * @param intervals an set of disjoint intervals (may be empty)
   * @param newInterval the interval to be added
   * @return a new set of disjoint intervals containing the original intervals
   * and the new interval, merging the new interval if necessary into existing
   * interval(s), to preseve the "disjointedness" property.
   * @throws IllegalArgumentException if either parameter is null
   */
  public static Set<Interval> merge(final Set<Interval> intervals, Interval newInterval)
  {
    if (intervals == null || newInterval == null)
    throw new IllegalArgumentException();

    TreeSet<Interval> treeSet = new TreeSet<>(intervals);

    ArrayList<Interval> newList = new ArrayList<>();
    Interval comparedInterval = newInterval;
    newList.add(treeSet.first());
    
    for (Interval interval : treeSet) {
      int newIntervalLeft = comparedInterval.left;
      int newIntervalRight = comparedInterval.right;
      int intervalLeft = interval.left;
      int intervalRight = interval.right;
     

      //ADD AFTER: [5,7] -> [2,4] = [2,4],[5,7] 
      if (newIntervalLeft > intervalRight) 
      {
        newList.set(newList.size()-1,interval);
        newList.add(comparedInterval);
      }
      //ADD BEFORE: [2,4] -> [5,7] = [2,4],[5,7] 
      else if (newIntervalRight < intervalLeft) {
        newList.set(newList.size()-1, comparedInterval);
        newList.add(interval);
        
        comparedInterval = interval;
      }
      //merge
      else if (newIntervalLeft <= intervalRight && newIntervalRight >= intervalLeft) {
        int smallerLeft = 0;
        int greaterRight = 0;
  
        if (newIntervalLeft <= intervalLeft)
          smallerLeft = newIntervalLeft;
        else 
          smallerLeft = intervalLeft;
  
        if (newIntervalRight >= intervalRight)
          greaterRight = newIntervalRight;
        else 
          greaterRight = intervalRight;

        Interval newMergedInterval = new Interval(smallerLeft, greaterRight);

        newList.set(newList.size() - 1, newMergedInterval);

        comparedInterval = newMergedInterval;

      }
    }

    //print intervals:
    // System.out.println();
    // System.out.println("FINAL LIST:");

    // for (Interval interval2 : newList) {
    //   System.out.print("interval: ");
    //   System.out.print("left: " +interval2.left+", ");
    //   System.out.println("right: " +interval2.right); 
    // }
    // System.out.println();

    HashSet<Interval> returnedSet = new HashSet<>(newList);

    return returnedSet;
  }

  

  
}