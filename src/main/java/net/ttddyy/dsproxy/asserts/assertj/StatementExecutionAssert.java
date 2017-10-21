package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.QueryType;
import net.ttddyy.dsproxy.asserts.StatementExecution;
import net.ttddyy.dsproxy.listener.QueryUtils;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class StatementExecutionAssert extends AbstractExecutionAssert<StatementExecutionAssert, StatementExecution> {

    public StatementExecutionAssert(StatementExecution actual) {
        super(actual, StatementExecutionAssert.class);
    }

    public StatementExecutionAssert isSuccess() {
        isExecutionSuccess();
        return this;
    }

    public StatementExecutionAssert isFailure() {
        isExecutionFailure();
        return this;
    }

    public AbstractCharSequenceAssert<?, String> query() {
        return Assertions.assertThat(this.actual.getQuery());
    }

    public StatementExecutionAssert isSelect() {
        hasQueryType(QueryType.SELECT);
        return this;
    }

    public StatementExecutionAssert isInsert() {
        hasQueryType(QueryType.INSERT);
        return this;
    }

    public StatementExecutionAssert isUpdate() {
        hasQueryType(QueryType.UPDATE);
        return this;
    }

    public StatementExecutionAssert isDelete() {
        hasQueryType(QueryType.DELETE);
        return this;
    }

    public StatementExecutionAssert isOther() {
        hasQueryType(QueryType.OTHER);
        return this;
    }

    public StatementExecutionAssert hasQueryType(QueryType queryType) {
        String query = this.actual.getQuery();
        QueryType actualType = QueryUtils.getQueryType(query);
        if (actualType != queryType) {
            failWithMessage("%nExpected query type:<%s> but was:<%s>%n", queryType, actualType);
        }
        return this;
    }

}
