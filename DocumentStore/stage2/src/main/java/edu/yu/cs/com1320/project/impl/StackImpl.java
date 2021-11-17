package edu.yu.cs.com1320.project.impl;

import java.util.LinkedList;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.Stack;
/**
 * StackImpl
 * @param <T>
 */

 
public class StackImpl<T> implements Stack<T>  {


// private T element1;
    private  LinkedList<T> list;
    // private Command command;
        
    public StackImpl()
    {
        this.list = new LinkedList<T>();
    }
    /**
     * @param element object to add to the Stack
     */
    public void push(T element)
    {
        list.addFirst(element);
    }

    /**
     * removes and returns element at the top of the stack
     * @return element at the top of the stack, null if the stack is empty
     */
    public T pop()
    {
        if (list.size() == 0)
        {    
            return null;
        }

        return(list.removeFirst()); 
        
        
        //wrong
        //when you add something to a list you add it to the end (same as adding to top of a stack)
        
    }

    /**
     *
     * @return the element at the top of the stack without removing it
     */
    public T peek()
    {
        return(list.element());
    }

    /**
     *
     * @return how many elements are currently in the stack
     */
    public int size()
    {
        return list.size();
    }
    
}