package edu.yu.introtoalgs;

import edu.yu.introtoalgs.IntersectRectangles.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class IntersectRectanglesTest {
   
    @Test
    public void simpleRectangleTest()
    {
        Rectangle rectangle1 = new Rectangle(1,1,3,4);
        Rectangle rectangle2 = new Rectangle(2,3,2,2);
        System.out.println(rectangle1.height);

        Rectangle rectangle3 = IntersectRectangles.intersect(rectangle1,rectangle2);
        if (rectangle3 != null)
            {
                System.out.println(rectangle3.height);

            }
        else{
            System.out.println("no intersect");
        }
    }
    @Test
    public void simpleRectangleTest2()
    {
        Rectangle rectangle1 = new Rectangle(9,4,2,2);
        Rectangle rectangle2 = new Rectangle(10,1,2,3);

        Rectangle rectangle3 = new Rectangle(10,4,4,5);
        Rectangle rectangle4 = new Rectangle(13,5,2,3);


        Rectangle rectangle5 = new Rectangle(4,4,4,4);
        Rectangle rectangle6 = new Rectangle(7,5,4,2);


        // Rectangle newRectangle12 = IntersectRectangles.intersect(rectangle1,rectangle2);
        // if (newRectangle12 != null)
        //     {
        //         System.out.println(newRectangle12.height);
        //     }
        // else{
        //     System.out.println("no intersect");
        // }
        // Rectangle newRectangle34 = IntersectRectangles.intersect(rectangle3,rectangle4);
        // if (newRectangle34 != null)
        //     {
        //         System.out.println(newRectangle34.height);
        //     }
        // else{
        //     System.out.println("no intersect");
        // }
        Rectangle newRectangle56 = IntersectRectangles.intersect(rectangle5,rectangle6);
        if (newRectangle56 != null)
            {
                System.out.println(newRectangle56.height);
            }
        else{
            System.out.println("no intersect");
        }
    }
    @Test
    public void rectangleTest3()
    {
        Rectangle rectangle1 = new Rectangle(4,3,2,4);
        Rectangle rectangle2 = new Rectangle(5,5,6,7);

        Rectangle rectangle3 = new Rectangle(10,3,3,4);
        Rectangle rectangle4 = new Rectangle(5,5,6,7);

        Rectangle rectangle5 = new Rectangle(2, 2, 3, 2);
        Rectangle rectangle6 = new Rectangle(4, 1, 3, 2);


        // Rectangle rectangle5 = new Rectangle(4,4,4,4);
        // Rectangle rectangle6 = new Rectangle(7,5,4,2);

        // Rectangle newRectangle12 = IntersectRectangles.intersect(rectangle1,rectangle2);
        // if (newRectangle12 != null)
        //     {
        //         System.out.println(newRectangle12.height);
        //     }
        // else{
        //     System.out.println("no intersect");
        // }
        // Rectangle newRectangle34 = IntersectRectangles.intersect(rectangle3,rectangle4);
        // if (newRectangle34 != null)
        //     {
        //         System.out.println(newRectangle34.height);
        //     }
        // else{
        //     System.out.println("no intersect");
        // }
        Rectangle newRectangle56 = IntersectRectangles.intersect(rectangle5,rectangle6);
        if (newRectangle56.height != -1)
            {
                System.out.println(newRectangle56.height);
            }
        else{
            System.out.println("no intersect");
        }
    }
   

    @Test
    public void extensiveRectangleTest()
    {
        Rectangle rectangleA = new Rectangle(4,3,2,4);
        Rectangle rectangleB = new Rectangle(10,3,3,4);
        Rectangle rectangleC = new Rectangle(10,10,1,4);
        Rectangle rectangleD = new Rectangle(3,10,3,4);

        Rectangle rectangleE = new Rectangle(5,5,6,7);

        System.out.println("AE");
        Rectangle newRectangleAE = IntersectRectangles.intersect(rectangleA,rectangleE);
        if (newRectangleAE.height != -1)
            {
                System.out.println("match");
            }
        else{
            System.out.println("no intersect");
        }

        System.out.println("BE");
        Rectangle newRectangleBE = IntersectRectangles.intersect(rectangleB,rectangleE);
        if (newRectangleBE.height != -1)
            {
                System.out.println("match");
            }
        else{
            System.out.println("no intersect");
        }
        System.out.println("CE");
        Rectangle newRectangleCE = IntersectRectangles.intersect(rectangleC,rectangleE);
        if (newRectangleCE.height != -1)
            {
                System.out.println("match");
            }
        else{
            System.out.println("no intersect");
        }
        System.out.println("DE");
         Rectangle newRectangleDE = IntersectRectangles.intersect(rectangleD,rectangleE);
        if (newRectangleDE.height != -1)
            {
                System.out.println("match");
            }
        else{
            System.out.println("no intersect");
        }
        System.out.println("EE");
          Rectangle newRectangleEE = IntersectRectangles.intersect(rectangleE,rectangleE);
        if (newRectangleEE.height != -1)
            {
                System.out.println("match");
            }
        else{
            System.out.println("no intersect");
        }



    }
    @Test
    public void demo ( ) {
    final Rectangle D = new Rectangle (0 , 0 , 2 , 5) ;
    System.out.println("r1: "+D.x+","+D.y+","+D.width+","+D.height);
    final Rectangle F = new Rectangle ( 2 , 0 , 2 , 5) ;
    System.out.println("r2: "+F.x+","+F.y+","+F.width+","+F.height);
    System.out.println("intersect of D and F");
    System.out.println((IntersectRectangles.intersect(D,F).x));//+","+IntersectRectangles.intersect(D,F).y+","+IntersectRectangles.intersect(D,F).width+","+IntersectRectangles.intersect(D,F).height);
    // assertEquals ("same? ", new Rectangle (2 ,0 ,0 ,5) ,IntersectRectangles.intersect(D , F));
    }
    
    @Test
    public void noIntersectionTest()
    {
        Rectangle rectangleA = new Rectangle(-1,0,1,0);
        Rectangle rectangleB = new Rectangle(10,3,3,4);
        Rectangle rectangleC = new Rectangle(3,3,3,3);
        Rectangle rectangleD = new Rectangle(4,4,4,4);


        Rectangle rectangleE = new Rectangle(0,0,0,0);

        // System.out.println("AE");
        // Rectangle newRectangleAE = IntersectRectangles.intersect(rectangleA,rectangleE);
        // if (newRectangleAE.height != -1)
        //     {
        //         System.out.println("match");
        //     }
        // else{
        //     System.out.println("no intersect");
        // }
        System.out.println("CD");
        Rectangle newRectangleCD = IntersectRectangles.intersect(rectangleC,rectangleD);
        if (newRectangleCD.height != -1)
            {
                System.out.println("match");
            }
        else{
            System.out.println("no intersect");
        }
    }
    @Test 
    public void equalsTest() {

        Rectangle rectangleEqual1 = new Rectangle(4,4,4,4);
        Rectangle rectangleEqual2 = new Rectangle(4,4,4,4);
        Rectangle rectangleNotEqual1 = new Rectangle(1,3,2,4);
        Rectangle rectangleNotEqual2 = new Rectangle(1,3,2,5);

        assertEquals(rectangleEqual1, rectangleEqual2);
        assertNotEquals(rectangleNotEqual1, rectangleNotEqual2);
        
    }


    @Test 
    public void hashCodeTest() {

        Rectangle rectangleEqual1 = new Rectangle(4,4,4,4);
        Rectangle rectangleEqual2 = new Rectangle(4,4,4,4);
        Rectangle rectangleNotEqual1 = new Rectangle(1,3,2,4);
        Rectangle rectangleNotEqual2 = new Rectangle(1,3,2,5);

        assertEquals(rectangleEqual1.hashCode(), rectangleEqual2.hashCode());
        assertNotEquals(rectangleNotEqual1.hashCode(), rectangleNotEqual2.hashCode());

    }
    @Test 
    public void exceptionTest() {

        Rectangle rectangleEqual1 = new Rectangle(4,4,4,4);
        Rectangle rectangleEqual2 = new Rectangle(4,4,4,4);
        Rectangle rectangleNotEqual1 = new Rectangle(1,3,2,4);
        Rectangle rectangleNotEqual2 = new Rectangle(1,3,2,5);

        assertEquals(rectangleEqual1.hashCode(), rectangleEqual2.hashCode());
        assertNotEquals(rectangleNotEqual1.hashCode(), rectangleNotEqual2.hashCode());

        try {
            Rectangle newRectangle = IntersectRectangles.intersect(rectangleEqual1,rectangleEqual2);
          } catch (IllegalArgumentException e) {
            System.out.println("Caught");
          }
    }

}
