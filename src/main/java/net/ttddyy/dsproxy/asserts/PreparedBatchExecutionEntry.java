package net.ttddyy.dsproxy.asserts;

import net.ttddyy.dsproxy.proxy.ParameterKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static net.ttddyy.dsproxy.asserts.ParameterKeyValueUtils.filterBy;
import static net.ttddyy.dsproxy.asserts.ParameterKeyValueUtils.filterByKeyType;
import static net.ttddyy.dsproxy.asserts.ParameterKeyValueUtils.toKeyIndexMap;

/**
 * Represent batch entry of {@link java.sql.PreparedStatement}.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class PreparedBatchExecutionEntry implements BatchExecutionEntry, ParameterByIndexHolder {

    private SortedSet<ParameterKeyValue> parameters = new TreeSet<>();

    @Override
    public SortedSet<ParameterKeyValue> getAllParameters() {
        return this.parameters;
    }

    @Override
    public SortedSet<ParameterKeyValue> getSetParams() {
        return filterBy(this.parameters, ParameterKeyValue.OperationType.SET_PARAM);
    }

    @Override
    public SortedSet<ParameterKeyValue> getSetNullParams() {
        return filterBy(this.parameters, ParameterKeyValue.OperationType.SET_NULL);
    }


    @Override
    public Map<Integer, Object> getSetParamsByIndex() {
        return toKeyIndexMap(filterByKeyType(getSetParams(), ParameterKey.ParameterKeyType.BY_INDEX));
    }

    @Override
    public Map<Integer, Integer> getSetNullParamsByIndex() {
        return toKeyIndexMap(filterByKeyType(getSetNullParams(), ParameterKey.ParameterKeyType.BY_INDEX));
    }

    @Override
    public List<Integer> getParamIndexes() {
        List<Integer> indexes = new ArrayList<>();
        indexes.addAll(getSetParamsByIndex().keySet());
        indexes.addAll(getSetNullParamsByIndex().keySet());
        return indexes;
    }

}
