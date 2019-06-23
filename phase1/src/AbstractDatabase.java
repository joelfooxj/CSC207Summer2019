import java.util.HashMap;

public abstract class AbstractDatabase {
  HashMap<Long, Object> data;

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
    return this.data.get(id);
  }

  public abstract void addItem();

  public void removeItemByID(Long id) {
    this.data.remove(id);
  }

  public void printAll() {
    for (Long id : this.data.keySet()) {
      System.out.println("[" + id.toString() + "] " + this.data.get(id).toString() + "\n");
    }
  }
}
