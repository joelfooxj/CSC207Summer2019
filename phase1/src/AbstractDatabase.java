import java.util.HashMap;

public abstract class AbstractDatabase {
  HashMap<Long, Object> data;
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

  public Object getItemByID(Long id) {
    if (this.data.containsKey(id)) {
      return this.data.get(id);
    }
    return null;
  }

  public void addItem(Object item) {
    this.data.put(new Long(this.currID), item);
    this.currID += 1;
  }

  public void removeItemByID(Long id) {
    this.data.remove(id);
  }

  public void printAll() {
    for (Long id : this.data.keySet()) {
      System.out.println("[" + id.toString() + "] " + this.data.get(id).toString() + "\n");
    }
  }

  public HashMap<Long, Object> getData() {
    return this.data;
  }

  public long getCurrID() {
    return this.currID;
  }
}
