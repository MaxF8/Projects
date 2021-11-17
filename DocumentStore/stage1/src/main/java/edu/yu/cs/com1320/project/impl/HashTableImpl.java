package edu.yu.cs.com1320.project.impl;

import java.util.Arrays;

import edu.yu.cs.com1320.project.HashTable;


public class HashTableImpl<Key,Value> implements HashTable<Key, Value> {
  
  class Entry<Key,Value>
  {
    private Key key;
    private Value value;
    private Entry<Key,Value> next;

    
    Entry(Key k, Value v)
    {
      this.key = k;
      this.value = v;
      this.next = null;
    }

    private Entry<Key, Value> getEntry() 
    {
      return next;
    }
    private void setEntry(Entry<Key, Value> entry) 
    //store entry 2 in entry 1
    {
      this.next = entry;
    }
    
    private Value getValue() 
    {
      if (this.value == null)
      {
        return null;
      }
      return this.value;
    }

    private void setValue(Value value) 
    {
      
      this.value = value;
    }

    private Key getKey()
    {
      if (this.key == null)
      {
        return null;
      }
      return this.key;
    }
  }

  //  private Key key;
  //  private Value value; 
   private Entry<Key,Value>[] entryArray;
   private Entry<Key,Value>[] doublingArray;

   private int counter;
  //  private Entry<Key,Value> testEntry; 

   public static void main(String[] args) {
     int[] arr = {1,2,3};
     int[] secondArr = new int[6];
     for (int i = 0; i < arr.length; i++) 
     {
        secondArr[i] = arr[i]; 
     }
     System.out.println(Arrays.toString(secondArr));
   }
//hashtable is an array of linked lists
    public HashTableImpl()
    {  
      this.entryArray = new Entry[5]; 
      this.counter = 0;
      // System.out.println(getCounter());

    }

/**
 * Instances of HashTable should be constructed with two type parameters, one for the type of the keys in the table and one for the type of the values
 * @param <Key>
 * @param <Value>
 */

    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */
  public Value get(Key k)
  {
    int index = hashFunction(k);
//if array is null at index (IT WILL ONLY BE NULL IF NOTHING HAS BEEN PUT THERE)
    if (entryArray[index] == null)
      return null;
    else 
    {
      //entry is equal to whatever is there at index 
        Entry<Key,Value> entryHolder = entryArray[index];
        //while entry doesn't equal null and entry's key does not equal k 
        while (entryHolder != null && !entryHolder.key.equals(k))
        {
          entryHolder = entryHolder.next;
        } 
        // case when entry is equal to null
        if (entryHolder == null)
        {
          return null;
        }
        else
        {
          Value entryHolderVal = entryHolder.value;
          return entryHolderVal;
        }
    }
  }


    /**
     * @param k the key at which to store the value
     * @param v the value to store
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */
  public Value put(Key k, Value v) 
    {
      System.out.println(entryArray.length);
      int index = hashFunction(k);
      System.out.println(getCounter());
      // if (counter ==  ) //TODO change to 75%
      // {
      //   // int hi = entryArray.length * 2;
      //   doublingArray = new Entry[entryArray.length*2]; 
      //   System.out.println(doublingArray.length);

      //    for (int i = 0; i < this.entryArray.length; i++) 
      //    {
      //     // System.out.println(i);
      //     doublingArray[i] = entryArray[i];
      //    }

      // }
      //re-hash all your entries after doubling the array

      //first go around, when their is nothing in array yet
      if (entryArray[index] == null)
      {
        entryArray[index] = new Entry<Key,Value>(k, v);
        this.counter++;
      } 
      //When there is already something at array index
      else 
      {
        Entry<Key,Value> entryHolder = entryArray[index];
      //this is for when the key is a different number
      while (entryHolder.next != null && !entryHolder.key.equals(k))
      {
        entryHolder = entryHolder.next;
      }
      //if the entry has the correct key, put in the value
      if (entryHolder.key.equals(k))
      {
        Value oldVal = entryHolder.value;
        entryHolder.setValue(v);
        this.counter++;
        return oldVal;
      }
      else
      {
        entryHolder.next = new Entry<Key,Value>(k,v); 
        return null;
      }
    }

    return v; 
    }

    public int getCounter()
    {
      System.out.print("Counter is: ");
      return this.counter;
    }


    private int hashFunction(Key key)
    {
      return (key.hashCode() & 0x7fffffff) % this.entryArray.length;
    }
}