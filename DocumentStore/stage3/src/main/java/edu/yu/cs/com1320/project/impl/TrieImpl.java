package edu.yu.cs.com1320.project.impl;

import java.util.*;

import edu.yu.cs.com1320.project.Trie;
// TrieImpl must use a java.util.Comparator<Document> 
// to sort collections of documents by how many times a given
//  word appears in them, when implementing Trie.getAllSorted and 
//  any other Trie methods that return a sorted collection.
import edu.yu.cs.com1320.project.stage3.Document;


public class TrieImpl<Value> implements Trie<Value>{
    private static final int alphabetSize = 256; // extended ASCII

    private Node<Value> root; // root of trie

    public static class Node<Value>
    {
        protected List<Value> valueList = new ArrayList<>();
        protected Node[] links = new Node[TrieImpl.alphabetSize];
    }

    public TrieImpl()
    {   
        this.root = new Node<Value>();
    }

/**
     * add the given value at the given key
     * @param key
     * @param val
     */
    //  public static void main(String[] args) {
    //      Trie trie = new TrieImpl<Integer>();
    //      trie.put("abc",1);
    //     //  trie.put("abc",2);
    //     //  trie.delete("abc", 2);
    //      System.out.println("");
    // }
    public void put(String key, Value val)
    {
        //val = doc1
        key = fixCase(key);
        // System.out.println(key);
        if (val == null)
        {
            // System.out.println("value was null");
            return;
        }
        else
        {
            this.root = put(this.root, key, val,0);
        }
    }
    private Node<Value> put(Node<Value> x,String key, Value val, int d){
        if (x == null)
        {
            x = new Node<Value>();
            // System.out.println("null");
            
        }
        if (d == key.length())
        {
            // System.out.println("put");
            if (!x.valueList.contains(val))
            {
             
                x.valueList.add(val);
            }
           
            return x;
        }
            //else, proceed to the next node in the chain of nodes that
            //forms the desired key
        char c = key.charAt(d);
        // System.out.println(c);
        x.links[c] = this.put(x.links[c], key, val, d + 1);
        return x;         
    }

    /**
     * get all exact matches for the given key, sorted in descending order.
     * Search is CASE INSENSITIVE.
     * @param key
     * @param comparator used to sort values
     * @return a List of matching Values, in descending order
     */
    public List<Value> getAllSorted(String key, Comparator<Value> comparator){  
        if (comparator == null)
        {
            throw new IllegalArgumentException();
        }
        key = fixCase(key);
        if (key.length()==0)
        {
            return new ArrayList<Value>();
        }

        // key = key.replaceAll("[^a-zA-Z0-9\\s]", "");
        // key = key.toLowerCase();  
        Node<Value> x = this.get(this.root, key, 0);
        if (x == null)
        {     
            return new ArrayList<Value>();
        }
        if (x.valueList == null)
        {
            return new ArrayList<Value>();
        }
        Collections.sort(x.valueList,comparator);
      
        return x.valueList;
    }
    private Node<Value> get(Node<Value> x, String key, int d)
    {
        //link was null - return null, indicating a miss
        // key = fixCase(key);
        if (x == null)
        {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length())
        {
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(x.links[c], key, d + 1);
    }
    /**
     * get all matches which contain a String with the given prefix, sorted in descending order.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order
     */
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator){
        prefix = fixCase(prefix);
        if (comparator == null)
        {
            throw new IllegalArgumentException();
        }
        if (prefix.length()==0)
        {
            return new ArrayList<Value>();
        }
        // prefix = prefix.replaceAll("[^a-zA-Z0-9\\s]", "");
        // prefix =prefix.toLowerCase();  
        Node<Value> x = this.get(this.root, prefix, 0);
        if (x == null)
        {
            return new ArrayList<Value>();
        }
        Set<Value> set = collect(x, new HashSet<Value>());
        if (set == null)
        {
            return new ArrayList<Value>();
        }
        ArrayList<Value> list = new ArrayList<Value>(set);
        list.sort(comparator);
        return list;
    }
    private Set<Value> collect(Node<Value> x, Set<Value> set){
    if (x.valueList != null) 
    {
        set.addAll(x.valueList);
    }
        //visit each non-null child/link
    for (char c = 0; c < TrieImpl.alphabetSize; c++) 
    { 
        if(x.links[c] != null){
        
        this.collect(x.links[c], set);
        //remove the child's char to prepare for next iteration
        }
    }
        return set;
    }
    /**
     * Delete the subtree rooted at the last character of the prefix.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @return a Set of all Values that were deleted.
     */
//  1.  You must delete all references to it 
// within all parts of the Trie.

// 2. If the Document being removed is that last one at that
//  node in the Trie, you must delete it and all ancestors
//  between it and the closest ancestor that has at least 
// one document in its Value collection.


