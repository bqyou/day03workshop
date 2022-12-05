package cartdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCartMemory {

    public HashMap<String, ArrayList<String>> cartDb = new HashMap<String, ArrayList<String>>();

    public ShoppingCartMemory(String folderName) {

        this.cartDb = this.loadDataFromFile(folderName);

    }

    public HashMap<String, ArrayList<String>> loadDataFromFile(String folderName) {
        File f = new File(folderName);
        File[] filteredFiles = f.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".db");
            }
        });

        for (File file : filteredFiles) {
            String userKey = file.getName().replace(".db", "");
            this.cartDb.put(userKey, readFile(file));
        }
        return this.cartDb;
    }

    // method to read files
    public ArrayList<String> readFile(File f) {
        ArrayList<String> data = new ArrayList<String>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = bf.readLine();
            while ((line = bf.readLine()) != null) {
                line = line.trim();
                data.add(line);
            }
            bf.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
}
