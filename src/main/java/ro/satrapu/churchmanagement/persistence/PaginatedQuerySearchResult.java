package ro.satrapu.churchmanagement.persistence;

import java.util.List;

/**
 * @param <T>
 */
public class PaginatedQuerySearchResult<T> extends QuerySearchResult<T> {
    private long totalRecords;

    /**
     * @param records
     */
    public PaginatedQuerySearchResult(List<T> records, long totalRecords) {
        super(records);
        this.totalRecords = totalRecords;
    }

    public long getTotalRecords() {
        return totalRecords;
    }
}
