package net.ttddyy.dsproxy.asserts.hamcrest;

import net.ttddyy.dsproxy.asserts.PreparedExecution;
import org.hamcrest.Matcher;
import org.junit.Test;

import static net.ttddyy.dsproxy.asserts.ParameterKeyValueUtils.createSetParam;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.param;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.paramIndexes;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.paramsByIndex;
import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.query;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public class PreparedExecutionTest {

    @Test
    public void testQuery() {
        PreparedExecution pe = new PreparedExecution();
        pe.setQuery("foo");

        assertThat(pe, query(is("foo")));
        assertThat(pe, query(startsWith("fo")));
        assertThat(pe.getQuery(), is("foo"));
    }

    @Test
    public void testParams() {
        PreparedExecution pe = new PreparedExecution();
        pe.getAllParameters().add(createSetParam(1, "foo"));
        pe.getAllParameters().add(createSetParam(2, "bar"));
        pe.getAllParameters().add(createSetParam(3, "baz"));
        pe.getAllParameters().add(createSetParam(4, 100));
        assertThat(pe, paramsByIndex(hasEntry(2, (Object) "bar")));
        assertThat(pe, paramsByIndex(hasEntry(4, (Object) 100)));
    }

    @Test
    public void testParamIndexes() {
        PreparedExecution pe = new PreparedExecution();
        pe.getAllParameters().add(createSetParam(1, "foo"));
        pe.getAllParameters().add(createSetParam(2, "bar"));
        pe.getAllParameters().add(createSetParam(3, "baz"));
        assertThat(pe, paramIndexes(hasItem(1)));
        assertThat(pe, paramIndexes(hasSize(3)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testParamValue() {
        PreparedExecution pe = new PreparedExecution();
        pe.getAllParameters().add(createSetParam(1, "foo"));
        pe.getAllParameters().add(createSetParam(100, 100));

        assertThat(pe, param(1, is((Object) "foo")));

        assertThat(pe, param(1, (Matcher) startsWith("foo")));
        assertThat(pe, param(100, is((Object) 100)));
    }

    @Test
    public void testParamValueWithClass() {
        PreparedExecution pe = new PreparedExecution();
        pe.getAllParameters().add(createSetParam(1, "foo"));
        pe.getAllParameters().add(createSetParam(100, 100));

        assertThat(pe, param(1, String.class, is("foo")));
        assertThat(pe, param(1, String.class, startsWith("foo")));
        assertThat(pe, param(100, Integer.class, is(100)));
    }
}
