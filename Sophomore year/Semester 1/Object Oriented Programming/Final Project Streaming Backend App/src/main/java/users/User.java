package users;

import mydatabase.CollectionDecorator;
import mydatabase.MyDatabase;
import org.json.JSONArray;
import steams.Stream;

import java.util.*;

public class User extends Client {

    private List<String> history;

    public User(String[] args) {
        super.setId(Integer.parseInt(args[0]));
        super.setName(args[1]);
        this.history = new ArrayList<>();
        String[] songIds = args[2].split(" ");
        this.history.addAll(Arrays.asList(songIds));
    }
    public void recommand3(int type , MyDatabase db) {
        List<Streamer> list = getNotListenStreamers(type, db);
        List<Stream> songs = getAllSongs(list, db, true);
        JSONArray jsonArray = new JSONArray();
        songs.sort(Stream::compareTo2);
        int a = 0;
        for (int i = 0; i < songs.size(); i++) {
            a++;
            jsonArray.put(super.makeJson(songs.get(i),db));
            if(a == 3) break;
        }
        System.out.println(jsonArray.toString());
    }

    private List<Streamer> getNotListenStreamers(int type, MyDatabase db) {
        CollectionDecorator streamersCollection = db.getStreamers();
        Collection<Streamer> listAllStreamers= (Collection<Streamer>) streamersCollection.getData().values();
        List<Streamer> list = getListenStreamers(type, db);
        listAllStreamers.removeAll(list);
        return new ArrayList<>(listAllStreamers);
    }

    public List<Streamer> getListenStreamers(int type, MyDatabase db) {
        CollectionDecorator streamsCollection = db.getStreams();
        CollectionDecorator streamersCollection = db.getStreamers();
        List<Streamer> streamersList = new ArrayList<>();
        for (String id: history) {
            Stream stream = (Stream) streamsCollection.getElementByID(id);
            if (stream.getType () == type)
                streamersList.add((Streamer) streamersCollection.
                        getElementByID(String.valueOf(stream.getStreamerId())));
        }
        Set<Streamer> set = new HashSet<>(streamersList);
        streamersList.clear();
        streamersList.addAll(set);
        return streamersList;
    }

    public void recommand5(int type, MyDatabase db) {
        List<Streamer> list = getListenStreamers(type, db);
        List<Stream> songs = getAllSongs(list, db, false);
        JSONArray jsonArray = new JSONArray();
        songs.sort(Stream::compareTo);
        int a = 0;

        for (int i = 0; i < songs.size(); i++) {
            a++;
            jsonArray.put(super.makeJson(songs.get(i),db));
            if(a == 5) break;
        }

        System.out.println(jsonArray.toString());
    }

    private List<Stream> getAllSongs(List<Streamer> list, MyDatabase db, boolean isSurprise) {
        CollectionDecorator streamsCollection = db.getStreams();
        List<Stream> streams = new ArrayList<>();

        for (Streamer streamer : list) {
            for (Object stream : streamsCollection.getData().values()) {
                Stream stream1 = (Stream) stream;
                if ( stream1.getStreamerId() == streamer.getId())
                    streams.add(stream1);
            }
        }

        if(!isSurprise) {
            for (String id : history) {
                Stream stream = (Stream) streamsCollection.getElementByID(id);
                streams.remove(stream);
            }
        }

        return streams;
    }

    @Override
    public void showStreams(MyDatabase db) {
        CollectionDecorator streamsCollection = db.getStreams();
        JSONArray jsonArray = new JSONArray();

        for (String songId: history) {
            Stream stream = (Stream) streamsCollection.getElementByID(songId);
            jsonArray.put(super.makeJson(stream,db));
        }

        System.out.println(jsonArray.toString());
    }

    public void listen(String aux, MyDatabase db) {
        CollectionDecorator collectionDecorator = db.getStreams();
        Stream stream = (Stream) collectionDecorator.getElementByID(aux);
        stream.incNoOfStreams();

        if(!this.history.contains(aux))
            this.history.add(aux);
    }

}
