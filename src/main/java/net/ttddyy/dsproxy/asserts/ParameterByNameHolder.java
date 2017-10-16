package net.ttddyy.dsproxy.asserts;

import java.util.List;
import java.util.Map;

/**
 * Represent an execution that hold parameter by name.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public interface ParameterByNameHolder extends ParameterHolder {

    Map<String, Object> getSetParamsByName();

    Map<String, Integer> getSetNullParamsByName();

    /**
     * Keys of parameters.
     * Includes both set_param and set_null parameters.
     *
     * @return String keys.
     */
    List<String> getParamNames();

}
