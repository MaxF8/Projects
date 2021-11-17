package edu.yu.cs.com1320.project.stage5.impl;
import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {

    private Map<String, Integer> wordMap;
    private URI uri;
    // private BTree<URI,Document> bTree;
    private File baseDir;
    public DocumentPersistenceManager(File baseDir)
    {
        System.out.println("pm");
        if (baseDir != null)
        {
            this.baseDir = baseDir;
        }
        else
        this.baseDir = new File(System.getProperty("user.dir"));
        // this.bTree = new BTreeImpl<>();
    }
// public static void main(String[] args) {
//     System.out.println(System.getProperty("user.dir"));
    
// }

    @Override
    public void serialize(URI uri, Document val) throws IOException 
    {
        System.out.println("serialize");
        // System.out.println(uri);
        // System.out.println(val.getKey());
        // this.wordMap = val.getWordMap();
        // Object mapper = new ObjectMapper();
     
        String newPath = this.baseDir+"\\"+uri.toString();

        newPath = newPath.replace("http://","");
        newPath = newPath.replace("/",File.separator);
        newPath = newPath+ ".json";
        // System.out.println(newPath);
        File file1 = new File(newPath);
        File p = new File(file1.getParent()); 
        p.mkdirs();
        GsonBuilder builder = new GsonBuilder(); 
        Gson gson = builder.create(); 
        String toJson = gson.toJson(val);
        System.out.println(toJson);

        FileWriter fileW = new FileWriter(newPath); 

        fileW.write(toJson);
        fileW.close();  
        System.out.println("succesful serialize");
    }
    @Override
    public Document deserialize(URI uri) throws IOException {
        
        System.out.println("deserialize>");

        String newPath = uri.toString();
        newPath = newPath.replace("http://","");
        newPath = newPath.replace("/",File.separator);
        newPath = this.baseDir+File.separator+newPath+".json";
        // System.out.println(newPath);
        GsonBuilder builder = new GsonBuilder(); 
        Gson gson = builder.create();
        //Gson gson = new Gson(); 
        File file = new File(newPath); 
        FileReader fReader = new FileReader(file);
        // String jsonString = fReader.toString();  
        Document doc = gson.fromJson(fReader,DocumentImpl.class);
        fReader.close();
        // System.out.println("succesful deserialize");
        // System.out.println(doc.getKey());
        // System.out.println(doc.getDocumentTxt());
        return doc;
    }

    @Override
    public boolean delete(URI uri) throws IOException {
        String str = uri.toString();
        str = str.replace("http://","");
        str =str+ ".json";
        File f = new File(str);
        f.delete();
        if (f.exists())
        {
            return false;
        }

        return true;
    }
}
