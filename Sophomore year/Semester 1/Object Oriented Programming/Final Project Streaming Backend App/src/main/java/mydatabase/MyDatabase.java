package mydatabase;

public class MyDatabase {

    private static MyDatabase db;
    private String[] urls;

    private MyDatabase(String[] urls) {
        MyCollection collection = new MyCollection(urls[1]);
        streams = new CollectionDecoratorStream(collection);
        MyCollection collection1 = new MyCollection(urls[0]);
        streamers = new CollectionDecoratorStreamer(collection1);
        MyCollection collection2 = new MyCollection(urls[2], true);
        users = new CollectionDecoratorUser(collection2);
        this.urls = urls;
    }

    private CollectionDecorator streams;
    private CollectionDecorator users;
    private CollectionDecorator streamers;

    public CollectionDecorator getStreams() {
        return streams;
    }

    public CollectionDecorator getUsers() {
        return users;
    }

    public CollectionDecorator getStreamers() {
        return streamers;
    }

    public static MyDatabase getInstance(String[] urls) {
        MyDatabase database = db;
        if (database == null) return db = new MyDatabase(urls);
        database.urls = urls;
        database.resetCollections();
        return database;
    }

    public void resetCollections() {
        MyCollection collection = new MyCollection(urls[1]);
        streams = new CollectionDecoratorStream(collection);
        MyCollection collection1 = new MyCollection(urls[0]);
        streamers = new CollectionDecoratorStreamer(collection1);
        MyCollection collection2 = new MyCollection(urls[2], true);
        users = new CollectionDecoratorUser(collection2);
    }

}
