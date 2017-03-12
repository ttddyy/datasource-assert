package net.ttddyy.dsproxy.asserts;

import java.util.List;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public interface BatchParameterHolder {
    List<BatchExecutionEntry> getBatchExecutionEntries();
}
