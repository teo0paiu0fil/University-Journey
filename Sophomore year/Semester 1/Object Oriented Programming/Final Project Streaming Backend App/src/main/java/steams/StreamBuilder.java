package steams;

public class StreamBuilder {

    private Stream stream;

    public StreamBuilder() {
        this.stream = new Stream();
    }

    public StreamBuilder withId(int id) {
        stream.setId(id);
        return this;
    }

    public StreamBuilder withNoOfStreams(long no) {
        stream.setNoOfStreams(no);
        return this;
    }

    public StreamBuilder withStreamGenre(int type) {
        stream.setStreamGenre(type);
        return this;
    }

    public StreamBuilder withStreamerId(int id) {
        stream.setStreamerId(id);
        return this;
    }

    public StreamBuilder withName(String name) {
        stream.setName(name);
        return this;
    }

    public StreamBuilder withDateAdded(long date) {
        stream.setDateAdded(date);
        return this;
    }

    public StreamBuilder withLenght(long lenght) {
        stream.setLength(lenght);
        return this;
    }

    public Stream build() {
        return stream;
    }

    public StreamBuilder withType(int type) {
        stream.setType(type);
        return this;
    }
}
