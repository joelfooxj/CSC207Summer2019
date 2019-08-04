package Model;
import java.io.*;
import java.util.*;

public abstract class TemplateDatabase<T> implements Iterable<T> {

    HashMap<Long, T> data = new HashMap<Long, T>();
    private long currID = 0;

    //added to deal with case when database is empty
//    public boolean isEmpty(){
//        if(data.isEmpty()){
//            return true;
//        }
//        return false;
//    }

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

    public long getCurrID() {
        return this.currID;
    }

    public class AbstractDatabaseIterator implements Iterator<T>{
        private int current = 0;

        @Override
//        public boolean hasNext() {
//            if(current<data.size()){
//                return true;
//            }
//            else{
//                return false;
//            }
//        }
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
