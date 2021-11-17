package edu.yu.cs.com1320.project.stage3.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
// import java.io.ByteArrayInputStream;
import java.util.function.Function;

// import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;

public class DocumentStoreImpl implements DocumentStore
{
  

private GenericCommand<URI> commStck; //undoable GenericCommand<URI>
private Document docEntry;

private Stack<Undoable> stack;

private HashTable<URI,Document> hashTable; 
private TrieImpl<Document> trie;
    public DocumentStoreImpl()
    {
        this.trie = new TrieImpl<>();
        this.hashTable = new HashTableImpl<>(); 
        this.docEntry = null;
        this.stack = new StackImpl<>();   
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
        if (input == null){
            if (hashTable.get(uri) == null){
                return 0;
            }
            else{ 
                Document docToDelete = hashTable.get(uri);//If lambda is called, it will do what is inside
                lambdaPut(docToDelete,uri);
                deleteDocument(uri);
                deleteFromTrie(docToDelete); // putInTrie(d);
                return docToDelete.hashCode();
            }
        }   
        Document doc = null; 
        byte[] byteTxt = input.readAllBytes();
        doc = testFormat(format,  uri,  byteTxt,  doc);
        if (hashTable.get(uri) == null){ //first time putting in doc  
            lambdaDelete(doc, uri);
            putInHashTableAndTrie(uri, doc);
            this.docEntry = doc;
            return 0;
        }
        else{ //already a doc in table, overwrite
            Document oldDoc = hashTable.put(uri,doc);
            deleteFromTrie(oldDoc);
            lambdaPut(oldDoc, uri); 
            putInTrie(doc);
        }
        return docEntry.hashCode();
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document getDocument(URI uri)
    {
        Document doc = hashTable.get(uri);
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
        deleteFromHashTableAndTrie(uri, docToDelete); //null error? =!
        lambdaPut(docToDelete, uri);
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
            // hashTable.put(uriLambda, d);
            // putInTrie(d); //todo test
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
            // hashTable.put(uriLambda, null); //test
            // deleteFromTrie(d); //todo test
            //put in trie  
            return true;
        };
    
        commStck = new GenericCommand<URI>(uri, lambda);
        stack.push((GenericCommand<URI>) commStck); //old: stack.push(commStck);   
    }
    private void putInHashTableAndTrie(URI uri, Document doc)
    {
        hashTable.put(uri, doc);
        putInTrie(doc); //todo test
    }
    private void deleteFromHashTableAndTrie(URI uri, Document doc)
    {
        hashTable.put(uri, null); //test
        deleteFromTrie(doc); //todo test
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
                putInTrie(document); //todo test
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
