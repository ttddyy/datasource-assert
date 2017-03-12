package net.ttddyy.dsproxy.asserts.hamcrest;

import net.ttddyy.dsproxy.asserts.CallableBatchExecution;
import net.ttddyy.dsproxy.asserts.CallableExecution;
import net.ttddyy.dsproxy.asserts.PreparedBatchExecution;
import net.ttddyy.dsproxy.asserts.PreparedExecution;
import net.ttddyy.dsproxy.asserts.QueryExecution;
import net.ttddyy.dsproxy.asserts.StatementBatchExecution;
import net.ttddyy.dsproxy.asserts.StatementExecution;
import org.junit.Assert;
import org.junit.Test;

import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.batch;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.batchCallable;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.batchPrepared;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.batchStatement;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.callable;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.callableOrBatchCallable;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.failure;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.prepared;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.preparedOrBatchPrepared;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.statement;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.statementOrBatchStatement;
import static net.ttddyy.dsproxy.asserts.hamcrest.QueryExecutionAssertions.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class QueryExecutionAssertionsTest {

    @Test
    public void testSuccess() {
        QueryExecution qe = mock(QueryExecution.class);
        when(qe.isSuccess()).thenReturn(true);

        Assert.assertThat(qe, success());
    }

    @Test
    public void testSuccessWithFailureMessage() {
        QueryExecution qe = mock(QueryExecution.class);
        when(qe.isSuccess()).thenReturn(false);


        try {
            Assert.assertThat(qe, success());
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: success\n     but: was failure");
        }
    }


    @Test
    public void testFailure() {
        QueryExecution qe = mock(QueryExecution.class);
        when(qe.isSuccess()).thenReturn(false);

        Assert.assertThat(qe, failure());
    }

    @Test
    public void testFailWithFailureMessage() {
        QueryExecution qe = mock(QueryExecution.class);
        when(qe.isSuccess()).thenReturn(true);

        try {
            Assert.assertThat(qe, failure());
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: failure\n     but: was success");
        }

    }


    @Test
    public void testExecutions() {
        StatementExecution se = new StatementExecution();
        StatementBatchExecution sbe = new StatementBatchExecution();
        PreparedExecution pe = new PreparedExecution();
        PreparedBatchExecution pbe = new PreparedBatchExecution();
        CallableExecution ce = new CallableExecution();
        CallableBatchExecution cbe = new CallableBatchExecution();

        Assert.assertThat(se, statement());
        Assert.assertThat(sbe, batchStatement());
        Assert.assertThat(se, statementOrBatchStatement());
        Assert.assertThat(sbe, statementOrBatchStatement());

        Assert.assertThat(pe, prepared());
        Assert.assertThat(pbe, batchPrepared());
        Assert.assertThat(pe, preparedOrBatchPrepared());
        Assert.assertThat(pbe, preparedOrBatchPrepared());

        Assert.assertThat(ce, callable());
        Assert.assertThat(cbe, batchCallable());
        Assert.assertThat(ce, callableOrBatchCallable());
        Assert.assertThat(cbe, callableOrBatchCallable());

        Assert.assertThat(sbe, batch());
        Assert.assertThat(pbe, batch());
        Assert.assertThat(cbe, batch());
    }

}
