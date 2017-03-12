package net.ttddyy.dsproxy.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class CallableBatchExecution extends BaseQueryExecution implements BatchParameterHolder, QueryHolder, BatchExecution {

    private String query;

    private List<BatchExecutionEntry> batchExecutionEntries = new ArrayList<BatchExecutionEntry>();

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
        return batchExecutionEntries;
    }

    public boolean addBatchExecutionEntry(CallableBatchExecutionEntry entry) {
        return this.batchExecutionEntries.add(entry);
    }

}
