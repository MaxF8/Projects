package edu.yu.cs.com1320.project.impl;


import java.util.NoSuchElementException;

import edu.yu.cs.com1320.project.MinHeap;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {


    public MinHeapImpl()
    {
        this.elements = (E[]) new Comparable[5];
        
    }
    @Override
    public void reHeapify(Comparable element) {
    
        int i = getArrayIndex(element);
        // System.out.println(i);
        upHeap(i);
        
        downHeap(i);
        
    }
   

    //      1  2 3 - 4 5 -

        // after you put,get, etc, you
        // set time
        // reheapify 
    

    @Override
    protected int getArrayIndex(Comparable element) 
    {
        // for (int i = 0; i < elements.length; i++) {
        //     System.out.println(i);
        // }
        for (int i = 0; i < elements.length; i++) {
            if (element.equals(elements[i]))
            {
            // if (element != null)
                // System.out.println(element);
                
            return i;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    protected void doubleArraySize() 
    {
        E elementsCopy[] = (E[]) new Comparable[elements.length*2];
        
        for (int i = 0; i < elements.length; i++) 
        {
            elementsCopy[i] = elements[i];    
        }
        elements = elementsCopy.clone();
        System.out.println("arrayDoubled^");

    }
    
}
