import java.io.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;

public abstract class AbstractDatabase<T> {
    HashMap<Long, T> data = new HashMap<Long, T>();
    private long currID = 0;

    //added to deal with case when database is empty
    public boolean isEmpty(){
        if(data.isEmpty()){
            return true;
        }
        return false;
    }

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

    public void removeItemByID(Long id) {
        this.data.remove(id);
    }

    public ArrayList<T> getListOfItems() {
        ArrayList<T> ret = new ArrayList<T>();
        for (Long id : this.data.keySet()) {
            ret.add(this.data.get(id));
        }
        return ret;
    }

    public void printAll() {
        for (Long id : this.data.keySet()) {
            System.out.println("[" + id.toString() + "] " + this.data.get(id).toString() + "\n");
        }
    }

    public HashMap<Long, T> getData() {
        return this.data;
    }

    public long getCurrID() {
        return this.currID;
    }

}
