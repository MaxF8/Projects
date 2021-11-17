package edu.yu.introtoalgs;

import static org.junit.Assert.assertTrue;
import java.util.*;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class MaxQueueTest {
    @Test
    public void basicTest() {
        ArrayDeque<Integer> queue = new ArrayDeque<Integer>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.remove();



        MaxQueue mq = new MaxQueue();
        mq.enqueue(590);
        mq.enqueue(380);


        mq.enqueue(4);
        // mq.enqueue(500);
        // mq.enqueue(66);
        mq.enqueue(7);
        // mq.enqueue(3);
        // mq.enqueue(12);
        mq.enqueue(490);
        // mq.enqueue(22);




        int x = mq.dequeue();
        // System.out.println(x);
        int x2 = mq.dequeue();
        // System.out.println(x2);
        System.out.println(mq.size());
        System.out.println(mq.max());




        // mq.dequeue();
        // mq.dequeue();
        // mq.dequeue();
        // mq.dequeue();
        // mq.dequeue();
        // mq.dequeue();

        // mq.dequeue();
        // mq.dequeue();






        // mq.enqueue(4);
        // // mq.dequeue();
        // // mq.dequeue();

        // mq.enqueue(5);
        // // mq.enqueue(5);
        // // mq.enqueue(6);
        // // mq.enqueue(7);
        // mq.dequeue();
        // mq.dequeue();
        // mq.enqueue(6);



      

        // // System.out.println(mq.size());
        System.out.println();
    }

    @Test
    public void maxTestAscending() {
        for (int i = 0; i < 25; i++)
        {
        MaxQueue mq = new MaxQueue();

        long startTime = System.nanoTime();
        for (int j = 0; j < i * 1000000; j++) {
            mq.enqueue(j);
        }
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        double seconds = (double) total / 1000000000;
      
       System.out.println(seconds);
    
    }
     
        System.out.println();
    }
    @Test
    public void maxTestDescending() {
        MaxQueue mq = new MaxQueue();

        mq.enqueue(12);
        // mq.enqueue(19);
        // mq.enqueue(10);

        // mq.enqueue(9);
        // mq.enqueue(8);
        // mq.enqueue(7);
        // mq.enqueue(6);
        // mq.enqueue(17);

        mq.dequeue();



     
        System.out.println();
    }
    @Test
    public void maxTestSimple() {
        MaxQueue mq = new MaxQueue();

        mq.enqueue(12);
        mq.enqueue(11);
        mq.enqueue(19);
        mq.enqueue(10);
        mq.enqueue(9);
        mq.enqueue(8);
        mq.enqueue(7);
        mq.enqueue(6);
        mq.enqueue(5);
        mq.enqueue(4);

        mq.enqueue(17);


        // mq.enqueue(12);
        // mq.enqueue(6);
        // mq.enqueue(1);

        
        mq.dequeue();
        mq.dequeue();
        mq.dequeue();

        mq.dequeue();
        mq.dequeue();
        mq.dequeue();
        mq.dequeue();
        mq.dequeue();
        mq.dequeue();
        mq.dequeue();
        mq.dequeue();





        System.out.println();
    }
    @Test
    public void maxTestComplicated() {
        MaxQueue mq = new MaxQueue();
        for (int i = 51; i > 1; i--) {
            mq.enqueue(i);
        }
        for (int i = 1; i < 51; i++) {
            mq.dequeue();
        }
        // System.out.println(mq.size());
        // mq.enqueue(7);
        // mq.enqueue(3);
        // mq.enqueue(5);

        // // mq.enqueue(12);
        // // mq.enqueue(6);
        // // mq.enqueue(1);

        
        // mq.dequeue();
        // mq.dequeue();
        // mq.dequeue();
        // mq.enqueue(99);

        // mq.dequeue();
        // mq.dequeue();
        // mq.dequeue();


        System.out.println();
    }
}
