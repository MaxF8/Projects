package edu.yu.cs.com1320.project.stage4.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
// import java.io.ByteArrayInputStream;
import java.util.function.Function;

import javax.print.Doc;

// import edu.yu.cs.com1320.project.Command;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;

public class DocumentStoreImpl implements DocumentStore
{

private GenericCommand<URI> commStck; //undoable GenericCommand<URI>
private Document docEntry;

private Stack<Undoable> stack;

private HashTable<URI,Document> hashTable; 
private TrieImpl<Document> trie;
private MinHeapImpl<Document> minHeap;
private int limitDocCount;
private int limitDocBytes; 
private boolean docCountBool = false;
private boolean docBytesBool = false;
private HashSet<Document> docSet;
private int currentDocBytes; 
private boolean zeroDocCount = false;
private boolean zeroBytesCount = false;
private long setSameTime;
    public DocumentStoreImpl()
    {
        this.minHeap = new MinHeapImpl<>();
        this.limitDocCount = 0;
        this.limitDocBytes = 0;
        this.trie = new TrieImpl<>();
        this.hashTable = new HashTableImpl<>(); 
        this.docEntry = null;
        this.stack = new StackImpl<>();   
        this.docSet = new HashSet<>();
    }


    //1. Clear from all areas (trie,hashtable)
    //2. UNDO
    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc. If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
    @throws IOException if there is an issue reading input
     * @throws IllegalArgumentException if uri or format are null
     */
   /*
        if put(new uri) -> delete uri -good v/
        if put(overwrite uri) -> put(uri, oldDoc) 
        if put(del uri) -> put(uri,doc) 
        /you must put whatever was deleted back into the DocumentStore exactly as it was before  
    */
    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException{  
        testFormatNull(uri,format);
        if (input == null){  //delete from trie, doc, hashtable, undo commands
            if (hashTable.get(uri) == null){
                return 0;}
            else{  //delete
                Document docToDelete = hashTable.get(uri);//If lambda is called, it will do what is inside
                lambdaPut(docToDelete,uri);
                deleteDocument(uri);
                // deleteFromTrie(docToDelete); // putInTrie(d);
                return docToDelete.hashCode();}}
        Document doc = null; 
        byte[] byteTxt = input.readAllBytes();
        doc = testFormat(format,  uri,  byteTxt,  doc);
        if (hashTable.get(uri) == null){ //first time putting in doc  
           if (testZero(doc) == 5){
               return 0;}
            setDocTime(doc);
            docSet.add(doc);
            limitTest();
            lambdaDelete(doc, uri);
            putHeapLogic(doc);
            putInHashTableAndTrie(uri, doc);
            this.docEntry = doc;
            return 0;
        }
        else{ //already a doc in table, overwrite
            Document oldDoc = hashTable.put(uri,doc);
            overwriteDocAllLogic(oldDoc,doc, uri);
        }
        return docEntry.hashCode();
    }
    private void overwriteDocAllLogic(Document oldDoc, Document doc, URI uri){
        deleteFromTrie(oldDoc);
        docSet.remove(oldDoc);
        lambdaPut(oldDoc, uri); 
        overwriteHeap(oldDoc, doc);
        docSet.add(doc);
        limitTest();
        putInTrie(doc);
    }
    private int testZero(Document doc) 
    {
        if (this.zeroDocCount == true)
        {
         if (this.docSet.size() == 0)
         {
            //  lambdaPut(doc, uri);
             return 5;
         }
        }
        if (this.zeroBytesCount == true)
        {
            if (doc.getDocumentBinaryData() == null) 
            {
              if (doc.getDocumentTxt().getBytes().length > 0)
              {
                  return 5;
              }
            }
            else if  (doc.getDocumentBinaryData() != null)
            {
               if (doc.getDocumentBinaryData().length > 0)
               {
                   return 5;
               }
            }
         }
         return 4;
    }
    private void limitTest() {
        if (docSet.size() > limitDocCount && docCountBool == true){ 
            while(docSet.size() > limitDocCount ){
            Document docu = minHeap.remove(); 
            docSet.remove(docu);
            // setOfDeletedDocs.add(docu);
            deleteFromHashTableAndTrie(docu.getKey(), docu);
            }
        }
        this.currentDocBytes = 0;
        for (Document d : docSet) {
           if (d.getDocumentBinaryData() == null) {
            this.currentDocBytes += d.getDocumentTxt().getBytes().length;
           }
           else if  (d.getDocumentBinaryData() != null){
              this.currentDocBytes += d.getDocumentBinaryData().length;
           }
        }
        if (currentDocBytes > limitDocBytes && docBytesBool == true) {
            while(currentDocBytes > limitDocBytes){
                Document d = minHeap.remove();
                // setOfDeletedDocs.add(d);
                deleteFromHashTableAndTrie(d.getKey(), d);
                if (d.getDocumentBinaryData() == null){ 
                    this.currentDocBytes -=d.getDocumentTxt().getBytes().length;
                }
                else if (d.getDocumentBinaryData() != null){
                    this.currentDocBytes -= d.getDocumentBinaryData().length;
                }
            }
        }
        // return setOfDeletedDocs;
    }
    private void overwriteHeap(Document oldDoc,Document doc) 
    {
        oldDoc.setLastUseTime(10);

        minHeap.reHeapify(oldDoc);
        minHeap.remove();
        setDocTime(doc);
        minHeap.insert(doc);
        minHeap.reHeapify(doc);

    }
    private void deleteDocFromHeap(Document doc)
    {
        doc.setLastUseTime(10);
        minHeap.reHeapify(doc); //this should upHeap

        minHeap.remove();
        docSet.remove(doc);

    }
    private void putHeapLogic(Document doc) 
    {
        minHeap.insert(doc);  
        minHeap.reHeapify(doc);  
        docSet.add(doc);


    }
   private void setDocTime(Document doc)
   {
    long newDocTime = System.nanoTime();
    doc.setLastUseTime(newDocTime);
   }
    
