package mydatabase;

import java.util.List;

public interface ICollection {
    List<String[]> getColection();
    Object getElementByID(String id);

}
