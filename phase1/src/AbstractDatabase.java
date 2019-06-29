import java.util.HashMap;
import java.util.ArrayList;

public abstract class AbstractDatabase<T> {
  HashMap<Long, T> data;
  private long currID = 0;

  public void populateDataFromText() {
    return;
  }

  public void eraseTextFile() {
    return;
  }

  public void writeToTextFile() {
    return;
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
