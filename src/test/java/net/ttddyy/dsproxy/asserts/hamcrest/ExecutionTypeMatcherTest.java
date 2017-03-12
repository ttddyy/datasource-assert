package net.ttddyy.dsproxy.asserts.hamcrest;

import net.ttddyy.dsproxy.asserts.StatementExecution;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class ExecutionTypeMatcherTest {

    @Test
    public void testMatch() {
        StatementExecution se = new StatementExecution();
        ExecutionTypeMatcher matcher = new ExecutionTypeMatcher(ExecutionType.IS_STATEMENT);
        assertThat(matcher.matches(se)).isTrue();
    }

    @Test
    public void testUnmatchMessage() {
        StatementExecution se = new StatementExecution();
        ExecutionTypeMatcher matcher = new ExecutionTypeMatcher(ExecutionType.IS_CALLABLE);
        try {
            Assert.assertThat(se, matcher);
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: callable\n     but: was statement");
        }
    }
}
