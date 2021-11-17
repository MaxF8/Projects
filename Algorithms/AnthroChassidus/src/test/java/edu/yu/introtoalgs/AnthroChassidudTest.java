package edu.yu.introtoalgs;
import edu.yu.introtoalgs.AnthroChassidus;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AnthroChassidudTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void firstTest()
    {
        int[] a = new int[5];
        a[0] = 1;
        a[1] = 2;
        a[2] = 9;
        a[3] = 7;
        a[4] = 8;



        int[] b = new int[5];
        b[0] = 2;
        b[1] = 3;
        b[2] = 5;
        b[3] = 1;
        b[4] = 4;

        AnthroChassidus UF = new AnthroChassidus(100,a,b);
        // System.out.println(UF.connected(1, 5));
        System.out.println(UF.nShareSameChassidus(1));
    }

    @Test
    public void lowerBoundTest()
    {
        int[] a = new int[1];
        a[0] = 0;
        // a[1] = 2;
        // a[2] = 9;
        // a[3] = 7;
        // a[4] = 8;
        int[] b = new int[1];
        b[0] = 1;
        // b[1] = 3;
        // b[2] = 5;
        // b[3] = 1;
        // b[4] = 4;
        AnthroChassidus UF = new AnthroChassidus(50,a,b);
        // System.out.println(UF.connected(1, 5));
        assertEquals(UF.getLowerBoundOnChassidusTypes(),49);
        assertEquals(UF.nShareSameChassidus(0),2);
        assertEquals(UF.nShareSameChassidus(1),2);

    }    
    @Test
    public void secondTest()
    {
        int[] a = new int[4];
        a[0] = 1;
        a[1] = 4;
        a[2] = 3;
        a[3] = 4;
        // a[4] = 4;
        // a[5] = 8;
        // a[6] = 8;

        int[] b = new int[4];
        b[0] = 3;
        b[1] = 5;
        b[2] = 6;
        b[3] = 1;
        // b[4] = 4;
        // b[5] = 4;
        // b[6] = 4;

        AnthroChassidus UF = new AnthroChassidus(7,a,b);
        // System.out.println(UF.connected(5, 1));
        // System.out.println(UF.nShareSameChassidus(5));
        
        System.out.println("id");
        // for (int i = 0; i < UF.id.length; i++) {
            // System.out.println(i+" - "+UF.id[i]);
        // }
        // System.out.println("size");

        // for (int i = 0; i < UF.id.length; i++) {
            // System.out.println(i+" - "+UF.size[i]);
        // }
     
           
    }   
    @Test
    public void worstCaseTest()
    {
        int[] a = new int[7];
        a[0] = 0;
        a[1] = 2;
        a[2] = 4;
        a[3] = 6;
        a[4] = 0;
        a[5] = 4;
        a[6] = 0;
        // a[7] = 8;


        int[] b = new int[7];
        b[0] = 1;
        b[1] = 3;
        b[2] = 5;
        b[3] = 7;
        b[4] = 2;
        b[5] = 6;
        b[6] = 4;
        // b[7] = 8;


        AnthroChassidus UF = new AnthroChassidus(9,a,b);
        // System.out.println(UF.connected(5, 1));
        // System.out.println(UF.nShareSameChassidus(5));

        // for (int i = 0; i < UF.id.length; i++) {
        //     System.out.println(UF.id[i]);
        // }
     System.out.println();
     System.out.println(UF.getLowerBoundOnChassidusTypes());
           
    }   
    @Test
    public void shareChassidusTest()
    {
        int[] array1 = new int[]{1,2,3,4,5,6,7,8,9,10};
        int[] array2 = new int[]{11,12,13,14,15,1,2,18,19,1};
        int population = 40;
        
        AnthroChassidus chassidus = new AnthroChassidus(population, array1, array2);

        int shareWith1 = chassidus.nShareSameChassidus(1);
        int shareWith2 = chassidus.nShareSameChassidus(2);
        int shareWith3 = chassidus.nShareSameChassidus(3);
        int shareWith10 = chassidus.nShareSameChassidus(10);
        
        assertEquals(4, shareWith1);
        assertEquals(3, shareWith2);
        assertEquals(2, shareWith3);
        assertEquals(4, shareWith10);

    }
    @Test
    public void testMinimal() {
      // There are 50 different people in the group, and the only fact I know is
      // that person 0 and person 1 share the same chassidus
      final int n = 50;
      final int[] a = {0};
      final int[] b = {1};
      final AnthroChassidus ac = new AnthroChassidus(n, a, b);
      assertEquals(ac.getLowerBoundOnChassidusTypes(), 49);
      assertEquals(ac.nShareSameChassidus(0), 2); 
      assertEquals(ac.nShareSameChassidus(1), 2);
    }
    
}
