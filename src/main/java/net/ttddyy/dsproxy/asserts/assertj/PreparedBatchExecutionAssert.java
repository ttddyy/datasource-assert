package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.PreparedBatchExecution;
import net.ttddyy.dsproxy.asserts.PreparedBatchExecutionEntry;
import net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters;
import net.ttddyy.dsproxy.asserts.assertj.helper.BatchExecutionEntryAsserts;
import net.ttddyy.dsproxy.asserts.assertj.helper.ExecutionParameterAsserts;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;

/**
 * @author Tadaya Tsuyukubo
 */
public class PreparedBatchExecutionAssert extends AbstractExecutionAssert<PreparedBatchExecutionAssert, PreparedBatchExecution> {

    private BatchExecutionEntryAsserts batchAssert = new BatchExecutionEntryAsserts(this.info);
    private ExecutionParameterAsserts parameterAssert = new ExecutionParameterAsserts(this.info);

    public PreparedBatchExecutionAssert(PreparedBatchExecution actual) {
        super(actual, PreparedBatchExecutionAssert.class);
    }

    public PreparedBatchExecutionAssert isSuccess() {
        isExecutionSuccess();
        return this;
    }

    public PreparedBatchExecutionAssert isFailure() {
        isExecutionFailure();
        return this;
    }

    public PreparedBatchExecutionAssert hasBatchSize(int batchSize) {
        this.batchAssert.assertBatchSize(this.actual, batchSize, "prepared");
        return this;
    }


    public PreparedBatchExecutionAssert batch(int batchIndex, ExecutionParameters params) {

        this.batchAssert.assertBatchExecutionEntry(this.actual, batchIndex, PreparedBatchExecutionEntry.class);

        // entry is validated to be the one for prepared
        PreparedBatchExecutionEntry batchEntry = (PreparedBatchExecutionEntry) this.actual.getBatchExecutionEntries().get(batchIndex);
        this.parameterAssert.assertParameterKeys(batchEntry, params, false);

        if (ExecutionParameters.ExecutionParametersType.CONTAINS_KEYS_ONLY == params.getType()) {
            return this;  // only check keys
        }

        // validate key-value pairs
        this.parameterAssert.assertExecutionParameters(batchEntry, params);

        return this;
    }

    public AbstractCharSequenceAssert<?, String> query() {
        return Assertions.assertThat(this.actual.getQuery());
    }

}
