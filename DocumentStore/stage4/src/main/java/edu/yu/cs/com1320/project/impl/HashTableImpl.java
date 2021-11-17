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
   private int total;
   private int length;

  //  private Entry<Key,Value> testEntry; 

//hashtable is an array of linked lists
    public HashTableImpl()
    {  
      this.entryArray = new Entry[5];
      this.counter = 0;
      this.total = 0;
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
    if (k == null)
    {
      throw new IllegalArgumentException();
    }
    int index = hashFunction(k);
//if array is null at index (IT WILL ONLY BE NULL IF NOTHING HAS BEEN PUT THERE)
    if (entryArray[index] == null)
    {
      return null;
    } 
    else 
    {
      // System.out.println(k);
      //entry is equal to whatever is there at index 
        Entry<Key,Value> entryHolder = entryArray[index];
        //while entry doesn't equal null and entry's key does not equal k 
        while (entryHolder != null && !entryHolder.key.equals(k))
        {
          entryHolder = entryHolder.next;
        } 
        // case when entry is equal to null
        return (ifElseValue(entryHolder));  
    }
  }

    private Value ifElseValue(Entry<Key,Value> entryHolder)
    {
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
    /**
     * @param k the key at which to store the value
     * @param v the value to store
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */
  public Value put(Key k, Value v) {
    if (k == null){
      throw new IllegalArgumentException();
    }
      int index = hashFunction(k);
      if (entryArray[index] == null){ 
        //first go around, when their is nothing in array yet
        entryArray[index] = new Entry<Key,Value>(k, v);
      } 
      else {//When there is already something at array index
        Entry<Key,Value> entryHolder = entryArray[index];
      while (entryHolder.next != null && !entryHolder.key.equals(k)){ //this is for when the key is a different number
        entryHolder = entryHolder.next;
      }
      //if the entry has the correct key, put in the value
      if (entryHolder.key.equals(k)){
        Value oldVal = entryHolder.value;
        entryHolder.setValue(v);
        // this.counter++;
          arrayDouble(k,v);
        return oldVal;
      }
      else{
        entryHolder.next = new Entry<Key,Value>(k,v); 
          arrayDouble(k,v);
        return null;
      }
    }
    arrayDouble(k,v);
    return null; //v?
    }


    private void arrayDouble(Key k, Value v)
    {
      // System.out.println("count: "+getCounter());
      counter++;
      
      if (counter > entryArray.length*.75) 
      {
    Entry<Key,Value>[] tempArray = entryArray;
    doublingArray = new Entry[tempArray.length*2]; 
    
      entryArray = doublingArray;
      counter = 0;
      for (int i = 0; i < tempArray.length; i++) 
      { //temp is original array

        Entry<Key,Value> entryHead = tempArray[i];
        while(entryHead != null)
        {
          put(entryHead.getKey(), entryHead.getValue());
          entryHead = entryHead.next;
        }
      }
    }
    }


    private int hashFunction(Key key)
    {
      return (key.hashCode() & 0x7fffffff) % this.entryArray.length;
    }
}