package Model;

import java.io.*;
import java.util.*;

public abstract class TemplateDatabase<T>{

    /**
     * Generic database that provides concrete database with HashMap to store
     * objects and basic getters and setters.
     */

    private HashMap<Long, T> data = new HashMap<>();
    private long currID = 0;

    /**
     * Reads file into database
     * @param fileName
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readDatabase(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        this.data = (HashMap<Long, T>) ois.readObject();
        if (data.isEmpty()) {
            this.currID = 0;
        } else {
            this.currID = Collections.max(this.data.keySet()) + 1;
        }
    }

    public void saveDatabase(String fileName) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(this.data);
    }

    public T getItemByID(Long id) {
        if (this.data.containsKey(id)) {
            return this.data.get(id);
        }
        return null;
    }

    public void addItem(T item) {
        this.data.put(new Long(this.currID), item);
        this.currID += 1;
    }

    public List<T> getListOfItems() {
        return new ArrayList<>(this.data.values());
    }


    public long getCurrID() {
        return this.currID;
    }

}
