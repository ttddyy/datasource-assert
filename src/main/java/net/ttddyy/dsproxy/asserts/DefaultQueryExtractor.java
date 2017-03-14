package net.ttddyy.dsproxy.asserts;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class DefaultQueryExtractor implements QueryExtractor {

    @Override
    public List<String> getQueries(QueryExecution queryExecution) {
        List<String> queries = new ArrayList<>();
        if (queryExecution instanceof QueryHolder) {
            queries.add(((QueryHolder) queryExecution).getQuery());
        } else if (queryExecution instanceof QueriesHolder) {
            queries.addAll(((QueriesHolder) queryExecution).getQueries());
        }
        return queries;
    }

}
