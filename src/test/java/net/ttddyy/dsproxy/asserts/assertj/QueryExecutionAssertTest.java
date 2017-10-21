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

        DataSourceAssertAssertions.assertThat(qe).isSuccess();

        try {
            DataSourceAssertAssertions.assertThat(qe).isFailure();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Failure execution> but was: <Successful execution>\n");
        }

    }

    @Test
    public void testIsFailure() {
        QueryExecution qe = mock(QueryExecution.class);
        given(qe.isSuccess()).willReturn(false);

        DataSourceAssertAssertions.assertThat(qe).isFailure();

        try {
            DataSourceAssertAssertions.assertThat(qe).isSuccess();
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

        DataSourceAssertAssertions.assertThat(qe).isBatch();

        // batch=false
        qe = mock(QueryExecution.class);
        given(qe.isBatch()).willReturn(false);
        try {
            DataSourceAssertAssertions.assertThat(qe).isBatch();
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

        DataSourceAssertAssertions.assertThat(se).isStatement();
        DataSourceAssertAssertions.assertThat(sbe).isBatchStatement();
        DataSourceAssertAssertions.assertThat(se).isStatementOrBatchStatement();
        DataSourceAssertAssertions.assertThat(sbe).isStatementOrBatchStatement();

        try {
            DataSourceAssertAssertions.assertThat(pe).isStatement();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <STATEMENT> but was: <PREPARED>\n");
        }
        try {
            DataSourceAssertAssertions.assertThat(pe).isBatchStatement();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <BATCH STATEMENT> but was: <PREPARED>\n");
        }
        try {
            DataSourceAssertAssertions.assertThat(pe).isStatementOrBatchStatement();
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

        DataSourceAssertAssertions.assertThat(pe).isPrepared();
        DataSourceAssertAssertions.assertThat(pbe).isBatchPrepared();
        DataSourceAssertAssertions.assertThat(pe).isPreparedOrBatchPrepared();
        DataSourceAssertAssertions.assertThat(pbe).isPreparedOrBatchPrepared();

        try {
            DataSourceAssertAssertions.assertThat(se).isPrepared();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <PREPARED> but was: <STATEMENT>\n");
        }
        try {
            DataSourceAssertAssertions.assertThat(se).isBatchPrepared();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <BATCH PREPARED> but was: <STATEMENT>\n");
        }
        try {
            DataSourceAssertAssertions.assertThat(se).isPreparedOrBatchPrepared();
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

        DataSourceAssertAssertions.assertThat(ce).isCallable();
        DataSourceAssertAssertions.assertThat(cbe).isBatchCallable();
        DataSourceAssertAssertions.assertThat(ce).isCallableOrBatchCallable();
        DataSourceAssertAssertions.assertThat(cbe).isCallableOrBatchCallable();

        try {
            DataSourceAssertAssertions.assertThat(se).isCallable();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <CALLABLE> but was: <STATEMENT>\n");
        }
        try {
            DataSourceAssertAssertions.assertThat(se).isBatchCallable();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <BATCH CALLABLE> but was: <STATEMENT>\n");
        }
        try {
            DataSourceAssertAssertions.assertThat(se).isCallableOrBatchCallable();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <CALLABLE or BATCH CALLABLE> but was: <STATEMENT>\n");
        }
    }
}
