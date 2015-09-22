package ro.satrapu.churchmanagement.persistence.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a query.
 *
 * @author satrapu
 */
public class QuerySearchResult<T> {
    private long totalCount;
    private List<T> records;

    public QuerySearchResult(List<T> records) {
        this.records = new ArrayList<>(records);
        this.totalCount = this.records.size();
    }

    public QuerySearchResult(List<T> records, long totalCount) {
        this(records);
        this.totalCount = totalCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public List<T> getRecords() {
        return records;
    }
}
