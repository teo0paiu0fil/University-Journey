package mydatabase;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MyCollection implements ICollection {

    public MyCollection(String url) {
        this(url, false);
    }

    public MyCollection(String url, boolean user) {
        this.url = url;
        isUserCollection = user;
    }

    private boolean isUserCollection;
    private String url;

    public List<String[]> getColection() {
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build();
        try {
            CSVReader csvReader = new CSVReaderBuilder(new FileReader("src/main/resources/" + url))
                    .withCSVParser(csvParser)
                    .withSkipLines(1)
                    .build();
            List<String[]> list = csvReader.readAll();
            Collections.reverse(list);
            return list;
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getElementByID(String id) {
        int index = 1;
        if(isUserCollection)
            index = 0;

        List<String[]> data = getColection();
        for (String[] line :data) {
            if (line[index].compareTo(id) == 0)
                return line;
        }

        return null;
    }
}
