package edu.yu.introtoalgs;
import edu.yu.introtoalgs.AnthroChassidus;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
  // while (p != id[p]) 
      // {
      //   id [p] = id[id[p]];
      //   p = id[p];
      // }
      
    //   int root = p;
    //   while (root != id[root]) 
    //   {
    //     root = id[root];
    //   }
    //   while (p != root) {
    //     int newp = id[p];
    //     id[p] = root;
    //     p = newp;
    // }
    // 
    //   private boolean connected(int p, int q)
    // { 
    //     boolean connected = (find(p) == find(q));
    //     return connected;
    //   }
    //   private int count()
    //   { 
    //     return count; 
    //   }
public class WeightedQuickUnionTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void firstTest()
    {
        WeightedQuickUnionUF UF = new WeightedQuickUnionUF(100);
        // System.out.println(UF.count());
        // System.out.println(UF.connected(2,9));
        UF.union(24, 3);
        UF.union(3, 9);
        // UF.union(3, 1);
        // UF.union(3, 3);
        // UF.union(3,9);
        System.out.println(UF.find(9));


        // System.out.println(UF.connected(3,2));

    }
}
