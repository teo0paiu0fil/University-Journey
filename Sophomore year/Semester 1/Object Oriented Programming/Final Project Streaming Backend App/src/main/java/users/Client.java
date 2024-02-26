package users;

import mydatabase.CollectionDecorator;
import mydatabase.MyDatabase;
import org.json.JSONObject;
import steams.Stream;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Client {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void showStreams(MyDatabase db);

    public JSONObject makeJson(Stream stream, MyDatabase db) {
        CollectionDecorator collectionDecorator = db.getStreamers();
        Streamer streamer = (Streamer) collectionDecorator.
                getElementByID(String.valueOf(stream.getStreamerId()));
        Map<String,String> jsonKeyValue = new LinkedHashMap<>();
        String streamerName = streamer.getName();
        jsonKeyValue.put("id", String.valueOf(stream.getId()));
        jsonKeyValue.put("name", stream.getName());
        jsonKeyValue.put("streamerName",streamerName);
        jsonKeyValue.put("nooflistenings", String.valueOf(stream.getNoOfStreams()));
        jsonKeyValue.put("length", getLength(stream.getLength()));
        jsonKeyValue.put("dateAdded", getDate(stream.getDateAdded()));

        return new JSONObject(jsonKeyValue);
    }

    public String getLength(long timestamp) {
        Duration duration = Duration.ofSeconds(timestamp);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        if(hours>=1) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }else{
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public String getDate(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        return DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("UTC")).format(instant);
    }

}