  /**
     * set maximum number of documents that may be stored
     * @param limit
     */
    public void setMaxDocumentCount(int limit)
    {
        if (limit < 0){
            throw new IllegalArgumentException();
        }
        this.limitDocCount = limit;
        this.docCountBool = true;
        if (limit == 0)
        {
            this.zeroDocCount = true;
        }
        if (this.docSet.size() > limit)
        {
            limitTest();
        }
        //if the documents are over limit, delete min, i.e. the first in order of it was put in 
    }

    /**
     * set maximum number of bytes of memory that may be used by all the documents in memory combined
     * @param limit
     */
    public void setMaxDocumentBytes(int limit)
    {
        if (limit < 0){
            throw new IllegalArgumentException();
        }
        this.limitDocBytes = limit;
        this.docBytesBool = true;
        if (limit == 0)
        {
            this.zeroBytesCount = true;
        }
        if (this.currentDocBytes > limit)
        {
            limitTest();
        }

    }
    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document getDocument(URI uri)
    {
        System.out.println("uri "+uri);
        
        Document doc = hashTable.get(uri);
        System.out.println(doc);
        if (doc != null) //TODO NULL DOC????
        {
        setDocTime(doc);
        minHeap.reHeapify(doc);
        }
        // System.out.println(doc.getLastUseTime());
        return doc;
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri)
    {
        Document docToDelete = hashTable.get(uri);

         if (docToDelete == null)
        {
           return false;
        }  
        deleteDocFromHeap(docToDelete);
        System.out.println(docToDelete);
        deleteFromHashTableAndTrie(uri, docToDelete); //null error? =!
        lambdaPut(docToDelete, uri);
        //set time to small number
        //remove
        //heapify 
        return true;
    }

