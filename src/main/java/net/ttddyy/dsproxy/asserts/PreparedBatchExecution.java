package net.ttddyy.dsproxy.asserts;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a single batch execution of {@link java.sql.PreparedStatement}.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class PreparedBatchExecution extends BaseQueryExecution implements QueryHolder, BatchParameterHolder, BatchExecution {

    private String query;
    private List<BatchExecutionEntry> batchExecutionEntries = new ArrayList<>();

    @Override
    public boolean isBatch() {
        return true;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public List<BatchExecutionEntry> getBatchExecutionEntries() {
        return this.batchExecutionEntries;
    }

    public boolean addBatchExecutionEntry(PreparedBatchExecutionEntry entry) {
        return this.batchExecutionEntries.add(entry);
    }

    @SuppressWarnings("unchecked")
    public List<PreparedBatchExecutionEntry> getPrepareds() {

        return (List<PreparedBatchExecutionEntry>) (List<?>) this.batchExecutionEntries;
    }

}
