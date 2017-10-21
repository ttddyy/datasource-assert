package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.CallableBatchExecution;
import net.ttddyy.dsproxy.asserts.CallableExecution;
import net.ttddyy.dsproxy.asserts.PreparedBatchExecution;
import net.ttddyy.dsproxy.asserts.PreparedExecution;
import net.ttddyy.dsproxy.asserts.ProxyTestDataSource;
import net.ttddyy.dsproxy.asserts.QueryExecution;
import net.ttddyy.dsproxy.asserts.StatementBatchExecution;
import net.ttddyy.dsproxy.asserts.StatementExecution;
import net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter;
import net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters;

import java.sql.SQLType;

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

    // from ExecutionParameter

    public static ExecutionParameter param(int paramIndex, Object value) {
        return ExecutionParameter.param(paramIndex, value);
    }

    public static ExecutionParameter param(String paramName, Object value) {
        return ExecutionParameter.param(paramName, value);
    }

    public static ExecutionParameter nullParam(int index, int sqlType) {
        return ExecutionParameter.nullParam(index, sqlType);
    }

    public static ExecutionParameter nullParam(int index) {
        return ExecutionParameter.nullParam(index);
    }

    public static ExecutionParameter nullParam(String name, int sqlType) {
        return ExecutionParameter.nullParam(name, sqlType);
    }

    public static ExecutionParameter nullParam(String name) {
        return ExecutionParameter.nullParam(name);
    }

    public static ExecutionParameter outParam(int paramIndex, int sqlType) {
        return ExecutionParameter.outParam(paramIndex, sqlType);
    }

    public static ExecutionParameter outParam(int paramIndex, SQLType sqlType) {
        return ExecutionParameter.outParam(paramIndex, sqlType);
    }

    public static ExecutionParameter outParam(String paramName, int sqlType) {
        return ExecutionParameter.outParam(paramName, sqlType);
    }

    public static ExecutionParameter outParam(String paramName, SQLType sqlType) {
        return ExecutionParameter.outParam(paramName, sqlType);
    }

    // from ExecutionParameters

    public static ExecutionParameters containsParams(ExecutionParameter... params) {
        return ExecutionParameters.containsParams(params);
    }

    public static ExecutionParameters containsParamsExactly(ExecutionParameter... params) {
        return ExecutionParameters.containsParamsExactly(params);
    }

    public static ExecutionParameters containsParamKeys(Object... paramKeys) {
        return ExecutionParameters.containsParamKeys(paramKeys);
    }

    public static ExecutionParameters containsParamIndexes(int... paramIndexes) {
        return ExecutionParameters.containsParamIndexes(paramIndexes);
    }

    public static ExecutionParameters containsParamNames(String... paramNames) {
        return ExecutionParameters.containsParamNames(paramNames);
    }
}