    public void undo() throws IllegalStateException {
        if (stack == null)
        {
            throw new IllegalStateException(); 
        }
        Undoable commStckUndoable = stack.pop(); 
        
        if (commStckUndoable == null)
        {
            throw new IllegalStateException(); 
        }

        commStckUndoable.undo();

    }
    private void lambdaPut(Document d, URI uri)
    {
        // Document docToDelete = d;
        Function<URI, Boolean> lambda = (URI uriLambda) -> {   
            putInHashTableAndTrie(uri, d);
            setDocTime(d);
            minHeap.insert(d);
            minHeap.reHeapify(d);               
            docSet.add(d); //todo limitTest here?
           
            return true;
        };
    
        commStck = new GenericCommand<URI>(uri, lambda);
        stack.push((GenericCommand<URI>) commStck); //old: stack.push(commStck);   
    }
    private void lambdaDelete(Document d, URI uri)
    {
        // Document docToDelete = d;
        Function<URI, Boolean> lambda = (URI uriLambda) -> { 
            deleteFromHashTableAndTrie(uri, d);  
            deleteDocFromHeap(d);
            docSet.remove(d);
        
            return true;
        };
    
        commStck = new GenericCommand<URI>(uri, lambda);
        stack.push((GenericCommand<URI>) commStck); //old: stack.push(commStck);   
    }
    private void putInHashTableAndTrie(URI uri, Document doc)
    {
        hashTable.put(uri, doc);
        putInTrie(doc); 
    }
    private void deleteFromHashTableAndTrie(URI uri, Document doc)
    {
        // System.out.println(doc);
        hashTable.put(uri, null); //test
        deleteFromTrie(doc); 
    }
    private void putInTrie(Document d)
    {
        for (String s  : d.getWords()) 
        {   
            this.trie.put(s, d);            
        }
    }
    private void deleteFromTrie(Document docToDelete)
    {
        for (String s  : docToDelete.getWords()) { //delete from trie, working i think
                
            this.trie.delete(s, docToDelete);
        }
    }
  
    public void undo(URI uri) throws IllegalStateException {
        testIfStackNull(stack, uri);

        Stack<Undoable> helperStack = new StackImpl<>();
        int size = stack.size();
        genericOrCommandLogic(helperStack, size, uri);

        int helperSize = helperStack.size();   //put helperStack back in original stack
        for (int j = 0; j < helperSize; j++) 
        {
            Undoable newCom = helperStack.pop(); 
          
            stack.push(newCom);   
        }  
        if (helperSize == size)
        {
            throw new IllegalStateException();
        }
    }
    private void genericOrCommandLogic(Stack<Undoable> helperStack, int size,URI uri){
        for (int i = 0; i < size; i++){ //put everything in helperStack except specific uri
            Undoable undoable = stack.pop();
            if (undoable == null){
                throw new IllegalStateException(); 
            }
        if (undoable instanceof GenericCommand){
            GenericCommand<URI> genericCom =  (GenericCommand)undoable;

            if (genericCom.getTarget().equals(uri)){
                // System.out.println("equal uri: "+genericCom.getTarget());
                genericCom.undo();
                break;
            }
            else{
                helperStack.push(genericCom); }//com   
        }
        else if (undoable instanceof CommandSet){
            CommandSet<URI> commandSet = (CommandSet)undoable;
            if (commandSet.containsTarget(uri)) //==?;
            {     
                commandSet.undo(uri);
                if (!commandSet.isEmpty())
                    stack.push(commandSet); //testtt
                break;
            }
            else{
                helperStack.push(commandSet); }//com error??? 
        }
    }
    }
    private void testIfStackNull(Stack<Undoable> stack, URI uri)
    {
        if (stack == null)
        {
            throw new IllegalStateException(); 
        }
        if (uri == null)
        {
            throw new IllegalStateException();
        }
    }
    private void testFormatNull(URI uri, DocumentFormat format)
    {
        if (format == null)
        {
            throw new IllegalArgumentException();
        } 
      
        if (uri == null)
        {
            throw new IllegalArgumentException();
        }
    }
    private Document testFormat(DocumentFormat format, URI uri, byte[] byteTxt, Document doc)
    {
        if (format == DocumentFormat.TXT){
            String str = new String(byteTxt);  
            doc = new DocumentImpl(uri,str);
            return doc;
        }
        else if (format == DocumentFormat.BINARY){
            // System.out.println("byte");
            doc = new DocumentImpl(uri,byteTxt);
            return doc;
        }   
        return doc;
    }
   

    class DocComparator implements Comparator<Document>
    {
        private String word;

        public DocComparator(String word, Boolean bool)
        {
            this.word = word;
        }

