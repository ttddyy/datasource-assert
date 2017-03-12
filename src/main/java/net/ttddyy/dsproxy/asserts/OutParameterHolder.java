package net.ttddyy.dsproxy.asserts;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public interface OutParameterHolder extends ParameterHolder {

    SortedSet<ParameterKeyValue> getOutParams();

    Map<Integer, Object> getOutParamsByIndex();

    Map<String, Object> getOutParamsByName();

    /**
     * Keys of out parameters.
     *
     * @return Integer keys.
     */
    List<Integer> getOutParamIndexes();

    /**
     * Keys of out parameters.
     *
     * @return String keys.
     */
    List<String> getOutParamNames();


}
