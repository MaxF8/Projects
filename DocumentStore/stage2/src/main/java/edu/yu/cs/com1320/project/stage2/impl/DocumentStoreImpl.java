package edu.yu.cs.com1320.project.stage2.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
// import java.io.ByteArrayInputStream;
import java.util.function.Function;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;

public class DocumentStoreImpl implements DocumentStore
{

private Command commStck;
private Document docEntry;

private Stack<Command> stack;



private HashTable<URI,Document> hashTable; 

    public DocumentStoreImpl()
    {
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
   
   

    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException{
             /*
        if put(new uri) -> delete uri -good v/
        if put(overwrite uri) -> put(uri, oldDoc) TODO
        if put(del uri) -> put(uri,doc) 
        /you must put whatever was deleted back into the DocumentStore exactly as it was before  
    */
       testNull(uri,format);
        if (input == null){
            // System.out.println("input is null");
            if (hashTable.get(uri) == null){
                return 0;
            }
            else { 
                Document docToDelete = hashTable.get(uri);//If lambda is called, it will do what is inside
                lambda1(docToDelete,uri);
                deleteDocument(uri);
                return docToDelete.hashCode();
            }
        }   
        Document doc = null; 
        byte[] byteTxt = input.readAllBytes();
        doc = testFormat( format,  uri,  byteTxt,  doc);
        if (hashTable.get(uri) == null){ //first time putting in doc  THIS WORKS v/           
            Function<URI, Boolean> lambda = (URI uriLambda) -> {  
               hashTable.put(uri, null);
                return true;
            };
             commStck = new Command(uri, lambda); 
            stack.push(commStck);   
            hashTable.put(uri,doc);
            this.docEntry = doc;
            return 0;
        }
        else{ //already a doc in table
            // System.out.println("test");
            Document oldDoc = hashTable.put(uri,doc);
           lambda1(oldDoc, uri);
        }
        return docEntry.hashCode();
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document getDocument(URI uri)
    {
        // hashTable.get(uri);
        //  System.out.println(hashTable.get(uri));

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
        hashTable.put(uri, null);

        Function<URI, Boolean> lambda = (URI uriLambda) -> { 
            
            hashTable.put(uriLambda, docToDelete);
            return true;
        };
          commStck = new Command(uri, lambda);
        stack.push(commStck);  


        // System.out.println("DELETED");
        return true;
    }

    public void undo() throws IllegalStateException {
    if (stack == null)
    {
        throw new IllegalStateException(); 
    }
    commStck = stack.pop(); 
     
    if (commStck == null)
    {
         throw new IllegalStateException(); 
    }
    commStck.undo();

    }
    private void lambda1(Document d, URI uri)
    {
        // Document docToDelete = d;
        Function<URI, Boolean> lambda = (URI uriLambda) -> {                  
            hashTable.put(uriLambda, d);
            return true;
        };
        commStck = new Command(uri, lambda);
        stack.push(commStck);   //add to stack?
    }
    public void undo(URI uri) throws IllegalStateException {
        if (stack == null)
        {
            throw new IllegalStateException(); 
        }
        Stack<Command> helperStack = new StackImpl<>();
        // System.out.println(stack.size());
        int size = stack.size();
        for (int i = 0; i < size; i++) //put everything in helperStack except specific uri
        {
            // System.out.println(i);
            //  Command com = stack.peek();
            Command com = stack.pop();
            if (com == null){
                throw new IllegalStateException(); 
            }
            if (com.getUri().equals(uri)) //==?
            {
                // System.out.println("yes:" +uri );
                com.undo();
            }
            else
            {
                helperStack.push(com);
            }
        }
        //put helperStack back in original stack
        int helperSize = helperStack.size();
        for (int j = 0; j < helperSize; j++) 
        {
            Command newCom = helperStack.pop();
            stack.push(newCom);   
        }  
    }
    private void testNull(URI uri, DocumentFormat format)
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
}
