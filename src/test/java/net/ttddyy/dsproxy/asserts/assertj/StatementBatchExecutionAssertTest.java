package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.QueryType;
import net.ttddyy.dsproxy.asserts.StatementBatchExecution;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Tadaya Tsuyukubo
 */
public class StatementBatchExecutionAssertTest {
    private StatementBatchExecution sbe;
    private StatementBatchExecutionAssert sbeAssert;

    @Before
    public void setUp() {
        this.sbe = new StatementBatchExecution();
        this.sbeAssert = new StatementBatchExecutionAssert(this.sbe);
    }

    @Test
    public void testIsSuccess() {
        // success case
        this.sbe.setSuccess(true);
        this.sbeAssert.isSuccess();

        // failure case
        this.sbe.setSuccess(false);
        try {
            this.sbeAssert.isSuccess();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Successful execution> but was: <Failure execution>\n");
        }

    }

    @Test
    public void testIsFailure() {
        // success case
        this.sbe.setSuccess(false);
        this.sbeAssert.isFailure();

        // failure case
        this.sbe.setSuccess(true);
        try {
            this.sbeAssert.isFailure();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Failure execution> but was: <Successful execution>\n");
        }
    }

    @Test
    public void testHasBatchSize() {
        List<String> entries = new ArrayList<>();
        entries.addAll(Arrays.asList("a", "b", "c"));

        StatementBatchExecution sbe = mock(StatementBatchExecution.class);
        given(sbe.getQueries()).willReturn(entries);

        DataSourceAssertAssertions.assertThat(sbe).hasBatchSize(3);

        try {
            DataSourceAssertAssertions.assertThat(sbe).hasBatchSize(1);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected batch size:<1> but was:<3> in batch statement executions\n");
        }
    }

    @Test
    public void testQueries() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("foo");
        sbe.getQueries().add("bar");
        sbe.getQueries().add("baz");

        DataSourceAssertAssertions.assertThat(sbe).queries().contains("bar");

        try {
            DataSourceAssertAssertions.assertThat(sbe).queries().contains("WRONG");
            fail("exception should be thrown");
        } catch (AssertionError e) {
        }
    }

    @Test
    public void testQuery() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("foo");
        sbe.getQueries().add("bar");
        sbe.getQueries().add("baz");

        DataSourceAssertAssertions.assertThat(sbe).query(1).isEqualTo("bar");

        try {
            DataSourceAssertAssertions.assertThat(sbe).query(3);
            fail("exception should be thrown");
        } catch (Throwable e) {
            assertThat(e).isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Test
    public void testContains() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("SELECT");
        sbe.getQueries().add("INSERT");
        sbe.getQueries().add("SELECT");

        DataSourceAssertAssertions.assertThat(sbe).contains(QueryType.SELECT, 0);
        DataSourceAssertAssertions.assertThat(sbe).contains(QueryType.INSERT, 1);
        DataSourceAssertAssertions.assertThat(sbe).contains(QueryType.SELECT, atIndex(2));

        // wrong type
        try {
            DataSourceAssertAssertions.assertThat(sbe).contains(QueryType.DELETE, 0);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected query type:<DELETE> but was:<SELECT> at index:<0>\n");
        }

        // out of index
        try {
            DataSourceAssertAssertions.assertThat(sbe).contains(QueryType.DELETE, 4);
            fail("exception should be thrown");
        } catch (Throwable e) {
            assertThat(e).isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Test
    public void testHasQueryCount() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("SELECT");
        sbe.getQueries().add("SELECT");
        sbe.getQueries().add("SELECT");
        sbe.getQueries().add("INSERT");
        sbe.getQueries().add("INSERT");
        sbe.getQueries().add("UPDATE");
        sbe.getQueries().add("OTHER");

        DataSourceAssertAssertions.assertThat(sbe).hasSelectCount(3);
        DataSourceAssertAssertions.assertThat(sbe).hasInsertCount(2);
        DataSourceAssertAssertions.assertThat(sbe).hasUpdateCount(1);
        DataSourceAssertAssertions.assertThat(sbe).hasDeleteCount(0);
        DataSourceAssertAssertions.assertThat(sbe).hasOtherCount(1);
        DataSourceAssertAssertions.assertThat(sbe).hasQueryCount(QueryType.INSERT, 2);

        // wrong type
        try {
            DataSourceAssertAssertions.assertThat(sbe).hasSelectCount(10);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expected SELECT count:<10> but was:<3> in:\n" +
                    "<select=3, insert=2, update=1, delete=0, other=1>");
        }
    }

}
