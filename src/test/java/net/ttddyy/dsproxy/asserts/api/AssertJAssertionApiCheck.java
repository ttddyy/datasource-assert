package net.ttddyy.dsproxy.asserts.api;

import net.ttddyy.dsproxy.QueryType;
import net.ttddyy.dsproxy.asserts.CallableBatchExecution;
import net.ttddyy.dsproxy.asserts.CallableExecution;
import net.ttddyy.dsproxy.asserts.PreparedBatchExecution;
import net.ttddyy.dsproxy.asserts.PreparedExecution;
import net.ttddyy.dsproxy.asserts.ProxyTestDataSource;
import net.ttddyy.dsproxy.asserts.QueryExecution;
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

    private void dataSource() {
        // tag::datasource[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        // check num of executions
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

        // check num of queries by type
        assertThat(ds).hasTotalQueryCount(3);
        assertThat(ds).hasSelectCount(3);
        assertThat(ds).hasInsertCount(3);
        assertThat(ds).hasUpdateCount(3);
        assertThat(ds).hasDeleteCount(3);
        assertThat(ds).hasOtherCount(3);

        // reset datasource
        ds.reset();
        // end::datasource[]
    }

    private void queryExecution() {
        // tag::query[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        QueryExecution qe = ds.getQueryExecutions().get(0);
        assertThat(qe).isSuccess();
        assertThat(qe).isFailure();

        qe = ds.getFirstBatchPrepared();

        assertThat(qe).isBatch();
        assertThat(qe).isStatement();
        assertThat(qe).isBatchStatement();
        assertThat(qe).isStatementOrBatchStatement();
        assertThat(qe).isPrepared();
        assertThat(qe).isBatchPrepared();
        assertThat(qe).isPreparedOrBatchPrepared();
        assertThat(qe).isCallable();
        assertThat(qe).isBatchCallable();
        assertThat(qe).isCallableOrBatchCallable();

        assertThat(qe).asStatement();       // follows StatementExecutionAssert
        assertThat(qe).asBatchStatement();  // follows StatementBatchExecutionAssert
        assertThat(qe).asPrepared();        // follows PreparedExecutionAssert
        assertThat(qe).asBatchPrepared();   // follows PreparedBatchExecutionAssert
        assertThat(qe).asCallable();        // follows CallableExecutionAssert
        assertThat(qe).asBatchCallable();   // follows CallableBatchExecutionAssert

        assertThat(ds.getFirstBatchStatement()).queries().contains("ABC", atIndex(3));
        assertThat(ds.getFirstBatchStatement().getQueries().get(3)).startsWith("SELECT ");

        assertThat(ds.getQueryExecutions().get(0)).isStatement().asStatement().query().startsWith("SELECT");
        assertThat(qe).asPrepared().containsParam(1, "value").containsNullParam(1, Types.INTEGER);
        // end::query[]
    }

    private void statementExecution() {
        // tag::statement[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        StatementExecution se = ds.getFirstStatement();

        assertThat(se).isSuccess();
        assertThat(se).isFailure();

        assertThat(se.getQuery()).isEqualTo("...");  // string assertion
        assertThat(se).hasQueryType(QueryType.SELECT);

        assertThat(ds.getQueryExecutions().get(0)).isStatement().asStatement().query().startsWith("SELECT");
        // end::statement[]
    }

    private void batchStatementExecution() {
        // tag::batch-statement[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        StatementBatchExecution sbe = ds.getFirstBatchStatement();

        assertThat(sbe).isSuccess();
        assertThat(sbe).isFailure();

        assertThat(sbe).hasBatchSize(3);

        // check batch queries
        assertThat(sbe.getQueries().get(0)).isEqualTo("...");  // string assertion
        assertThat(sbe).query(atIndex(0)).isEqualTo("...");    // string assertion
        assertThat(sbe).query(0).isEqualTo("...");       // string assertion
        assertThat(sbe).queries().contains("...");  // list assertion

        assertThat(sbe).contains(QueryType.SELECT, atIndex(0));  // list assertion
        assertThat(sbe).contains(QueryType.SELECT, 0);    // list assertion

        assertThat(ds.getQueryExecutions().get(0)).isBatchStatement().asBatchStatement().queries().hasSize(3);
        // end::batch-statement[]
    }

    public void preparedExecution() {
        // tag::prepared[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        PreparedExecution pe = ds.getFirstPrepared();

        assertThat(pe).isSuccess();
        assertThat(pe).isFailure();

        assertThat(pe).containsParam(10, "value").containsParam(10, "value");
        assertThat(pe).containsParams(param(10, "value"), param(10, "value"), nullParam(12));
        assertThat(pe).containsParamIndex(10);
        assertThat(pe).containsParamIndexes(10, 11);
        assertThat(pe).containsParamValuesExactly("value", 100, null, 12);

        assertThat(pe).query().isEqualTo("...");     // string assertion
        assertThat(pe.getQuery()).isEqualTo("...");  // string assertion

        assertThat(ds.getQueryExecutions().get(0)).isPrepared().asPrepared().query().startsWith("SELECT");
        // end::prepared[]
    }

    public void preparedBatchExecution() {
        // tag::batch-prepared[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        PreparedBatchExecution pbe = ds.getFirstBatchPrepared();

        assertThat(pbe).isSuccess();
        assertThat(pbe).isFailure();

        assertThat(pbe).hasBatchSize(3);

        assertThat(pbe).batch(0, containsParamIndexes(1, 2, 3, 4));
        assertThat(pbe).batch(0, containsParams(
                param(1, "value"), param(2, 100),
                nullParam(3), nullParam(4, Types.VARCHAR))
        );
        assertThat(pbe).batch(0, containsParamsExactly(
                param(1, "value"), param(2, 100),
                nullParam(3), nullParam(4, Types.VARCHAR))
        );

        // assertThat(pbe).batch(0, containsParamValuesExactly("value", 100, null, 12));  // TODO: for values??

        assertThat(pbe).query().isEqualTo("...");     // string assertion
        assertThat(pbe.getQuery()).isEqualTo("...");  // string assertion

        assertThat(ds.getQueryExecutions().get(0)).isBatchPrepared().asBatchPrepared().query().startsWith("SELECT");
        // end::batch-prepared[]
    }

    public void callableExecution() {
        // tag::callable[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        CallableExecution ce = ds.getFirstCallable();

        assertThat(ce).isSuccess();
        assertThat(ce).isFailure();

        assertThat(ce).query().isEqualTo("...");     // string assertion
        assertThat(ce.getQuery()).isEqualTo("...");  // string assertion

        assertThat(ce).containsParam("key", "value")
                .containsParam(1, "value")
                .containsNullParam(2)
                .containsNullParam(3, Types.INTEGER)
                .containsOutParam(4, Types.INTEGER)
        ;

        assertThat(ce).containsParamIndex(1)
                .containsParamName("foo")
                .containsParamKey(2)
                .containsParamKey("bar")
        ;

        assertThat(ce).containsParamIndexes(1, 2)
                .containsParamNames("foo", "bar")
                .containsParamKeys("baz", 3)
        ;

        assertThat(ce).containsParams(
                param("key", "value"),
                param(10, "value"),
                param("a", 100),
                outParam("key", Types.INTEGER),
                nullParam("key"),
                nullParam("key", Types.INTEGER))
        ;

        assertThat(ce).containsParamsExactly(
                param("key", "value"),
                param(10, "value"),
                param("a", 100),
                outParam("key", Types.INTEGER),
                nullParam("key"));

        assertThat(ce).containsParamKeys("key").containsParamKeys(10).containsParamIndex(11).containsParamName("key");

        assertThat(ce).containsParamKeys("key", 10).containsParamIndexes(10, 11).containsParamNames("key1", "key2");

        //  assertThat(ce).containsOutParams(param("key", "value"), paramAsString("key", "value"));
        //  assertThat(ce).containsNullParams(param("key", "value"), paramAsString("key", "value"));

        assertThat(ds.getQueryExecutions().get(0)).asCallable().query().startsWith("SELECT");
        // end::callable[]
    }

    public void callableBatchExecution() {
        // tag::batch-callable[]
        ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);

        // ... perform application logic with database ...

        CallableBatchExecution cbe = ds.getFirstBatchCallable();

        assertThat(cbe).isSuccess();
        assertThat(cbe).isFailure();

        assertThat(cbe).hasBatchSize(3);

        assertThat(cbe).batch(0, containsParams(
                param("key", "value"), param("key", "value")
        ));

        assertThat(cbe).batch(0, containsParams(
                param("key", "value"), param(10, "value"),
                param("a", 100),
                outParam("key", Types.INTEGER),
                nullParam("key")
        ));

        assertThat(cbe).batch(0, containsParams(
                param("key", "value"), param(10, "value")
        ));

        assertThat(cbe).batch(0, containsParams(
                outParam("key", Types.INTEGER), outParam(10, Types.INTEGER)
        ));

        assertThat(cbe).batch(0, containsParams(
                nullParam("key", Types.INTEGER), nullParam(10))
        );

        assertThat(cbe).batch(0, containsParamKeys("key", 10));
        assertThat(cbe).batch(0, containsParamIndexes(10, 11));
        assertThat(cbe).batch(0, containsParamNames("key", "key2"));

        assertThat(cbe.getQuery()).isEqualTo("...");  // string assertion

        assertThat(ds.getQueryExecutions().get(0)).isBatchCallable().asBatchCallable().query().startsWith("SELECT");
        // end::batch-callable[]
    }

}
