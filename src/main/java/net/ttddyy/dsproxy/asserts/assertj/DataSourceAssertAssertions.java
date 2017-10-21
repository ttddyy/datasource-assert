package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.CallableBatchExecution;
import net.ttddyy.dsproxy.asserts.CallableExecution;
import net.ttddyy.dsproxy.asserts.PreparedBatchExecution;
import net.ttddyy.dsproxy.asserts.PreparedExecution;
import net.ttddyy.dsproxy.asserts.ProxyTestDataSource;
import net.ttddyy.dsproxy.asserts.QueryExecution;
import net.ttddyy.dsproxy.asserts.StatementBatchExecution;
import net.ttddyy.dsproxy.asserts.StatementExecution;

/**
 * Aggregated assertj assertions.
 *
 * @author Tadaya Tsuyukubo
 * @see ProxyTestDataSourceAssert
 * @see StatementExecutionAssert
 * @since 1.0
 */
public class DataSourceAssertAssertions {

    public static ProxyTestDataSourceAssert assertThat(ProxyTestDataSource actual) {
        return new ProxyTestDataSourceAssert(actual);
    }

    public static QueryExecutionAssert assertThat(QueryExecution actual) {
        return new QueryExecutionAssert(actual);
    }

    public static StatementExecutionAssert assertThat(StatementExecution actual) {
        return new StatementExecutionAssert(actual);
    }

    public static StatementBatchExecutionAssert assertThat(StatementBatchExecution actual) {
        return new StatementBatchExecutionAssert(actual);
    }

    public static PreparedExecutionAssert assertThat(PreparedExecution actual) {
        return new PreparedExecutionAssert(actual);
    }

    public static PreparedBatchExecutionAssert assertThat(PreparedBatchExecution actual) {
        return new PreparedBatchExecutionAssert(actual);
    }

    public static CallableExecutionAssert assertThat(CallableExecution actual) {
        return new CallableExecutionAssert(actual);
    }

    public static CallableBatchExecutionAssert assertThat(CallableBatchExecution actual) {
        return new CallableBatchExecutionAssert(actual);
    }

}
