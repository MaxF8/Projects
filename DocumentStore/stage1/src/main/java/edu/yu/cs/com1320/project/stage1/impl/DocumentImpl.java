package edu.yu.cs.com1320.project.stage1.impl;

import java.net.URI;
// import java.net.URISyntaxException;
import java.util.*;

import edu.yu.cs.com1320.project.stage1.Document;

public class DocumentImpl implements Document {

    String documentText;
    byte[] documentBinaryData;
    URI key;

    public DocumentImpl(URI uri, String txt) 
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

        this.documentText = txt;
        this.key = uri;
        // System.out.println("new doc");
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
       
        if (this.hashCode() == obj.hashCode())
        {
            return true;
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

   /* @Override 
    public int hashCode() {
        if (documentText == null)
        {
            return Objects.hash(key, documentBinaryData);
        }
    
        return Objects.hash(key, documentText);
    }*/
}
