package edu.yu.introtoalgs;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.util.TreeSet;

import org.junit.Test;

import edu.yu.introtoalgs.MergeAnInterval.Interval;

/**
 * Unit test for simple App.
 */
public class MergeAnIntervalTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void treeSet()
    {
        TreeSet<Integer> set1= new TreeSet<>();
        set1.add(60);
        set1.add(20);
        set1.add(30);
        set1.add(40);
        set1.add(50);
        System.out.println();
    }
    @Test
    public void basicTest()
    {
      Interval interval1 = new Interval(1,2);
      Interval interval2 = new Interval(3,4);
      Interval interval3 = new Interval(5,6);

      Interval interval4 = new Interval(7,8);

      HashSet<Interval> set = new HashSet<Interval>();
      set.add(interval1);
      set.add(interval2);
      set.add(interval3);

    //   MergeAnInterval mergeClass = new MergeAnInterval();
    //   Set<Interval> set2 = MergeAnInterval.merge(set, interval4);
    //   int counter = 0;
    //   for (Interval interval : set2) {
    //     System.out.print("interval: " +counter+" ");
    //     System.out.print("left: " +interval.left+" ");
    //     System.out.println("right: " +interval.right);
    //     counter++;

    // }



    }
    
    @Test
    public void mergeTest()
    {
      Interval interval1 = new Interval(1,3);

      Interval interval2 = new Interval(4,7);
      Interval interval3 = new Interval(9,10);
      // Interval interval3 = new Interval(7,8);

      Interval newInterval = new Interval(0,8);
     
      ArrayList<Interval> list = new ArrayList<Interval>();
      list.add(interval1);
      list.add(interval2);
      list.add(interval3);


      HashSet<Interval> set = new HashSet<Interval>(list);
       
        Set<Interval> mergeSet = MergeAnInterval.merge(set, newInterval);
        int counter = 0;
        System.out.println("SIZE: "+mergeSet.size());
        System.out.println("RETURNED SET:");

        for (Interval interval: mergeSet) {
          System.out.print("INTERVAL " +counter+ ": ");
          System.out.print("left: " +interval.left+", ");
          System.out.println("right: " +interval.right); 
          counter++;
        }


    }
    @Test
    public void addAfterTest()
    {

      Interval interval1 = new Interval(2,4);
      Interval interval2 = new Interval(6,7);
      Interval interval3 = new Interval(7,8);

      Interval newInterval = new Interval(9,10);
     
      ArrayList<Interval> list = new ArrayList<Interval>();
      list.add(interval1);
      list.add(interval2);
      list.add(interval3);



      // System.out.println("set");
      HashSet<Interval> set = new HashSet<Interval>(list);
      // for (Interval interval : set) {
      //   System.out.println(interval);
      // }
      // System.out.println("set SIZE: "+set.size());
      
      //   set.add(interval1);
      //    set.add(interval2);
      // set.add(interval2);

   
       
        Set<Interval> mergeSet = MergeAnInterval.merge(set, newInterval);
        int counter = 0;
        System.out.println("SIZE: "+mergeSet.size());
        System.out.println("RETURNED SET:");

        for (Interval interval: mergeSet) {
          System.out.print("INTERVAL " +counter+ ": ");
          System.out.print("left: " +interval.left+", ");
          System.out.println("right: " +interval.right); 
          counter++;
        }

    }
    @Test
    public void addBeforeTest()
    {
      Interval interval1 = new Interval(7,14);
      Interval interval2 = new Interval(15,18);

      // Interval interval3 = new Interval(2,3);
      // Interval interval4 = new Interval(5,6);
      // Interval interval3 = new Interval(11,24);
      // Interval interval4 = new Interval(240,241);

      Interval newInterval = new Interval(8,16);
     
      ArrayList<Interval> list = new ArrayList<Interval>();
      list.add(interval1);
      list.add(interval2);
      // list.add(interval3);
      // list.add(interval4);



      HashSet<Interval> set = new HashSet<Interval>(list);
       
        Set<Interval> mergeSet = MergeAnInterval.merge(set, newInterval);
        int counter = 0;
        System.out.println("SIZE: "+mergeSet.size());
        System.out.println("RETURNED SET:");

        for (Interval interval: mergeSet) {
          System.out.print("INTERVAL " +counter+ ": ");
          System.out.print("left: " +interval.left+", ");
          System.out.println("right: " +interval.right); 
          counter++;
        }


    }
    @Test
    public void AddBeforeAndAfterTest()
    {
      Interval interval1 = new Interval(0,2);
      // Interval interval2 = new Interval(3,4);
      // Interval interval3 = new Interval(7,8);

      Interval newInterval = new Interval(1,4);
     
      ArrayList<Interval> list = new ArrayList<Interval>();
      list.add(interval1);
      // list.add(interval2);
      // list.add(interval3);


      HashSet<Interval> set = new HashSet<Interval>(list);
       
        Set<Interval> mergeSet = MergeAnInterval.merge(set, newInterval);
        int counter = 0;
        System.out.println("SIZE: "+mergeSet.size());
        System.out.println("RETURNED SET:");

        for (Interval interval: mergeSet) {
          System.out.print("INTERVAL " +counter+ ": ");
          System.out.print("left: " +interval.left+", ");
          System.out.println("right: " +interval.right); 
          counter++;
        }


    }
    @Test
    public void hashAndEqualsTest()
    {
      Interval interval1 = new Interval(5,9);
      Interval interval2 = new Interval(5,9);
      System.out.println(interval1.hashCode());
      System.out.println(interval2.hashCode());

      if (interval1.equals(interval2))
        System.out.println("true");
      else
        System.out.println("false");
    }
    @Test
    public void variousTests()
    {
      Interval interval1 = new Interval(2,3);
      Interval interval2 = new Interval(5,9);
      Interval interval3 = new Interval(11,24);
      Interval interval4 = new Interval(240,241);
      Interval interval5 = new Interval(120,122);
      Interval interval6 = new Interval(144,145);
      Interval interval7 = new Interval(25,29);
      Interval interval8 = new Interval(0,1);
      Interval interval9 = new Interval(-12,-1);
      // Interval interval3 = new Interval(7,8);

      Interval newInterval = new Interval(5,121);

      // newIntervalLeft <= intervalRight && newIntervalRight >= intervalLeft) 
     
      ArrayList<Interval> list = new ArrayList<Interval>();
      list.add(interval1);
      list.add(interval2);
      list.add(interval3);
      list.add(interval4);
      list.add(interval5);
      list.add(interval6);
      list.add(interval7);
      list.add(interval8);
      list.add(interval9);


      HashSet<Interval> set = new HashSet<Interval>(list);
       
        Set<Interval> mergeSet = MergeAnInterval.merge(set, newInterval);
        int counter = 0;
        System.out.println("SIZE: "+mergeSet.size());
        System.out.println("RETURNED SET:");

        for (Interval interval: mergeSet) {
          System.out.print("INTERVAL " +counter+ ": ");
          System.out.print("left: " +interval.left+", ");
          System.out.println("right: " +interval.right); 
          counter++;
        }


    }
    @Test
    public void mergeTestRandom()
    {
        Set<Interval> intervals = new HashSet<>();

        for(int i = 0; i < 25; i++)
        {
            int a = getRandomInteger(i);
            int b = a + 1 + getRandomInteger(i);

            Interval inter = new Interval(a, b);
            intervals.add(inter);
        }

        for(int i = 100; i < 125; i++)
        {
            int a = getRandomInteger(i);
            int b = a + 1 + getRandomInteger(i);

            Interval inter = new Interval(a, b);
            intervals.add(inter);
        }

        for(int i = 200; i < 214; i++)
        {
            int a = getRandomInteger(i);
            int b = a + 1 + getRandomInteger(i);

            Interval inter = new Interval(a, b);
            intervals.add(inter);
        }
        System.out.println("Unsorted Set: \n");
        int line = 1;
        for(Interval in: intervals)
        {
            System.out.println(line + ". [" + in.left + "," + in.right + "]");
            line++;
        }
        Interval n = new Interval(0, 1);
        Set<Interval> merged = MergeAnInterval.merge(intervals,n);
        System.out.println("______ \n");
        System.out.println("Sorted Set: \n");
        line = 1;
        for(Interval in: merged)
        {
            System.out.println(line + ". [" + in.left + "," + in.right + "]");
            line++;
        }
    }
    @Test 
    public void speedTest()
    {
        System.out.println("\n\nSpeed Test:\n+---------------+\n");
        double[] logY = new double[10];
        double[] logX = new double[10];
        int logArrayIndex = 0; 
        for(int i = 10; i < 100000000; i*=10)
        {
            Set<Interval> intervals = new HashSet<Interval>();
            for(int j = 0; j < i; j++)
            {
                int a = getRandomInteger(j);
                int b = a + 1 + getRandomInteger(j);
                Interval inter = new Interval(a, b);
                intervals.add(inter);
            }
            Interval x = new Interval(0, 1);
            long startTime = System.nanoTime();
            MergeAnInterval.merge(intervals,x);
            long endTime = System.nanoTime();
            long totalTime = endTime-startTime;
            double seconds = (double) totalTime/1000000000;
            
            int n = i;
            double logN = Math.log(i);
            double fn = seconds;
            double logFn = Math.log(fn);

            System.out.println("n = " + n + "\nLog(n) = " + logN + "\nF(n) = " + fn + "\nLog(F(n)) = " + logFn + "\n");
            logX[logArrayIndex] = logN;
            logY[logArrayIndex] = logFn;
            logArrayIndex++;
            if(fn > 60)
            {
                break;
            }
        }
        double x = logX[2] - logX[logArrayIndex];
        double y = logY[logArrayIndex] - logY[2];
        double slope = x/y;
        System.out.println("x = "+x);
        System.out.println("y = "+y);
        System.out.println("\nAverage Slope of the Log Graph: " + slope);
        System.out.println("+-------------------+");
    }
    private static int getRandomInteger(int max)
    {
        double random = Math.floor(Math.random() * max);
        return (int)random;
    } 
    

}
