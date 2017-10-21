package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.CallableExecution;
import net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter;
import net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameters;
import net.ttddyy.dsproxy.asserts.assertj.helper.ExecutionParameterAsserts;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;

import java.sql.SQLType;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class CallableExecutionAssert extends AbstractExecutionAssert<CallableExecutionAssert, CallableExecution> {

    private ExecutionParameterAsserts parameterAsserts = new ExecutionParameterAsserts(this.info);

    public CallableExecutionAssert(CallableExecution actual) {
        super(actual, CallableExecutionAssert.class);
    }

    public CallableExecutionAssert isSuccess() {
        this.isExecutionSuccess();
        return this;
    }

    public CallableExecutionAssert isFailure() {
        this.isExecutionFailure();
        return this;
    }

    public CallableExecutionAssert containsParam(int paramIndex, Object value) {
        containsParams(ExecutionParameter.param(paramIndex, value));
        return this;
    }

    public CallableExecutionAssert containsParam(String paramName, Object value) {
        containsParams(ExecutionParameter.param(paramName, value));
        return this;
    }

    public CallableExecutionAssert containsNullParam(int index, int sqlType) {
        containsParams(ExecutionParameter.nullParam(index, sqlType));
        return this;
    }

    public CallableExecutionAssert containsNullParam(int index) {
        containsParams(ExecutionParameter.nullParam(index));
        return this;
    }

    public CallableExecutionAssert containsNullParam(String name, int sqlType) {
        containsParams(ExecutionParameter.nullParam(name, sqlType));
        return this;
    }

    public CallableExecutionAssert containsNullParam(String name) {
        containsParams(ExecutionParameter.nullParam(name));
        return this;
    }

    public CallableExecutionAssert containsOutParam(int paramIndex, int sqlType) {
        containsParams(ExecutionParameter.outParam(paramIndex, sqlType));
        return this;
    }

    public CallableExecutionAssert containsOutParam(int paramIndex, SQLType sqlType) {
        containsParams(ExecutionParameter.outParam(paramIndex, sqlType));
        return this;
    }

    public CallableExecutionAssert containsOutParam(String paramName, int sqlType) {
        containsParams(ExecutionParameter.outParam(paramName, sqlType));
        return this;
    }

    public CallableExecutionAssert containsOutParam(String paramName, SQLType sqlType) {
        containsParams(ExecutionParameter.outParam(paramName, sqlType));
        return this;
    }


    public CallableExecutionAssert containsParams(ExecutionParameter... params) {
        ExecutionParameters executionParameters = ExecutionParameters.containsParams(params);
        this.parameterAsserts.assertParameterKeys(this.actual, executionParameters, true);
        this.parameterAsserts.assertExecutionParameters(this.actual, executionParameters);
        return this;
    }

    public CallableExecutionAssert containsParamsExactly(ExecutionParameter... params) {
        ExecutionParameters executionParameters = ExecutionParameters.containsParamsExactly(params);
        this.parameterAsserts.assertParameterKeys(this.actual, executionParameters, true);
        this.parameterAsserts.assertExecutionParameters(this.actual, executionParameters);
        return this;
    }

    public CallableExecutionAssert containsParamKey(Object paramKey) {
        containsParamKeys(paramKey);
        return this;
    }

    public CallableExecutionAssert containsParamKeys(Object... paramKeys) {
        ExecutionParameters executionParameters = ExecutionParameters.containsParamKeys(paramKeys);
        this.parameterAsserts.assertParameterKeys(this.actual, executionParameters, true);
        return this;
    }

    public CallableExecutionAssert containsParamIndex(int paramIndex) {
        containsParamIndexes(paramIndex);
        return this;
    }

    public CallableExecutionAssert containsParamIndexes(int... paramIndexes) {
        ExecutionParameters executionParameters = ExecutionParameters.containsParamIndexes(paramIndexes);
        this.parameterAsserts.assertParameterKeys(this.actual, executionParameters, true);
        return this;
    }

    public CallableExecutionAssert containsParamName(String paramName) {
        containsParamNames(paramName);
        return this;
    }

    public CallableExecutionAssert containsParamNames(String... paramNames) {
        ExecutionParameters executionParameters = ExecutionParameters.containsParamNames(paramNames);
        this.parameterAsserts.assertParameterKeys(this.actual, executionParameters, true);
        return this;
    }

    public AbstractCharSequenceAssert<?, String> query() {
        return Assertions.assertThat(this.actual.getQuery());
    }

}
