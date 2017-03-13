package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.QueryType;
import net.ttddyy.dsproxy.asserts.CallableBatchExecution;
import net.ttddyy.dsproxy.asserts.CallableExecution;
import net.ttddyy.dsproxy.asserts.PreparedBatchExecution;
import net.ttddyy.dsproxy.asserts.PreparedExecution;
import net.ttddyy.dsproxy.asserts.ProxyTestDataSource;
import net.ttddyy.dsproxy.asserts.StatementBatchExecution;
import net.ttddyy.dsproxy.asserts.StatementExecution;

import javax.sql.DataSource;
import java.sql.Types;

import static net.ttddyy.dsproxy.asserts.assertj.DataSourceProxyAssertions.assertThat;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter.nullParam;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter.outParam;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter.param;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters.containsParamIndexes;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters.containsParamKeys;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters.containsParamNames;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters.containsParams;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters.containsParamsExactly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

/**
 * API compilation check with AssertJ.
 *
 * @author Tadaya Tsuyukubo
 */
public class AssertJAssertionApiCheck {

    private DataSource actualDataSource;

    private void testDataSource() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);

        // execution count
        assertThat(ds).hasExecutionCount(3);
        assertThat(ds).hasStatementCount(3);
        assertThat(ds).hasBatchStatementCount(3);
        assertThat(ds).hasStatementOrBatchStatementCount(3);
        assertThat(ds).hasPreparedCount(3);
        assertThat(ds).hasBatchPreparedCount(3);
        assertThat(ds).hasPreparedOrBatchPreparedCount(3);
        assertThat(ds).hasCallableCount(3);
        assertThat(ds).hasBatchCallableCount(3);
        assertThat(ds).hasCallableOrBatchCallableCount(3);

        assertThat(ds).hasTotalQueryCount(3);
        assertThat(ds).hasSelectCount(3);
        assertThat(ds).hasInsertCount(3);
        assertThat(ds).hasUpdateCount(3);
        assertThat(ds).hasDeleteCount(3);
        assertThat(ds).hasOtherCount(3);

        ds.reset();

        assertThat(ds.getQueryExecutions().get(0)).isSuccess();
        assertThat(ds.getQueryExecutions().get(0)).isFailure();

        assertThat(ds.getQueryExecutions().get(0)).isBatch();
        assertThat(ds.getQueryExecutions().get(0)).isStatement();
        assertThat(ds.getQueryExecutions().get(0)).isBatchStatement();
        assertThat(ds.getQueryExecutions().get(0)).isStatementOrBatchStatement();
        assertThat(ds.getQueryExecutions().get(0)).isPrepared();
        assertThat(ds.getQueryExecutions().get(0)).isBatchPrepared();
        assertThat(ds.getQueryExecutions().get(0)).isPreparedOrBatchPrepared();
        assertThat(ds.getQueryExecutions().get(0)).isCallable();
        assertThat(ds.getQueryExecutions().get(0)).isBatchCallable();
        assertThat(ds.getQueryExecutions().get(0)).isCallableOrBatchCallable();

        assertThat(ds.getQueryExecutions().get(0)).asStatement();
        assertThat(ds.getQueryExecutions().get(0)).asBatchStatement();
        assertThat(ds.getQueryExecutions().get(0)).asPrepared();
        assertThat(ds.getQueryExecutions().get(0)).asBatchPrepared();
        assertThat(ds.getQueryExecutions().get(0)).asCallable();
        assertThat(ds.getQueryExecutions().get(0)).asBatchCallable();

        assertThat(ds.getFirstBatchStatement()).queries().contains("ABC", atIndex(3));
        assertThat(ds.getFirstBatchStatement().getQueries().get(3)).startsWith("SELECT ");
    }


    private void statementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        StatementExecution se = ds.getFirstStatement();

        assertThat(se).isSuccess();
        assertThat(se).isFailure();

        assertThat(se.getQuery()).isEqualTo("...");
        assertThat(se).hasQueryType(QueryType.SELECT);
    }

    private void batchStatementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        StatementBatchExecution sbe = ds.getFirstBatchStatement();

        assertThat(sbe).isSuccess();
        assertThat(sbe).isFailure();

        assertThat(sbe).hasBatchSize(3);

        // check batch queries
        assertThat(sbe.getQueries().get(0)).isEqualTo("...");
        assertThat(sbe).contains(QueryType.SELECT, atIndex(0));
    }

    public void preparedExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        PreparedExecution pe = ds.getFirstPrepared();

        assertThat(pe).isSuccess();
        assertThat(pe).isFailure();

        assertThat(pe).containsParam(10, "value").containsParam(10, "value");
        assertThat(pe).containsParams(param(10, "value"), param(10, "value"), nullParam(12));
        assertThat(pe).containsParamIndex(10);
        assertThat(pe).containsParamIndexes(10, 11);
        assertThat(pe).containsParamValuesExactly("value", 100, null, 12);

        assertThat(pe.getQuery()).isEqualTo("...");
    }

    public void preparedBatchExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        PreparedBatchExecution pbe = ds.getFirstBatchPrepared();

        assertThat(pbe).isSuccess();
        assertThat(pbe).isFailure();

        assertThat(pbe).hasBatchSize(3);

        assertThat(pbe).batch(0, containsParams(param(10, "value"), param(11, 100), nullParam(12)));
        assertThat(pbe).batch(0, containsParamsExactly(param(10, "value"), param(11, 100), nullParam(12)));
        assertThat(pbe).batch(0, containsParamIndexes(10, 11));
