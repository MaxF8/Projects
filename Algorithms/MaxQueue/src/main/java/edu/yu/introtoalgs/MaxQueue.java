package edu.yu.introtoalgs;

import java.util.LinkedList;

/** Enhances the Queue enqueue() and dequeue() API with a O(1) max()
 * method and O(1) size().  The dequeue() method is O(1), the enqueue
 * is amortized O(1).  The implementation is O(n) in space.
 *
 * @author Avraham Leff
 */

import java.util.NoSuchElementException;

public class MaxQueue {


    Integer[] queue;
    LinkedList<Integer> maxList;
    int count;
    int head;
    int tail;
    int max;

   public MaxQueue(){
        this.count = 0;
        this.queue = new Integer[2];
        this.head = 0;
        this.tail = 1;
        this.maxList = new LinkedList<>();  
   }

 /** Insert the element with FIFO semantics
   *
   * @param x the element to be inserted.
   */

  public void enqueue(int x) {
    if (this.count == this.queue.length) //array double
    { 
       Integer[] newArray = arrayDouble(this.queue);
        this.queue = newArray;
    }
      int tail = (this.head + this.count) % this.queue.length;
      this.queue[tail] = x;
      
         if (this.maxList.size() == 0 || x <= this.maxList.peekLast() ) //if x > last value in list, add to maxList
        {
          if (this.maxList.size() == 0)
          this.max = x;

          this.maxList.add(x);
        }
        else //else (x > last) replace last with x
        {
          while (this.maxList.size() > 0 && x > this.maxList.peekLast())
          {
            this.maxList.removeLast();
          }
          this.maxList.add(x);
          
          if (x > this.max)
          this.max = x;
        }
        this.count++;
  }

  private Integer[]  arrayDouble(Integer[] oldArray){ //double array
    Integer[] newArray = new Integer[oldArray.length*2];
    for(int i = 0; i < oldArray.length;i++){
        newArray[i] = oldArray[i];
    }
    return newArray;
  }

  /** Dequeue an element with FIFO semantics.
   *
   * @return the element that satisfies the FIFO semantics if the queue is not
   * empty.
   * @throws NoSuchElementException if the queue is empty
   */
  public int dequeue() {
      if (this.count == 0)
        throw new NoSuchElementException();
        
        int x = this.queue[this.head];
        this.queue[this.head] = null; 
        this.head = (this.head + 1)%this.queue.length;

        //if x is max, delete from maxList
        if (x == this.maxList.getFirst())
        {
          maxList.removeFirst();

          if (this.maxList.size() > 0)
          this.max = this.maxList.getFirst(); //max = next value in maxList
          else
          this.max = 0;
        }
        this.count--;

        return x;
  }

  /** Returns the number of elements in the queue
   *
   * @return number of elements in the queue
   */
  public int size() {
      return this.count;
  }

  /** Returns the element with the maximum value
   * 
   * @return the element with the maximum value
   * @throws NoSuchElementException if the queue is empty
   */
  public int max() {
      return this.max;
  }
} 