    public Set<Value> deleteAllWithPrefix(String prefix)
    {
        prefix = fixCase(prefix);

        if (prefix.length()==0)
        {
            return new HashSet<Value>();
        }
        // prefix = prefix.replaceAll("[^a-zA-Z0-9\\s]", "");
        // prefix = prefix.toLowerCase(); 
         //call collect gives me the set to return
        Node<Value> x = this.get(this.root, prefix, 0);
        Set<Value> set = collect(x, new HashSet<Value>());

        //chop off children
        x.links = new Node[TrieImpl.alphabetSize]; //links are children

        deleteAll(prefix);
        return set;
        // delete all
    }

    /**
     * Delete all values from the node of the given key (do not remove the values from other nodes in the Trie)
     * @param key
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAll(String key){
        key = fixCase(key);
        
        if (key.length()==0)
        {
            return new HashSet<Value>();
        }
        Node<Value> x = this.get(this.root, key, 0);
        if (x == null)
        {
        //    System.out.println("x is null");
        }
        Set<Value> set = collect(x, new HashSet<Value>());
 
        this.root = deleteAll(this.root, key, 0);

        // Set<Value> set = collect(this.root, new HashSet<Value>());
        return set;
    }

    private Node<Value> deleteAll(Node<Value> x, String key, int d){
        if (x == null)
        {
            return null;
        }
        if (d == key.length())
        {
            x.valueList = null;
        }
        else
        {
            char c = key.charAt(d);
            x.links[c] = this.deleteAll(x.links[c], key, d+1);
        }
        if (x.valueList != null)
        {
            return x;
        }
        //remove subtrie rooted at x if it is completely empty	
        for (int c = 0; c <TrieImpl.alphabetSize; c++)
        {
            if (x.links[c] != null)
            {
                return x; //not empty
            }
        }
        //empty - set this link to null in the parent
        return null;
    }

    /**
     * Remove the given value from the node of the given key (do not remove the value from other nodes in the Trie)
     * @param key
     * @param val
     * @return the value which was deleted. If the key
     *  did not contain the given value, return null.
     */
    public Value delete(String key, Value val){
        // if (key == null) //needed?
        // {
        //     throw new IllegalArgumentException();
        // }
        key = fixCase(key);

        if (key.length()==0)
        {
            return null;
        }
        if (val == null) //needed?
        {
            throw new IllegalArgumentException();
        }
        Value returnedVal = null;
        // key = key.replaceAll("[^a-zA-Z0-9\\s]", "");
        // key = key.toLowerCase(); 
        Node<Value> x = this.get(this.root, key, 0);
        if (x != null) //todo ??? 
        {
            for (Value v : x.valueList) 
            {
                if (v.equals(val))
                {
                    returnedVal = v;
                    break;
                } 
            }
        }
        //delete if null, (apple 1 would delete all apple unless one of the other letters  isn't null)
        delete(this.root, key,val, 0); //this.root?
        return returnedVal;
    }

    private Node<Value> delete(Node<Value> x, String key, Value val, int d){
        if (x == null) {
            return null;
        }
            //we're at the node to delete - set the val to null
        if (d == key.length()){
            if (x.valueList.contains(val))
            {
                x.valueList.remove(val);
            }
        }
        else //continue down the trie to the target node
        {
        char c = key.charAt(d);
        x.links[c] = this.delete(x.links[c], key, val, d + 1);
        }
        //this node has a val – do nothing, return the node
        if (x.valueList != null) {
            return x;
        }
        //otherwise, check if subtrie rooted at x is completely empty
        for (int c = 0; c <TrieImpl.alphabetSize; c++){
            if (x.links[c] != null)
            {
                return x; //not empty
            }
        }
        return null; //empty - set this link to null in the parent
    }

   private String fixCase(String str)
   {
        if (str == null)
        {
            throw new IllegalArgumentException();
        }
        str = str.replaceAll("[^a-zA-Z0-9\\s]", "");
        str = str.toLowerCase(); 
        return str;
   }
}
