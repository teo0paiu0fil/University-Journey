package mydatabase;

import steams.Stream;
import users.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionDecoratorUser extends CollectionDecorator{

    LinkedHashMap<String, User> data;

    public CollectionDecoratorUser(MyCollection collection) {
        super(collection);
        data = new LinkedHashMap<>();
        loadData();
    }

    @Override
    public List<String[]> getColection() {
        return super.getColection();
    }

    private void loadData() {
        List<String[]> rowData = getColection();

        for (String[] line : rowData) {
            User user = new User(line);
            data.put(line[0], user);
        }
    }

    @Override
    public Object getElementByID(String id) {
        String[] info = (String[]) super.getElementByID(id);
        if (info == null) return null;
        return data.get(info[0]);
    }

    @Override
    public void addToData(String arg, Stream stream) {
        // code to add user in future commits
    }

    @Override
    public void deleteFromData(String id) {
        // code to remove user in future commits
    }

    @Override
    public Map<String, User> getData() {
        return data;
    }
}
