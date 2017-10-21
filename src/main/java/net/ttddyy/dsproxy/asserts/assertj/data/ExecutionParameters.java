package net.ttddyy.dsproxy.asserts.assertj.data;

import net.ttddyy.dsproxy.proxy.ParameterKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class ExecutionParameters {

    public enum ExecutionParametersType {
        CONTAINS, CONTAINS_EXACTLY, CONTAINS_KEYS_ONLY
    }

    public static ExecutionParameters containsParams(ExecutionParameter... params) {
        ExecutionParameters executionParameters = new ExecutionParameters();
        executionParameters.type = ExecutionParametersType.CONTAINS;
        executionParameters.parameters.addAll(Arrays.asList(params));
        return executionParameters;
    }

    public static ExecutionParameters containsParamsExactly(ExecutionParameter... params) {
        ExecutionParameters executionParameters = new ExecutionParameters();
        executionParameters.type = ExecutionParametersType.CONTAINS_EXACTLY;
        executionParameters.parameters.addAll(Arrays.asList(params));
        return executionParameters;

    }

    public static ExecutionParameters containsParamKeys(Object... paramKeys) {

        List<ExecutionParameter> params = new ArrayList<>();

        for (Object rawParamKey : paramKeys) {
            ExecutionParameter param;
            if (rawParamKey instanceof Integer) {
                param = new ExecutionParameter.ParamKeyOnlyExecution(new ParameterKey((Integer) rawParamKey));
            } else if (rawParamKey instanceof String) {
                param = new ExecutionParameter.ParamKeyOnlyExecution(new ParameterKey((String) rawParamKey));
            } else {
                throw new IllegalArgumentException("param key should be int or String");
            }
            params.add(param);
        }


        ExecutionParameters executionParameters = new ExecutionParameters();
        executionParameters.type = ExecutionParametersType.CONTAINS_KEYS_ONLY;
        executionParameters.parameters.addAll(params);
        return executionParameters;
    }

    public static ExecutionParameters containsParamIndexes(int... paramIndexes) {

        List<ExecutionParameter> params = new ArrayList<>();
        for (int paramIndex : paramIndexes) {
            ExecutionParameter param = new ExecutionParameter.ParamKeyOnlyExecution(new ParameterKey(paramIndex));
            params.add(param);
        }

        ExecutionParameters executionParameters = new ExecutionParameters();
        executionParameters.type = ExecutionParametersType.CONTAINS_KEYS_ONLY;
        executionParameters.parameters.addAll(params);
        return executionParameters;
    }

    public static ExecutionParameters containsParamNames(String... paramNames) {

        List<ExecutionParameter> params = new ArrayList<>();
        for (String paramName : paramNames) {
            ExecutionParameter param = new ExecutionParameter.ParamKeyOnlyExecution(new ParameterKey(paramName));
            params.add(param);
        }

        ExecutionParameters executionParameters = new ExecutionParameters();
        executionParameters.type = ExecutionParametersType.CONTAINS_KEYS_ONLY;
        executionParameters.parameters.addAll(params);
        return executionParameters;

    }

    private List<ExecutionParameter> parameters = new ArrayList<>();
    private ExecutionParametersType type;


    public List<ExecutionParameter> getParameters() {
        return parameters;
    }

    public ExecutionParametersType getType() {
        return type;
    }
}
