package net.ttddyy.dsproxy.asserts;

import java.util.SortedSet;

/**
 * Represent execution that hold parameters.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public interface ParameterHolder {

    SortedSet<ParameterKeyValue> getAllParameters();

    SortedSet<ParameterKeyValue> getSetParams();

    SortedSet<ParameterKeyValue> getSetNullParams();

}
