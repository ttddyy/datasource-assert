package net.ttddyy.dsproxy.asserts;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static net.ttddyy.dsproxy.asserts.assertj.DataSourceProxyAssertions.assertThat;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.batchCallableCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.batchPreparedCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.batchStatementCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.callableCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.callableOrBatchCallableCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.executions;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.preparedCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.preparedOrBatchPreparedCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.query;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.statement;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.statementCount;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.statementOrBatchStatementCount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class ProxyTestDataSourceTest {

    @Rule
    public DatabaseTestRule databaseTestRule = new DatabaseTestRule();

    @Test
    public void testStatementExecution() throws Exception {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.databaseTestRule.dataSource);

        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeQuery("SELECT id FROM emp");

        List<QueryExecution> queryExecutions = ds.getQueryExecutions();

        // simple assertion
        assertThat(queryExecutions).hasSize(1);
        assertThat(queryExecutions.get(0)).isInstanceOf(StatementExecution.class);
        StatementExecution se = (StatementExecution) queryExecutions.get(0);
        assertThat(se.getQuery()).isEqualTo("SELECT id FROM emp");

        // custom assertj assertion
        assertThat(queryExecutions).hasSize(1);
        assertThat(queryExecutions.get(0)).isStatement().asStatement().query().isEqualTo("SELECT id FROM emp");

        // hamcrest
        Assert.assertThat(queryExecutions, hasSize(1));
        Assert.assertThat(queryExecutions.get(0), is(statement()));
        Assert.assertThat(se, query(is("SELECT id FROM emp")));
    }

    @Test
    public void testStatementExecutionMultiple() throws Exception {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.databaseTestRule.dataSource);

        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT id FROM emp");
        stmt.execute("SELECT name FROM emp");
        stmt.execute("SELECT id FROM emp WHERE id = 1");

        List<QueryExecution> queryExecutions = ds.getQueryExecutions();

        // simple assertion
        assertThat(queryExecutions).hasSize(3);
        assertThat(queryExecutions.get(0)).isInstanceOf(StatementExecution.class);
        assertThat(queryExecutions.get(1)).isInstanceOf(StatementExecution.class);
        assertThat(queryExecutions.get(2)).isInstanceOf(StatementExecution.class);
        StatementExecution se;
        se = (StatementExecution) queryExecutions.get(0);
        assertThat(se.getQuery()).isEqualTo("SELECT id FROM emp");
        se = (StatementExecution) queryExecutions.get(1);
        assertThat(se.getQuery()).isEqualTo("SELECT name FROM emp");
        se = (StatementExecution) queryExecutions.get(2);
        assertThat(se.getQuery()).isEqualTo("SELECT id FROM emp WHERE id = 1");

        // assertj assertions
        assertThat(queryExecutions.get(0)).isStatement();
        assertThat(queryExecutions.get(1)).isStatement();
        assertThat(queryExecutions.get(2)).isStatement();
        assertThat(queryExecutions.get(0)).asStatement().query().isEqualTo("SELECT id FROM emp");
        assertThat(queryExecutions.get(1)).asStatement().query().isEqualTo("SELECT name FROM emp");
        assertThat(queryExecutions.get(2)).asStatement().query().isEqualTo("SELECT id FROM emp WHERE id = 1");

        // junit(hamcrest) assertions
        Assert.assertThat(ds, executions(0, is(statement())));
        Assert.assertThat(ds, executions(1, is(statement())));
        Assert.assertThat(ds, executions(2, is(statement())));
        Assert.assertThat((StatementExecution) queryExecutions.get(0), query(is("SELECT id FROM emp")));
        Assert.assertThat((StatementExecution) queryExecutions.get(1), query(is("SELECT name FROM emp")));
        Assert.assertThat((StatementExecution) queryExecutions.get(2), query(is("SELECT id FROM emp WHERE id = 1")));

    }

    @Test
    public void testGetExecutions() {
        QueryExecution stmt1 = mock(StatementExecution.class);
        QueryExecution stmt2 = mock(StatementExecution.class);
        QueryExecution stmt3 = mock(StatementExecution.class);
        QueryExecution batchStmt1 = mock(StatementBatchExecution.class);
        QueryExecution batchStmt2 = mock(StatementBatchExecution.class);
        QueryExecution batchStmt3 = mock(StatementBatchExecution.class);
        QueryExecution prepared1 = mock(PreparedExecution.class);
        QueryExecution prepared2 = mock(PreparedExecution.class);
        QueryExecution prepared3 = mock(PreparedExecution.class);
        QueryExecution batchPrepared1 = mock(PreparedBatchExecution.class);
        QueryExecution batchPrepared2 = mock(PreparedBatchExecution.class);
        QueryExecution batchPrepared3 = mock(PreparedBatchExecution.class);
        QueryExecution callable1 = mock(CallableExecution.class);
        QueryExecution callable2 = mock(CallableExecution.class);
        QueryExecution callable3 = mock(CallableExecution.class);
        QueryExecution batchCallable1 = mock(CallableBatchExecution.class);
        QueryExecution batchCallable2 = mock(CallableBatchExecution.class);
        QueryExecution batchCallable3 = mock(CallableBatchExecution.class);

        ProxyTestDataSource ds = new ProxyTestDataSource();
        ds.getQueryExecutions().addAll(Arrays.asList(stmt1, stmt2, stmt3));
        ds.getQueryExecutions().addAll(Arrays.asList(batchStmt1, batchStmt2, batchStmt3));
        ds.getQueryExecutions().addAll(Arrays.asList(prepared1, prepared2, prepared3));
        ds.getQueryExecutions().addAll(Arrays.asList(batchPrepared1, batchPrepared2, batchPrepared3));
        ds.getQueryExecutions().addAll(Arrays.asList(callable1, callable2, callable3));
        ds.getQueryExecutions().addAll(Arrays.asList(batchCallable1, batchCallable2, batchCallable3));

        assertThat(ds.getStatements()).hasSize(3)
                .containsSequence((StatementExecution) stmt1, (StatementExecution) stmt2, (StatementExecution) stmt3);
        assertThat(ds.getFirstStatement()).isSameAs(stmt1);
        assertThat(ds.getLastStatement()).isSameAs(stmt3);

        assertThat(ds.getBatchStatements()).hasSize(3)
                .containsSequence((StatementBatchExecution) batchStmt1, (StatementBatchExecution) batchStmt2, (StatementBatchExecution) batchStmt3);
        assertThat(ds.getFirstBatchStatement()).isSameAs(batchStmt1);
        assertThat(ds.getLastBatchStatement()).isSameAs(batchStmt3);

        assertThat(ds.getPrepareds()).hasSize(3)
                .containsSequence((PreparedExecution) prepared1, (PreparedExecution) prepared2, (PreparedExecution) prepared3);
        assertThat(ds.getFirstPrepared()).isSameAs(prepared1);
        assertThat(ds.getLastPrepared()).isSameAs(prepared3);

        assertThat(ds.getBatchPrepareds()).hasSize(3)
                .containsSequence((PreparedBatchExecution) batchPrepared1, (PreparedBatchExecution) batchPrepared2, (PreparedBatchExecution) batchPrepared3);
        assertThat(ds.getFirstBatchPrepared()).isSameAs(batchPrepared1);
        assertThat(ds.getLastBatchPrepared()).isSameAs(batchPrepared3);

        assertThat(ds.getCallables()).hasSize(3)
                .containsSequence((CallableExecution) callable1, (CallableExecution) callable2, (CallableExecution) callable3);
        assertThat(ds.getFirstCallable()).isSameAs(callable1);
        assertThat(ds.getLastCallable()).isSameAs(callable3);

        assertThat(ds.getBatchCallables()).hasSize(3)
                .containsSequence((CallableBatchExecution) batchCallable1, (CallableBatchExecution) batchCallable2, (CallableBatchExecution) batchCallable3);
        assertThat(ds.getFirstBatchCallable()).isSameAs(batchCallable1);
        assertThat(ds.getLastBatchCallable()).isSameAs(batchCallable3);

        // custom assertj assertion
        assertThat(ds).hasStatementCount(3);
        assertThat(ds).hasBatchStatementCount(3);
        assertThat(ds).hasPreparedCount(3);
        assertThat(ds).hasBatchPreparedCount(3);
        assertThat(ds).hasCallableCount(3);
        assertThat(ds).hasBatchCallableCount(3);
        assertThat(ds).hasStatementOrBatchStatementCount(6);
        assertThat(ds).hasPreparedOrBatchPreparedCount(6);
        assertThat(ds).hasCallableOrBatchCallableCount(6);


        // hamcrest
        Assert.assertThat(ds, statementCount(3));
        Assert.assertThat(ds, batchStatementCount(3));
        Assert.assertThat(ds, preparedCount(3));
        Assert.assertThat(ds, batchPreparedCount(3));
        Assert.assertThat(ds, callableCount(3));
        Assert.assertThat(ds, batchCallableCount(3));
        Assert.assertThat(ds, statementOrBatchStatementCount(6));
        Assert.assertThat(ds, preparedOrBatchPreparedCount(6));
        Assert.assertThat(ds, callableOrBatchCallableCount(6));
    }

    @Test
    public void testGetExecutionsWithEmptyExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource();

        assertThat(ds.getStatements()).hasSize(0);
        assertThat(ds.getFirstStatement()).isNull();
        assertThat(ds.getLastStatement()).isNull();

        assertThat(ds.getBatchStatements()).hasSize(0);
        assertThat(ds.getFirstBatchStatement()).isNull();
        assertThat(ds.getLastBatchStatement()).isNull();

        assertThat(ds.getPrepareds()).hasSize(0);
        assertThat(ds.getFirstPrepared()).isNull();
        assertThat(ds.getLastPrepared()).isNull();

        assertThat(ds.getBatchPrepareds()).hasSize(0);
        assertThat(ds.getFirstBatchPrepared()).isNull();
        assertThat(ds.getLastBatchPrepared()).isNull();

        assertThat(ds.getCallables()).hasSize(0);
        assertThat(ds.getFirstCallable()).isNull();
        assertThat(ds.getLastCallable()).isNull();

        assertThat(ds.getBatchCallables()).hasSize(0);
        assertThat(ds.getFirstBatchCallable()).isNull();
        assertThat(ds.getLastBatchCallable()).isNull();

    }

    @Test
    public void reset() throws Exception {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.databaseTestRule.dataSource);

        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeQuery("SELECT id FROM emp");

        List<QueryExecution> queryExecutions = ds.getQueryExecutions();
        assertThat(queryExecutions).hasSize(1);

        ds.reset();

        assertThat(ds.getQueryExecutions()).isEmpty();
    }

}
