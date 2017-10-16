package net.ttddyy.dsproxy.asserts;

import java.util.List;

/**
 * Represent execution that hold batched batch parameters.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public interface BatchParameterHolder {
    List<BatchExecutionEntry> getBatchExecutionEntries();
}
