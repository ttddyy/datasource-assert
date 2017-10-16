package net.ttddyy.dsproxy.asserts;

import java.util.List;

/**
 * Represent an execution that hold multiple queries.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public interface QueriesHolder {

    List<String> getQueries();

}
