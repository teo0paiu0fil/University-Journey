package mydatabase;

import steams.Stream;

import java.util.List;
import java.util.Map;

public abstract class CollectionDecorator implements ICollection{
    private MyCollection collection;

    public CollectionDecorator(MyCollection collection) {
        this.collection = collection;
    }

    @Override
    public List<String[]> getColection() {
        return collection.getColection();
    }

    @Override
    public Object getElementByID(String id) {
        return collection.getElementByID(id);
    }

    public abstract Map<String, ?> getData();
    public abstract void addToData(String id, Stream stream);
    public abstract void deleteFromData(String id);
}
