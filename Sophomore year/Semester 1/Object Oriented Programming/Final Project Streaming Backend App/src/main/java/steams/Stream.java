package steams;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Stream implements Comparable{
    private int id;
    private int streamGenre;
    private int streamerId;
    private long noOfStreams;
    private long dateAdded;
    private long length;
    private String name;
    private int type;

    public void incNoOfStreams() {
        this.noOfStreams++;
    }

    public int getId() {
        return id;
    }

    public int getStreamGenre() {
        return streamGenre;
    }

    public int getStreamerId() {
        return streamerId;
    }

    public long getNoOfStreams() {
        return noOfStreams;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public long getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStreamGenre(int streamGenre) {
        this.streamGenre = streamGenre;
    }

    public void setStreamerId(int streamerId) {
        this.streamerId = streamerId;
    }

    public void setNoOfStreams(long noOfStreams) {
        this.noOfStreams = noOfStreams;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if(this.getNoOfStreams() < ((Stream)o).getNoOfStreams())
            return 1;
        else if  (this.getNoOfStreams() > ((Stream)o).getNoOfStreams())
            return -1;
        return 0;
    }

    public int compareTo2(Stream stream) {
        Date date1 = new Date(this.getDateAdded());
        Date date2 = new Date(stream.getDateAdded());
        SimpleDateFormat fd = new SimpleDateFormat("dd-MM-yyyy");

        try {
            date1 = fd.parse(fd.format(date1));
            date2 = fd.parse(fd.format(date2));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (date1.compareTo(date2) < 0)
            return 1;
        else if (date1.compareTo(date2) > 0)
            return -1;
        else return this.compareTo(stream);
    }
}
