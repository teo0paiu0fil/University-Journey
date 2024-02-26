package mydatabase;

import steams.Stream;
import steams.StreamBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionDecoratorStream extends CollectionDecorator {
    private LinkedHashMap<String, Stream> data;

    public CollectionDecoratorStream(MyCollection collection) {
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
            Stream user = new StreamBuilder()
                    .withType(Integer.parseInt(line[0]))
                    .withId(Integer.parseInt(line[1]))
                    .withStreamGenre(Integer.parseInt(line[2]))
                    .withNoOfStreams(Long.parseLong(line[3]))
                    .withStreamerId(Integer.parseInt(line[4]))
                    .withLenght(Long.parseLong(line[5]))
                    .withDateAdded(Long.parseLong(line[6]))
                    .withName(line[7])
                    .build();

            data.put(line[1], user);
        }
    }

    @Override
    public Object getElementByID(String id) {
        String[] info = (String[]) super.getElementByID(id);
        return data.get(id);
    }

    @Override
    public Map<String, Stream> getData() {
        return data;
    }

    @Override
    public void addToData(String id, Stream stream) {
        data.put(id, stream);
    }

    @Override
    public void deleteFromData(String id) {
        data.remove(id);
    }
}
