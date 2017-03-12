package net.ttddyy.dsproxy.asserts.hamcrest;

import net.ttddyy.dsproxy.asserts.StatementExecution;
import org.junit.Test;

import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.query;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class StatementExecutionTest {

    @Test
    public void testQuery() {
        StatementExecution se = new StatementExecution();
        se.setQuery("foo");

        assertThat(se, query(is("foo")));
        assertThat(se, query(startsWith("fo")));
    }
}
