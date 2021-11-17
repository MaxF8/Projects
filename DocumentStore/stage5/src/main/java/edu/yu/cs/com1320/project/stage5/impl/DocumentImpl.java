package edu.yu.cs.com1320.project.stage5.impl;

import java.net.URI;
// import java.net.URISyntaxException;
import java.util.*;

import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;

public class DocumentImpl implements Document  {

    private String documentText;
    private byte[] documentBinaryData;
    private URI key;
    private transient Set<String> stringSet;
    private Map<String,Integer> wordMap;
         
    private transient long docTime = System.nanoTime();
    private transient int bytesLength = 0;     
    public DocumentImpl(URI uri, String txt) {
        testNullStringDoc(uri, txt);
        // System.out.println(docTime);
        this.documentText = txt;
        this.key = uri;
        this.stringSet = new HashSet<String>();
        this.wordMap = new HashMap<String,Integer>();
        byte binaryData[] =  txt.getBytes();
        this.bytesLength = binaryData.length;
        // System.out.println(bytesLength);
        txt = txt.trim();
        String[] arrStr = txt.split("\\s+"); 
    
        for (String str: arrStr)
        {
        int counter = 0; 
            str = fixCase(str);
            for (String strComparison : arrStr) {
                strComparison = fixCase(strComparison);
                if (str.equals(strComparison)){
                    counter += 1;
                }
            }
            
            wordMap.put(str,counter);
        }  
        // System.out.println(wordMap);
        
    }

    public DocumentImpl(URI uri, byte[] binaryData) 
    {

        if (uri == null || binaryData == null || binaryData.length == 0 )
        {
            throw new IllegalArgumentException();
        }
       
        String uriTest = uri.toString();

        if (uriTest.length() == 0)
        {
            throw new IllegalArgumentException(); 
        }

        this.documentBinaryData = binaryData;
        this.key = uri;
        this.wordMap = new HashMap<String,Integer>();
        this.bytesLength = binaryData.length;

    }

     /**
     * @return content of text document
     */
    public String getDocumentTxt()
    {
        return documentText;
    }

    /**
     * @return content of binary data document
     */
    public byte[] getDocumentBinaryData()
    {
        return documentBinaryData;
    }

    /**
     * @return URI which uniquely identifies this document
     */
    public URI getKey()
    {
        return key;
    }
    @Override 
    public boolean equals(Object obj) 
    {
        if (obj != null)
        {

        if (this.hashCode() == obj.hashCode())
        {
            return true;
        }
    }
        return false;
    }
    @Override
public int hashCode() {
    int result = key.hashCode();
    result = 31 * result + (documentText != null ? documentText.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(documentBinaryData);
    return result;
}

  /**
     * how many times does the given word appear in the document?
     * @param word
     * @return the number of times the given words appears in the document. If it's a binary document, return 0.
     */
    public int wordCount(String word)
    {
        if (this.documentBinaryData != null)
        {
            // System.out.println("binary");
            return 0;
        }
        word = fixCase(word);
        int wordCounter = 0;

        Set<String> set = this.getWords();
        for (String string : set){ //test null?
            if (string.length() >= word.length()){                
                if (string.startsWith(word)){
                    wordCounter += wordMap.get(string);
                }
            }           
        }
        return wordCounter;
    }

    /**
     * @return all the words that appear in the document
     */
    public Set<String> getWords()
    {
        return this.wordMap.keySet(); 
    }
    
    /**
     * return the last time this document was used, via put/get or via a search result
     * (for stage 4 of project)
     */
    public long getLastUseTime()
    {
        return docTime;

    }
    public void setLastUseTime(long timeInNanoseconds)
    {  
        docTime = timeInNanoseconds; 
    }

    @Override
    public int compareTo(Document o) {
        // System.out.println("obj1 "+this.getLastUseTime());
        // System.out.println("obj2 "+o.getLastUseTime());
        // System.out.println("this, time:"+this.getLastUseTime());
        // System.out.println("object, time:"+o.getLastUseTime());

        if (this.getLastUseTime() > o.getLastUseTime())
        {
            return 1;
        }
        else if (this.getLastUseTime() < o.getLastUseTime())
        {
            return -1;
        }  
        return 0;
    }
    private String fixCase(String str)
    {
         testStringNull(str);
         str = str.replaceAll("[^a-zA-Z0-9\\s]", "");
         str = str.toLowerCase(); 
         return str;
    }
    private void testStringNull(String str) {
        if (str == null)
        {
            throw new IllegalArgumentException();
        } 
    }
    private void  testNullStringDoc(URI uri, String txt)
    {
        if (uri == null || txt == null || txt.length() == 0)
        {
            throw new IllegalArgumentException();
        }
        String uriTest = uri.toString();

        if (uriTest.length() == 0)
        {
            throw new IllegalArgumentException(); 
        }
    }

    public Map<String, Integer> getWordMap() {
        
        return wordMap;
    }

    public void setWordMap(Map<String, Integer> wordMap) {
        this.wordMap = wordMap;
        
    }

   

   
}
   
