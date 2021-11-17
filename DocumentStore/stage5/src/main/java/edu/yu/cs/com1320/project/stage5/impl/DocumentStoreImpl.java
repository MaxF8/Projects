package edu.yu.cs.com1320.project.stage5.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
// import java.io.ByteArrayInputStream;
import java.util.function.Function;

import edu.yu.cs.com1320.project.BTree;

// import edu.yu.cs.com1320.project.Command;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;

public class DocumentStoreImpl implements DocumentStore
{

   
    
private GenericCommand<URI> commStck; //undoable GenericCommand<URI>
private Document docEntry;

private Stack<Undoable> stack;

private BTree<URI,Document> bTree; 
private TrieImpl<URI> trie;
private MinHeapImpl<DocReplacement> minHeap;
private int limitDocCount;
private int limitDocBytes; 
private boolean docCountBool = false;
private boolean docBytesBool = false;
private HashSet<Document> docSet; //Document
private int currentDocBytes; 
private boolean zeroDocCount = false;
private boolean zeroBytesCount = false;
private long setSameTime;
private HashSet<DocReplacement> replacementDocSet;
private HashSet<DocReplacement> movedToDiskSet;


    class DocReplacement implements Comparable<DocReplacement> 
    {
        private URI replacementUri;
        private long replacementLastUseTime;
        public DocReplacement(Document d)
        {
            // System.out.println("time: "+d.getLastUseTime());
            this.replacementLastUseTime = d.getLastUseTime();
            this.replacementUri = d.getKey();
        }
        // @Override
        private long getTime()
        {
            return this.replacementLastUseTime;
        }
        private void setTime(long newTime)
        {
            // System.out.println(newTime);
            this.replacementLastUseTime = newTime;
        }
        private URI getURI()
        {
            return this.replacementUri;
        }

        @Override
        public int compareTo(DocReplacement object) 
        {
            // System.out.println(this.getTime());
            // if (object != null)
            // {
          
            // System.out.println(this.getTime());
            // System.out.println(object.getTime());

            // System.out.println(">>>>"+bTree.get(this.getURI()).getLastUseTime());
            // System.out.println(">>>>"+bTree.get(object.getURI()).getLastUseTime());
            // System.out.println("1st uri: "+this.getURI());
            // System.out.println("2nd uri: "+object.getURI());
    
            // return bTree.get(this.getURI()).compareTo(bTree.get(object.getURI()));
        
            if (this.getTime() > object.getTime())
            {
                return 1;
            }
            else if (this.getTime() < object.getTime())
            {
                return -1;
            }  
            return 0;
        }
        @Override
        public boolean equals(Object object) 
        {
            if (object != null && this != null)
            {
            // System.out.println(object);
            if (this.getURI() == ((DocReplacement) object).getURI())
            {
            // System.out.println("> "+this.getTime());
            // System.out.println("> "+((DocReplacement) object).getTime());

                return true;
            }
            }
            return false;
        }