//        assertThat(pbe).batch(0, containsParamValuesExactly("value", 100, null, 12));  // TODO: for values??

        assertThat(pbe.getQuery()).isEqualTo("...");
    }


    public void callableExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        CallableExecution ce = ds.getFirstCallable();

        assertThat(ce).isSuccess();
        assertThat(ce).isFailure();
        assertThat(ce.getQuery()).isEqualTo("...");

        assertThat(ce).containsParam("key", "value")
                .containsParam(10, "value")
                .containsOutParam(10, Types.INTEGER)
                .containsNullParam(10);

        assertThat(ce).containsParams(param("key", "value"), param(10, "value"), param("a", 100), outParam("key", Types.INTEGER), nullParam("key"));
        assertThat(ce).containsParamsExactly(param("key", "value"), param(10, "value"), param("a", 100), outParam("key", Types.INTEGER), nullParam("key"));
        assertThat(ce).containsParamKeys("key").containsParamKeys(10).containsParamIndex(10).containsParamName("key");
        assertThat(ce).containsParamKeys("key", 10).containsParamIndexes(10, 11).containsParamNames("key", "key");

//        assertThat(ce).containsOutParams(param("key", "value"), paramAsString("key", "value"));
//        assertThat(ce).containsNullParams(param("key", "value"), paramAsString("key", "value"));

    }

    public void callableBatchExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        CallableBatchExecution cbe = ds.getFirstBatchCallable();

        assertThat(cbe).isSuccess();
        assertThat(cbe).isFailure();

        assertThat(cbe).hasBatchSize(3);
        assertThat(cbe).batch(0, containsParams(param("key", "value"), param("key", "value")));
        assertThat(cbe).batch(0, containsParams(param("key", "value"), param(10, "value"), param("a", 100), outParam("key", Types.INTEGER), nullParam("key")));
        assertThat(cbe).batch(0, containsParams(param("key", "value"), param(10, "value")));
        assertThat(cbe).batch(0, containsParams(outParam("key", Types.INTEGER), outParam(10, Types.INTEGER)));
        assertThat(cbe).batch(0, containsParams(nullParam("key", Types.INTEGER), nullParam(10)));

        assertThat(cbe).batch(0, containsParamKeys("key", 10));
        assertThat(cbe).batch(0, containsParamIndexes(10, 11));
        assertThat(cbe).batch(0, containsParamNames("key", "key2"));

        assertThat(cbe.getQuery()).isEqualTo("...");

    }
}
