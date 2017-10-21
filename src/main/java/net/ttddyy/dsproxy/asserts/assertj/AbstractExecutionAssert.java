package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.QueryExecution;
import org.assertj.core.api.AbstractAssert;

/**
 * Shared asserts methods.
 *
 * @author Tadaya Tsuyukubo
 * @see QueryExecutionAssert
 * @see StatementExecutionAssert
 * @see StatementBatchExecutionAssert
 * @see PreparedExecutionAssert
 * @see PreparedBatchExecutionAssert
 * @see CallableExecutionAssert
 * @see CallableBatchExecutionAssert
 * @since 1.0
 */
public abstract class AbstractExecutionAssert<S extends AbstractAssert<S, A>, A extends QueryExecution> extends AbstractAssert<S, A> {
    public AbstractExecutionAssert(A actual, Class<?> selfType) {
        super(actual, selfType);
    }

    protected void isExecutionSuccess() {
        if (!this.actual.isSuccess()) {
            failWithMessage("%nExpecting: <%s> but was: <%s>%n", "Successful execution", "Failure execution");
        }
    }

    protected void isExecutionFailure() {
        if (this.actual.isSuccess()) {
            failWithMessage("%nExpecting: <%s> but was: <%s>%n", "Failure execution", "Successful execution");
        }
    }

}
