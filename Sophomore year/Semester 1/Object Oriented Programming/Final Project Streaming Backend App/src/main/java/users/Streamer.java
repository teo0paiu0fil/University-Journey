package users;

import mydatabase.CollectionDecorator;
import mydatabase.MyDatabase;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import steams.Stream;
import steams.StreamBuilder;

public class Streamer extends Client {

    public Streamer(String[] args) {
        super.setId(Integer.parseInt(args[1]));
        super.setName(args[2]);
    }

    public void addStream(String[] args, CollectionDecorator streamsCollection) {
        Stream stream = new StreamBuilder()
                .withType(Integer.parseInt(args[2]))
                .withId(Integer.parseInt(args[3]))
                .withStreamGenre(Integer.parseInt(args[4]))
                .withNoOfStreams(0)
                .withStreamerId(this.getId())
                .withLenght(Long.parseLong(args[5]))
                .withDateAdded(System.currentTimeMillis())
                .withName(args[6])
                .build();
        streamsCollection.addToData(args[3], stream);
    }


    public void deleteStream(String id, @NotNull CollectionDecorator collectionDecorator) {
        Stream stream = (Stream) collectionDecorator.getElementByID(id);
        if (this.getId() == stream.getStreamerId())
            collectionDecorator.deleteFromData(id);
    }

    @Override
    public void showStreams(MyDatabase db) {
        CollectionDecorator streamsCollection = db.getStreams();
        JSONArray jsonArray = new JSONArray();
        for (Object stream: streamsCollection.getData().values() ) {
            if(((Stream)stream).getStreamerId() == this.getId())
                jsonArray.put(super.makeJson((Stream) stream, db));
        }
        System.out.println(jsonArray.toString());
    }
}
