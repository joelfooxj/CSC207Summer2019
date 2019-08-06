package Model;
import java.io.*;
import java.util.*;

public abstract class TemplateDatabase<T> implements Iterable<T> {

    HashMap<Long, T> data = new HashMap<Long, T>();
    private long currID = 0;



    public boolean isEmpty(){
        return data.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractDatabaseIterator();
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

    public List<T> getListOfItems() {
        List<T> ret = new ArrayList<>();
        ret.addAll(this.data.values());
        return ret;
    }


    public long getCurrID() {
        return this.currID;
    }

    public class AbstractDatabaseIterator implements Iterator<T>{
        private int current = 0;

        @Override
        public boolean hasNext(){
            return current<getListOfItems().size();
        }


        @Override
        public T next(){
            if(hasNext()) {
                T result = getListOfItems().get(current);
                current ++;
                return result;
            }
            throw new NoSuchElementException();
        }

    }

}
