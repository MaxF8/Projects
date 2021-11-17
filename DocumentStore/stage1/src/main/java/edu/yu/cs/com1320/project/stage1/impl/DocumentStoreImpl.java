package edu.yu.cs.com1320.project.stage1.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
// import java.io.ByteArrayInputStream;


import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentStore;

public class DocumentStoreImpl implements DocumentStore
{

private Document docEntry;

private HashTable<URI,Document> hashTable; 

    public DocumentStoreImpl()
    {
        this.hashTable = new HashTableImpl<>(); 
        this.docEntry = null;
    }

    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc. If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     */
    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException
    {  
        if (format == null)
        {
            throw new IllegalArgumentException();
        }
        if (uri == null)
        {
            throw new IllegalArgumentException();
        }
        if (input == null){
            // System.out.println("input is null");
            if (hashTable.get(uri) == null){
                return 0;
            }
            else { 
                Document docToDelete = hashTable.get(uri);
                deleteDocument(uri);
                return docToDelete.hashCode();
            }
        }   
        Document doc = null; 
        byte[] byteTxt = input.readAllBytes();
        if (format == DocumentFormat.TXT){
            String str = new String(byteTxt);   //check for both cases tho
            doc = new DocumentImpl(uri,str);
        }
        else if (format == DocumentFormat.BINARY){
            // System.out.println("byte");
            doc = new DocumentImpl(uri,byteTxt);
        }   
        if (hashTable.get(uri) == null){
            hashTable.put(uri,doc);
            this.docEntry = doc;
            return 0;
        }
        else{
            hashTable.put(uri,doc);
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
        Document doc = hashTable.get(uri);

         if (doc == null)
        {

           return false;
        }

        hashTable.put(uri, null);
        return true;
    }
}
