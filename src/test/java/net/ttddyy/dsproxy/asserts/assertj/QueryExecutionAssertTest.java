package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.CallableBatchExecution;
import net.ttddyy.dsproxy.asserts.CallableExecution;
import net.ttddyy.dsproxy.asserts.PreparedBatchExecution;
import net.ttddyy.dsproxy.asserts.PreparedExecution;
import net.ttddyy.dsproxy.asserts.QueryExecution;
import net.ttddyy.dsproxy.asserts.StatementBatchExecution;
import net.ttddyy.dsproxy.asserts.StatementExecution;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Tadaya Tsuyukubo
 */
public class QueryExecutionAssertTest {

    @Test
    public void testIsSuccess() {
        QueryExecution qe = mock(QueryExecution.class);
        given(qe.isSuccess()).willReturn(true);

        DataSourceProxyAssertions.assertThat(qe).isSuccess();

        try {
            DataSourceProxyAssertions.assertThat(qe).isFailure();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Failure execution> but was: <Successful execution>\n");
        }

    }

    @Test
    public void testIsFailure() {
        QueryExecution qe = mock(QueryExecution.class);
        given(qe.isSuccess()).willReturn(false);

        DataSourceProxyAssertions.assertThat(qe).isFailure();

        try {
            DataSourceProxyAssertions.assertThat(qe).isSuccess();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Successful execution> but was: <Failure execution>\n");
        }
    }

    @Test
    public void testIsBatch() {
        // batch=true
        QueryExecution qe = mock(QueryExecution.class);
        given(qe.isBatch()).willReturn(true);

        DataSourceProxyAssertions.assertThat(qe).isBatch();

        // batch=false
        qe = mock(QueryExecution.class);
        given(qe.isBatch()).willReturn(false);
        try {
            DataSourceProxyAssertions.assertThat(qe).isBatch();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Batch Execution> but was: <Not Batch Execution>\n");
        }
    }

    @Test
    public void testIsStatement() {
        QueryExecution se = new StatementExecution();
        QueryExecution sbe = new StatementBatchExecution();
        QueryExecution pe = new PreparedExecution();

        DataSourceProxyAssertions.assertThat(se).isStatement();
        DataSourceProxyAssertions.assertThat(sbe).isBatchStatement();
        DataSourceProxyAssertions.assertThat(se).isStatementOrBatchStatement();
        DataSourceProxyAssertions.assertThat(sbe).isStatementOrBatchStatement();

        try {
            DataSourceProxyAssertions.assertThat(pe).isStatement();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <STATEMENT> but was: <PREPARED>\n");
        }
        try {
            DataSourceProxyAssertions.assertThat(pe).isBatchStatement();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <BATCH STATEMENT> but was: <PREPARED>\n");
        }
        try {
            DataSourceProxyAssertions.assertThat(pe).isStatementOrBatchStatement();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <STATEMENT or BATCH STATEMENT> but was: <PREPARED>\n");
        }
    }

    @Test
    public void testIsPrepared() {
        QueryExecution pe = new PreparedExecution();
        QueryExecution pbe = new PreparedBatchExecution();
        QueryExecution se = new StatementExecution();

        DataSourceProxyAssertions.assertThat(pe).isPrepared();
        DataSourceProxyAssertions.assertThat(pbe).isBatchPrepared();
        DataSourceProxyAssertions.assertThat(pe).isPreparedOrBatchPrepared();
        DataSourceProxyAssertions.assertThat(pbe).isPreparedOrBatchPrepared();

        try {
            DataSourceProxyAssertions.assertThat(se).isPrepared();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <PREPARED> but was: <STATEMENT>\n");
        }
        try {
            DataSourceProxyAssertions.assertThat(se).isBatchPrepared();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <BATCH PREPARED> but was: <STATEMENT>\n");
        }
        try {
            DataSourceProxyAssertions.assertThat(se).isPreparedOrBatchPrepared();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <PREPARED or BATCH PREPARED> but was: <STATEMENT>\n");
        }
    }

    @Test
    public void testIsCallable() {
        QueryExecution ce = new CallableExecution();
        QueryExecution cbe = new CallableBatchExecution();
        QueryExecution se = new StatementExecution();

        DataSourceProxyAssertions.assertThat(ce).isCallable();
        DataSourceProxyAssertions.assertThat(cbe).isBatchCallable();
        DataSourceProxyAssertions.assertThat(ce).isCallableOrBatchCallable();
        DataSourceProxyAssertions.assertThat(cbe).isCallableOrBatchCallable();

        try {
            DataSourceProxyAssertions.assertThat(se).isCallable();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <CALLABLE> but was: <STATEMENT>\n");
        }
        try {
            DataSourceProxyAssertions.assertThat(se).isBatchCallable();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <BATCH CALLABLE> but was: <STATEMENT>\n");
        }
        try {
            DataSourceProxyAssertions.assertThat(se).isCallableOrBatchCallable();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <CALLABLE or BATCH CALLABLE> but was: <STATEMENT>\n");
        }
    }
}
