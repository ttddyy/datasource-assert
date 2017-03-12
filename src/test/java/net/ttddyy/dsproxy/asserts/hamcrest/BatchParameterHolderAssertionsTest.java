package net.ttddyy.dsproxy.asserts.hamcrest;

import net.ttddyy.dsproxy.asserts.BatchExecutionEntry;
import net.ttddyy.dsproxy.asserts.BatchParameterHolder;
import net.ttddyy.dsproxy.asserts.ParameterByIndexHolder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.paramIndexes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class BatchParameterHolderAssertionsTest {

    @Test
    public void testBatchSize() {
        BatchExecutionEntry entry = mock(BatchExecutionEntry.class);

        ArrayList<BatchExecutionEntry> entries = new ArrayList<BatchExecutionEntry>();
        entries.add(entry);

        BatchParameterHolder holder = mock(BatchParameterHolder.class);
        given(holder.getBatchExecutionEntries()).willReturn(entries);

        Assert.assertThat(holder, BatchParameterHolderAssertions.batchSize(1));
    }

    @Test
    public void testBatchSizeFailure() {
        BatchParameterHolder holder = mock(BatchParameterHolder.class);
        given(holder.getBatchExecutionEntries()).willReturn(new ArrayList<BatchExecutionEntry>());  // empty list


        try {
            Assert.assertThat(holder, BatchParameterHolderAssertions.batchSize(1));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: batchSize <1>\n     but: batchSize was <0>");
        }
    }

    @Test
    public void testBatch() {
        ParameterByIndexHolder entry = mock(ParameterByIndexHolder.class, withSettings().extraInterfaces(BatchExecutionEntry.class));
        given(entry.getParamIndexes()).willReturn(Arrays.asList(100));
        List<BatchExecutionEntry> entries = Arrays.asList((BatchExecutionEntry) entry);

        BatchParameterHolder holder = mock(BatchParameterHolder.class);
        given(holder.getBatchExecutionEntries()).willReturn(entries);


        Assert.assertThat(holder, BatchParameterHolderAssertions.batch(0, paramIndexes(hasItem(100))));
    }

    @Test
    public void testBatchOutOfIndex() {
        BatchParameterHolder holder = mock(BatchParameterHolder.class);
        given(holder.getBatchExecutionEntries()).willReturn(new ArrayList<BatchExecutionEntry>());  // empty list


        try {
            Assert.assertThat(holder, BatchParameterHolderAssertions.batch(10, paramIndexes(hasItem(100))));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: batch[10] exists\n     but: batch[] size was 0");
        }

    }

    @Test
    public void testBatchUnmatchMessage() {
        ParameterByIndexHolder entry = mock(ParameterByIndexHolder.class, withSettings().extraInterfaces(BatchExecutionEntry.class));
        given(entry.getParamIndexes()).willReturn(Arrays.asList(100));
        List<BatchExecutionEntry> entries = Arrays.asList((BatchExecutionEntry) entry);

        BatchParameterHolder holder = mock(BatchParameterHolder.class);
        given(holder.getBatchExecutionEntries()).willReturn(entries);


        try {
            Assert.assertThat(holder, BatchParameterHolderAssertions.batch(0, paramIndexes(hasItem(50))));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: batch[0] parameter indexes as a collection containing <50>\n     but: batch[0] mismatches were: [was <100>]");
        }

    }
}
