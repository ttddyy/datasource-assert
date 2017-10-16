package net.ttddyy.dsproxy.asserts;

/**
 * Represent single execution of {@link java.sql.Statement}.
 *
 * @author Tadaya Tsuyukubo
 */
public class StatementExecution extends BaseQueryExecution implements QueryHolder {

    private String query;

    @Override
    public boolean isBatch() {
        return false;
    }

    @Override
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
