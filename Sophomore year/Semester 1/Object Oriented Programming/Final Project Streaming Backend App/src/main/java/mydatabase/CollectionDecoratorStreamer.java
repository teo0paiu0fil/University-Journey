package mydatabase;

import steams.Stream;
import users.Streamer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CollectionDecoratorStreamer extends CollectionDecorator {

    private LinkedHashMap<String, Streamer> data;

    public CollectionDecoratorStreamer(MyCollection collection) {
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
            Streamer user = new Streamer(line);
            data.put(line[1], user);
        }
    }

    @Override
    public Object getElementByID(String id) {
        String[] info = (String[]) super.getElementByID(id);
        if (info == null) return null;
        return data.get(info[1]);
    }

    @Override
    public void addToData(String arg, Stream stream) {
        // code to add streamers in future commits
    }

    @Override
    public void deleteFromData(String id) {
        // code to remove streamers in future commits

    }

    @Override
    public Map<String, Streamer> getData() {
        return data;
    }
}
