package net.ttddyy.dsproxy.asserts;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent single batch execution of {@link java.sql.Statement}.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class StatementBatchExecution extends BaseQueryExecution implements QueriesHolder, BatchExecution {

    private List<String> queries = new ArrayList<>();

    @Override
    public boolean isBatch() {
        return true;
    }

    @Override
    public List<String> getQueries() {
        return this.queries;
    }

    public void setQueries(List<String> queries) {
        this.queries = queries;
    }

}
