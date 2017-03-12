package net.ttddyy.dsproxy.asserts;

import java.util.List;

/**
 * Retrieve queries from {@link QueryExecution}.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public interface QueryExtractor {

    List<String> getQueries(QueryExecution queryExecution);

}