        @Override
        public int compare(Document Doc1, Document Doc2) {
             
                if (Doc1.wordCount(word) < Doc2.wordCount(word))
                {
                    return 1;
                }
                if (Doc1.wordCount(word) > Doc2.wordCount(word))
                {
                    return -1;
                }
                return 0;
            }
    }
    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    //keyword "the"
    //1 doc has 4 "the" (this goes before the second one, first in list)
    // 1 doc has 3 "the"
    
    public List<Document> search(String keyword)
    {   
        // if(keyword.length() == 0)
        keyword = fixCase(keyword);
        // keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "");
        // keyword = keyword.toLowerCase();

        DocComparator docComp = new DocComparator(keyword, true);

        //call trie
        List<Document> docList = new ArrayList<>();
        
        docList = trie.getAllSorted(keyword,docComp); //make sure this also returns empty list
        
        for (Document doc : docList) {
            setDocTime(doc);
            minHeap.reHeapify(doc);
        }
        return docList;
    }

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> searchByPrefix(String keywordPrefix)
    {

        keywordPrefix = fixCase(keywordPrefix);

        // keywordPrefix = keywordPrefix.replaceAll("[^a-zA-Z0-9\\s]", "");
        // keywordPrefix = keywordPrefix.toLowerCase();

        DocComparator docComp = new DocComparator(keywordPrefix, false);

        List<Document> docList = new ArrayList<>();

        docList = trie.getAllWithPrefixSorted(keywordPrefix, docComp);
        for (Document doc : docList) {
            setDocTime(doc);
            minHeap.reHeapify(doc);
        }
        return docList;
    }

    /**
     * Completely remove any trace of any document which contains the given keyword
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAll(String keyword)
    {
        keyword = fixCase(keyword);
        Set<Document> valueSet = new HashSet<>();
        Set<URI> uriSet = new HashSet<>();
        valueSet = trie.deleteAll(keyword); 
        CommandSet<URI> commStckSet = new CommandSet<>();
        
        uriSet = commandSetLogic(valueSet, uriSet, commStckSet);
        int i = 0;
        for (Document doc : valueSet) 
        {
            i++;
            if (i == 1)
            {
                setDocTime(doc);
                this.setSameTime = doc.getLastUseTime();
                // setDocTime(doc);
            }
          //setUseTime(this.setSameTime);
            deleteDocFromHeap(doc);
        }
        return uriSet;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String keywordPrefix)
    {
        keywordPrefix = fixCase(keywordPrefix);
        // keywordPrefix = keywordPrefix.replaceAll("[^a-zA-Z0-9\\s]", "");
        // keywordPrefix = keywordPrefix.toLowerCase();
       
        // DocComparator docComp = new DocComparator(keywordPrefix, false);

        Set<Document> valueSet = new HashSet<>();
        Set<URI> uriSet = new HashSet<>();

        valueSet = trie.deleteAllWithPrefix(keywordPrefix); //values vs docs???
        
        CommandSet<URI> commStckSet = new CommandSet<>();
        uriSet = commandSetLogic(valueSet, uriSet, commStckSet);
        int i = 0;
        for (Document doc : valueSet) 
        {
            System.out.println(doc.getKey());
            i++;
            if (i == 1)
            {
                setDocTime(doc);
                this.setSameTime = doc.getLastUseTime();

                // setDocTime(doc);
            }
          //setUseTime(this.setSameTime);
            deleteDocFromHeap(doc);
        }
        return uriSet;
    }
    private Set<URI> commandSetLogic(Set<Document> valueSet,Set<URI> uriSet, CommandSet<URI> commStckSet)
    {
        for (Document document : valueSet) { //commandset???
            URI uri = document.getKey();
            uriSet.add(uri);
            deleteFromTrie(document);
            this.hashTable.put(uri, null); //delete from hashtable
            
            Function<URI, Boolean> lambda = (URI uriLambda) -> { 
            
                hashTable.put(uriLambda, document);
                putInTrie(document); 
                document.setLastUseTime(this.setSameTime);
                putHeapLogic(document);
                return true;
            };
            commStck = new GenericCommand<URI>(uri, lambda);

            commStckSet.addCommand((GenericCommand<URI>) commStck);   
            // deleteDocument(uriToDelete);
        }
        stack.push((CommandSet<URI>)commStckSet);
        return uriSet;
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
