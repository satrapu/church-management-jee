package ro.satrapu.churchmanagement.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a query.
 *
 * @param <T>
 * @author satrapu
 */
public class QuerySearchResult<T> {
    private List<T> records;

    public QuerySearchResult(List<T> records) {
        this.records = new ArrayList<>(records);
    }

    public List<T> getRecords() {
        return records;
    }
}
