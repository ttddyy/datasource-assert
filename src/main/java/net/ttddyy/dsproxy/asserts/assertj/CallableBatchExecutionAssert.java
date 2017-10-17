package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.CallableBatchExecution;
import net.ttddyy.dsproxy.asserts.CallableBatchExecutionEntry;
import net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters;
import net.ttddyy.dsproxy.asserts.assertj.helper.BatchExecutionEntryAsserts;
import net.ttddyy.dsproxy.asserts.assertj.helper.ExecutionParameterAsserts;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;

/**
 * @author Tadaya Tsuyukubo
 */
// TODO: should this extend AbstractAssert??
public class CallableBatchExecutionAssert extends AbstractExecutionAssert<CallableBatchExecutionAssert, CallableBatchExecution> {

    private BatchExecutionEntryAsserts batchAssert = new BatchExecutionEntryAsserts(this.info);
    private ExecutionParameterAsserts parameterAssert = new ExecutionParameterAsserts(this.info);

    public CallableBatchExecutionAssert(CallableBatchExecution actual) {
        super(actual, CallableBatchExecutionAssert.class);
    }

    public CallableBatchExecutionAssert isSuccess() {
        isExecutionSuccess();
        return this;
    }

    public CallableBatchExecutionAssert isFailure() {
        isExecutionFailure();
        return this;
    }

    public CallableBatchExecutionAssert hasBatchSize(int batchSize) {
        this.batchAssert.assertBatchSize(this.actual, batchSize, "callable");
        return this;
    }


    public CallableBatchExecutionAssert batch(int batchIndex, ExecutionParameters params) {

        this.batchAssert.assertBatchExecutionEntry(this.actual, batchIndex, CallableBatchExecutionEntry.class);

        // entry is validated to be the one for callable
        CallableBatchExecutionEntry batchEntry = (CallableBatchExecutionEntry) this.actual.getBatchExecutionEntries().get(batchIndex);
        this.parameterAssert.assertParameterKeys(batchEntry, params, true);


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