    //     if (bTree.get(this.getURI()).getLastUseTime() > ((DocReplacement) o).getTime())
    //     {
    //         return 1;
    //     }
    //     else if (this.getTime() < ((DocReplacement) o).getTime())
    //     {
    //         return -1;
    //     }  
    //     return 0;
        
        
      
      
    }
    public DocumentStoreImpl()
    {
        DocumentPersistenceManager docPM = new DocumentPersistenceManager(null);
        this.bTree = new BTreeImpl<>(); 
        this.bTree.setPersistenceManager(docPM);
        this.minHeap = new MinHeapImpl<>();
        this.limitDocCount = 0;
        this.limitDocBytes = 0;
        this.trie = new TrieImpl<>();
        this.docEntry = null;
        this.stack = new StackImpl<>();   
        this.docSet = new HashSet<>();
        this.replacementDocSet = new HashSet<>();
        this.movedToDiskSet = new HashSet<>();

    }
    public DocumentStoreImpl(File baseDir) //todo
    {
        DocumentPersistenceManager docPM = new DocumentPersistenceManager(baseDir); 
        this.bTree = new BTreeImpl<>(); 
        this.bTree.setPersistenceManager(docPM);
        this.minHeap = new MinHeapImpl<>();
        this.limitDocCount = 0;
        this.limitDocBytes = 0;
        this.trie = new TrieImpl<>();
        this.docEntry = null;
        this.stack = new StackImpl<>(); 
        this.docSet = new HashSet<>();
        this.replacementDocSet = new HashSet<>();
        this.movedToDiskSet = new HashSet<>();


    }

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
        if (input == null){  //delete from trie, doc, bTree, undo commands
            if (bTree.get(uri) == null){
                return 0;}
            else{  //delete
                Document docToDelete = bTree.get(uri);//If lambda is called, it will do what is inside
                lambdaPut(docToDelete,uri);
                deleteDocument(uri);
                // deleteFromTrie(docToDelete); // putInTrie(d);
                return docToDelete.hashCode();}}
        Document doc = null; 
        byte[] byteTxt = input.readAllBytes();
        doc = testFormat(format,  uri,  byteTxt,  doc);
        if (bTree.get(uri) == null){ //first time putting in doc  
           if (testZero(doc) == 5){
            return 0;}
            setDocTime(doc);
            docSet.add(doc);
            limitTest();
            lambdaDelete(doc, uri);
            putHeapLogic(doc);
            putInbTreeAndTrie(uri, doc);
            this.docEntry = doc;
            // System.out.println(docSet.size());
            return 0;
        }
        else{ //already a doc in table, overwrite
            System.out.println("overwrite");
            Document oldDoc = bTree.put(uri,doc);
            // System.out.println(oldDoc);
            overwriteDocAllLogic(oldDoc,doc, uri);}
        return docEntry.hashCode();}
        
    private DocReplacement getReplacementDoc(Document doc) 
    {
        for (DocReplacement docR : this.replacementDocSet) {
            if (doc.getKey() == docR.getURI())
            {
                docR.setTime(doc.getLastUseTime());
                // minHeap.reHeapify(docR);
                return docR;
            }
        }
        return null;
    }
    private void overwriteDocAllLogic(Document oldDoc, Document doc, URI uri){
        // System.out.println("old "+oldDoc);
        // System.out.println("new "+doc);
        // System.out.println(docSet.size());

        deleteFromTrie(oldDoc);
        docSet.remove(oldDoc);
        for (Document document : docSet) {
            // System.out.println(">"+document);
        }
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
             return 5; //arbitrary number
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
        if (docSet.size() > limitDocCount && docCountBool == true){  //docSize test
            while(docSet.size() > limitDocCount ){
            DocReplacement docReplacement = minHeap.remove(); //Document
            moveUriToDisk(docReplacement.getURI());
            replacementDocSet.remove(docReplacement);
            docSet.remove(bTree.get(docReplacement.getURI()));
            // setOfDeletedDocs.add(docu);
            deleteFrombTreeAndTrie(docReplacement.getURI(), bTree.get(docReplacement.getURI()));
            }
        }
        this.currentDocBytes = 0;
        for (Document d : docSet) {
            // System.out.println(d);
           if (d.getDocumentBinaryData() == null) {
            //    System.out.println(d.getDocumentTxt());
            this.currentDocBytes += d.getDocumentTxt().getBytes().length;
           }
           else if  (d.getDocumentBinaryData() != null){
              this.currentDocBytes += d.getDocumentBinaryData().length;
           }
        }
        if (currentDocBytes > limitDocBytes && docBytesBool == true) { //docBytes test
            while(currentDocBytes > limitDocBytes){
                DocReplacement docReplacement = minHeap.remove(); //Document
                Document document = bTree.get(docReplacement.getURI());
                moveUriToDisk(document.getKey());
                replacementDocSet.remove(docReplacement);
                docSet.remove(document);
                // setOfDeletedDocs.add(d);
                deleteFrombTreeAndTrie(document.getKey(), document);
                if (document.getDocumentBinaryData() == null){ 
                    this.currentDocBytes -=document.getDocumentTxt().getBytes().length;
                }
                else if (document.getDocumentBinaryData() != null){
                    this.currentDocBytes -= document.getDocumentBinaryData().length;
                }
                // System.out.println("current "+currentDocBytes+" limit "+limitDocBytes);
            }
        }
        // return setOfDeletedDocs;
    }
    private void overwriteHeap(Document oldDoc,Document doc) 
    {   
        
        System.out.println("overwrite heap");
       
        oldDoc.setLastUseTime(10);
        
        DocReplacement oldDocReplacement = getReplacementDoc(oldDoc);
       

        minHeap.reHeapify(oldDocReplacement);

        minHeap.remove();
        // System.out.println("docR "+docR.getURI());
        setDocTime(doc);
        DocReplacement docReplacement = getReplacementDoc(doc);
       
        minHeap.insert(docReplacement);
        minHeap.reHeapify(docReplacement);
        System.out.println();
    }
    private void deleteDocFromHeap(Document doc)
    {
        doc.setLastUseTime(10);
        DocReplacement docReplacement = getReplacementDoc(doc);
        minHeap.reHeapify(docReplacement); //this should upHeap
        minHeap.remove();
        replacementDocSet.remove(docReplacement);
        docSet.remove(doc);

    }
    private void putHeapLogic(Document doc) 
    {
        DocReplacement docReplacement = new DocReplacement(doc);
        this.replacementDocSet.add(docReplacement);

        // System.out.println("_> "+docReplacement);
        minHeap.insert(docReplacement);  
        // System.out.println("original docR: "+docReplacement);
        minHeap.reHeapify(docReplacement);  
        // docSet.add(doc); //todo error
        // docSet.add(doc);


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
        // System.out.println("uri "+uri);
        Document doc = bTree.get(uri);
        // System.out.println("gotten from btree: "+doc);
        if (doc != null) 
        {

        setDocTime(doc);
        // for (DocReplacement docRep : this.replacementDocSet) {
        //     System.out.println();
        // }
        Boolean bool = false;
        for (DocReplacement docR : this.replacementDocSet) {
            if (doc.getKey() == docR.getURI())
            {
                docR.setTime(doc.getLastUseTime());
                minHeap.reHeapify(docR);
                bool = true;
                break;
            }
        }
        // if(bool == false)
        // {
        // docSet.add(doc);
        // limitTest();
        // lambdaDelete(doc, uri);
        // putInbTreeAndTrie(uri, doc);
        // putHeapLogic(doc);
        // }
    }
    return doc;

    }
    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri)
    {
        Document docToDelete = bTree.get(uri);
        // try {
        //     this.bTree.moveToDisk(uri);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

         if (docToDelete == null)
        {
           return false;
        }  
        deleteDocFromHeap(docToDelete);
        deleteFrombTreeAndTrie(uri, docToDelete); //null error? =!
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
    private void moveUriToDisk(URI uri)
    {
        try {
            bTree.moveToDisk(uri);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
    private void lambdaPut(Document d, URI uri)
    {
        Function<URI, Boolean> lambda = (URI uriLambda) -> {   
            putInbTreeAndTrie(uri, d);
            setDocTime(d);

            DocReplacement docReplacement = new DocReplacement(d); //todo wrong?
            minHeap.insert(docReplacement);
            minHeap.reHeapify(docReplacement);     
            replacementDocSet.add(docReplacement); //todo       
            docSet.add(d); 
           
            return true;
        };
    
        commStck = new GenericCommand<URI>(uri, lambda);
        stack.push((GenericCommand<URI>) commStck); //old: stack.push(commStck);   
    }
    private void lambdaDelete(Document d, URI uri)
    {
        // Document docToDelete = d;
        Function<URI, Boolean> lambda = (URI uriLambda) -> { 
            System.out.println("delete lambda");
            System.out.println(d.getKey());
            deleteDocFromHeap(d);

            deleteFrombTreeAndTrie(uri, d);  
            docSet.remove(d);
        
            return true;
        };
    
        commStck = new GenericCommand<URI>(uri, lambda);
        stack.push((GenericCommand<URI>) commStck); //old: stack.push(commStck);   
    }
    private void putInbTreeAndTrie(URI uri, Document doc)
    {
        // System.out.println("PUT IN BTREE");
        bTree.put(uri, doc);
        Document d = bTree.get(uri);
        // System.out.println("document d: "+d);
        putInTrie(doc); 
    }
    private void deleteFrombTreeAndTrie(URI uri, Document doc) 
    {
        // System.out.println(doc);
        bTree.put(uri, null); //test
        deleteFromTrie(doc); 
        // moveUriToDisk(uri);  //todo
        // bTree.moveToDisk(uri);

    }
    private void putInTrie(Document d)
    {
        for (String s  : d.getWords()) 
        {   
            this.trie.put(s, d.getKey());            
        }
    }
    private void deleteFromTrie(Document docToDelete)
    {
        // System.out.println("@@ "+docToDelete.getKey());
        for (String s  : docToDelete.getWords()) { //delete from trie, working i think
                
            this.trie.delete(s, docToDelete.getKey());
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
   

    class DocComparator implements Comparator<URI>
    {
        private String word;

        public DocComparator(String word, Boolean bool)
        {
            this.word = word;
        }

        @Override
        public int compare(URI uri1, URI uri2) {
            if (bTree.get(uri1).wordCount(word) < bTree.get(uri2).wordCount(word))
            {
                return 1;
            }
            if (bTree.get(uri1).wordCount(word) > bTree.get(uri2).wordCount(word))
            {
                return -1;
            }
            return 0;
        }
            //     if (Doc1.wordCount(word) < Doc2.wordCount(word))
            //     {
            //         return 1;
            //     }
            //     if (Doc1.wordCount(word) > Doc2.wordCount(word))
            //     {
            //         return -1;
            //     }
            //     return 0;
            // }
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

        List<URI> uriList = new ArrayList<>();
        List<Document> docList = new ArrayList<>();
        
        uriList = trie.getAllSorted(keyword,docComp); //make sure this also returns empty list
        for (URI uri : uriList) {
            docList.add(bTree.get(uri));
        }
        for (Document doc : docList) {
            setDocTime(doc);
            DocReplacement docReplacement = getReplacementDoc(doc);
            minHeap.reHeapify(docReplacement);
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

        List<URI> uriList = new ArrayList<>();
        List<Document> docList = new ArrayList<>();

        uriList = trie.getAllWithPrefixSorted(keywordPrefix, docComp);
        for (URI uri : uriList) {
            docList.add(bTree.get(uri));
        }
        for (Document doc : docList) {
            setDocTime(doc);
            DocReplacement docReplacement = getReplacementDoc(doc);

            minHeap.reHeapify(docReplacement);
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
        uriSet = trie.deleteAll(keyword); 
        for (URI uri : uriSet) {
            valueSet.add(bTree.get(uri));
        }
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
        uriSet = trie.deleteAllWithPrefix(keywordPrefix); 
        for (URI uri : uriSet) {
            valueSet.add(bTree.get(uri));
        }
        
        CommandSet<URI> commStckSet = new CommandSet<>();
        uriSet = commandSetLogic(valueSet, uriSet, commStckSet);
        int i = 0;
        for (Document doc : valueSet) 
        {
            // System.out.println(doc.getKey());
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
            this.bTree.put(uri, null); //delete from bTree
            
            Function<URI, Boolean> lambda = (URI uriLambda) -> { 
            
                bTree.put(uriLambda, document);
                putInTrie(document); 
                document.setLastUseTime(this.setSameTime);
                putHeapLogic(document);// stage 4: doc
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
