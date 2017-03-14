package net.ttddyy.dsproxy.asserts.hamcrest;

import net.ttddyy.dsproxy.QueryType;
import net.ttddyy.dsproxy.asserts.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

import static net.ttddyy.dsproxy.asserts.hamcrest.DataSourceProxyMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * API compilation check with hamcrest.
 *
 * @author Tadaya Tsuyukubo
 */
public class HamcrestAssertionApiCheck {

    private DataSource actualDataSource;

    private void testDataSource() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);

        // execution count
        assertThat(ds, executionCount(3));
        assertThat(ds, statementCount(3));
        assertThat(ds, batchStatementCount(3));
        assertThat(ds, statementOrBatchStatementCount(3));
        assertThat(ds, preparedCount(3));
        assertThat(ds, batchPreparedCount(3));
        assertThat(ds, preparedOrBatchPreparedCount(3));
        assertThat(ds, callableCount(3));
        assertThat(ds, batchCallableCount(3));
        assertThat(ds, callableOrBatchCallableCount(3));

        // each execution
        assertThat(ds, executions(0, isBatch()));
        assertThat(ds, executions(0, batch()));
        assertThat(ds, executions(0, is(batch())));

        assertThat(ds, executions(0, isStatement()));
        assertThat(ds, executions(0, statement()));
        assertThat(ds, executions(0, is(isStatement())));

        assertThat(ds, executions(0, isBatchStatement()));
        assertThat(ds, executions(0, batchStatement()));
        assertThat(ds, executions(0, is(batchStatement())));

        assertThat(ds, executions(0, isStatementOrBatchStatement()));
        assertThat(ds, executions(0, isPrepared()));
        assertThat(ds, executions(0, isBatchPrepared()));
        assertThat(ds, executions(0, isPreparedOrBatchPrepared()));
        assertThat(ds, executions(0, isCallable()));
        assertThat(ds, executions(0, isBatchCallable()));
        assertThat(ds, executions(0, isCallableOrBatchCallable()));

        assertThat(ds, executions(0, is(success())));

        // query count
        assertThat(ds, totalQueryCount(5));
        assertThat(ds, selectCount(3));
        assertThat(ds, insertCount(3));
        assertThat(ds, otherCount(3));
        assertThat(ds, allOf(updateCount(3), deleteCount(1)));

        ds.reset();

        assertThat(ds.getFirstStatement(), query(is("abc")));
        assertThat(ds.getFirstBatchStatement(), queries(0, is("abc")));
        assertThat(ds.getFirstBatchCallable(), query(is("abc")));

    }

    private void queryExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        QueryExecution qe = ds.getFirstStatement();

        assertThat(qe, success());
        assertThat(qe, failure());

        // type of execution
        assertThat(qe, isBatch());
        assertThat(qe, statement());
        assertThat(qe, batchStatement());
        assertThat(qe, statementOrBatchStatement());
        assertThat(qe, prepared());
        assertThat(qe, batchPrepared());
        assertThat(qe, preparedOrBatchPrepared());
        assertThat(qe, callable());
        assertThat(qe, batchCallable());
        assertThat(qe, callableOrBatchCallable());

    }

    private void statementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        StatementExecution se = ds.getFirstStatement();

        assertThat(se, success());
        assertThat(se, failure());

        // query with StringMatcher
        assertThat(se, query(is("...")));
        assertThat(se, query(startsWith("...")));

        assertThat(se, queryType(QueryType.SELECT));
    }

    private void batchStatementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        StatementBatchExecution sbe = ds.getFirstBatchStatement();

        assertThat(sbe, success());
        assertThat(sbe, failure());

        assertThat(sbe, queries(0, is("...")));   // string matcher
        assertThat(sbe, queries(hasItems("...", "...")));  // collection matcher
        assertThat(sbe, queryTypes(0, is(select())));
        assertThat(sbe, queryTypes(0, either(insert()).or(update())));
    }

    private void preparedStatementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        PreparedExecution pe = ds.getFirstPrepared();

        assertThat(pe, success());
        assertThat(pe, failure());

        assertThat(pe, query(is("FOO")));  // string matcher

        // parameter indexes
        assertThat(pe, paramIndexes(1, 2, 3));
        assertThat(pe, paramIndexes(hasItem(1)));  // integer collection matcher

        // parameters
        assertThat(pe, param(1, Integer.class, is(100)));
        assertThat(pe, param(1, is((Object) 100)));  // Object matcher
        assertThat(pe, paramAsInteger(1, is(100)));
        assertThat(pe, paramAsLong(1, is(100L)));
        assertThat(pe, paramAsDouble(1, is(100.0)));
        assertThat(pe, paramAsShort(1, is((short) 1)));
        assertThat(pe, paramAsBoolean(1, is(true)));
        assertThat(pe, paramAsByte(1, is(new Byte("1"))));
        assertThat(pe, paramAsFloat(1, is(Float.valueOf("1"))));
        assertThat(pe, paramAsBigDecimal(1, is(BigDecimal.ONE)));
        assertThat(pe, paramAsBytes(1, is("100".getBytes())));
        assertThat(pe, paramAsDate(1, is(new Date(100))));
        assertThat(pe, paramAsTime(1, is(new Time(1000))));
        assertThat(pe, paramAsTimestamp(1, is(new Timestamp(1000))));
        assertThat(pe, paramAsArray(1, is((Array) new Object())));
        assertThat(pe, paramsByIndex(hasEntry(1, (Object) "FOO")));  // map matcher

        // setNull parameters
        assertThat(pe, nullParam(10, Types.INTEGER));
        assertThat(pe, nullParam(10));

        assertThat(pe, allOf(paramAsInteger(1, is(100)), paramAsInteger(2, is(200)), nullParam(3)));
    }

    private void preparedBatchStatementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        PreparedBatchExecution pbe = ds.getFirstBatchPrepared();

        assertThat(pbe, success());
        assertThat(pbe, failure());

        assertThat(pbe, query(is("FOO")));

        // check batch executions
        assertThat(pbe, batchSize(10));

        assertThat(pbe, batch(0, paramIndexes(1, 2, 3)));
        assertThat(pbe, batch(0, paramIndexes(hasItem(11))));    // integer collection matcher

        assertThat(pbe, batch(0, param(1, Integer.class, is(100))));
        assertThat(pbe, batch(0, param(1, is((Object) 100))));  // Object matcher
        assertThat(pbe, batch(0, paramAsInteger(1, is(100))));
        assertThat(pbe, batch(0, paramsByIndex(hasEntry(11, (Object) "FOO"))));  // map matcher

        // setNull parameters
        assertThat(pbe, batch(0, nullParam(10, Types.INTEGER)));
        assertThat(pbe, batch(0, nullParam(10)));

        assertThat(pbe, batch(0,
                allOf(paramAsInteger(1, is(100)), paramAsInteger(2, is(200)), nullParam(3))));
    }

    private void callableStatementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        CallableExecution ce = ds.getFirstCallable();

        assertThat(ce, success());
        assertThat(ce, failure());

        assertThat(ce, query(is("FOO")));

        // parameter names/indexes
        assertThat(ce, paramNames("foo", "bar", "baz"));
        assertThat(ce, paramNames(hasItem("foo")));  // string collection matcher
        assertThat(ce, paramIndexes(1, 2, 3));
        assertThat(ce, paramIndexes(hasItem(11)));    // integer collection matcher

        // parameters with map matcher
        assertThat(ce, paramsByName(hasEntry("foo", (Object) "FOO")));
        assertThat(ce, paramsByIndex(hasEntry(1, (Object) "FOO")));

        // parameters
        assertThat(ce, param("foo", is((Object) 100)));
        assertThat(ce, param("foo", Integer.class, is(100)));
        assertThat(ce, paramAsInteger("foo", is(100)));
        assertThat(ce, paramAsLong("foo", is(100L)));
        assertThat(ce, paramAsDouble("foo", is(100.0)));
        assertThat(ce, paramAsShort("foo", is((short) 1)));
        assertThat(ce, paramAsBoolean("foo", is(true)));
        assertThat(ce, paramAsByte("foo", is(new Byte("1"))));
        assertThat(ce, paramAsFloat("foo", is(Float.valueOf("1"))));
        assertThat(ce, paramAsBigDecimal("foo", is(BigDecimal.ONE)));
        assertThat(ce, paramAsBytes("foo", is("100".getBytes())));
        assertThat(ce, paramAsDate("foo", is(new Date(100))));
        assertThat(ce, paramAsTime("foo", is(new Time(1000))));
        assertThat(ce, paramAsTimestamp("foo", is(new Timestamp(1000))));
        assertThat(ce, paramAsArray("foo", is((Array) new Object())));
        assertThat(ce, param("foo", is((Object) 100)));
        assertThat(ce, param("foo", Integer.class, is(100)));
        assertThat(ce, paramAsInteger(1, is(100)));

        // setNull parameters
        assertThat(ce, nullParam("bar"));
        assertThat(ce, nullParam("bar", Types.INTEGER));
        assertThat(ce, nullParam(2));
        assertThat(ce, nullParam(2, Types.INTEGER));

        assertThat(ce, allOf(paramAsInteger(1, is(100)), paramAsInteger("foo", is(100)),
                nullParam("bar")));

        // registerOut parameters
        assertThat(ce, outParamNames(hasItem("foo")));
        assertThat(ce, outParamIndexes(hasItem(10)));
        assertThat(ce, outParam("foo", Types.INTEGER));
        assertThat(ce, outParam("foo", JDBCType.INTEGER));
        assertThat(ce, outParam(10, Types.INTEGER));
        assertThat(ce, outParam(10, JDBCType.INTEGER));
        assertThat(ce, allOf(outParam("foo", JDBCType.INTEGER), outParam(10, Types.INTEGER)));

        assertThat(ce, allOf(paramAsInteger(10, is(100)), paramAsInteger("foo", is(100)),
                outParam("bar", JDBCType.INTEGER)));
    }

    private void callableBatchStatementExecution() {
        ProxyTestDataSource ds = new ProxyTestDataSource(this.actualDataSource);
        CallableBatchExecution cbe = ds.getFirstBatchCallable();

        assertThat(cbe, success());
        assertThat(cbe, failure());

        assertThat(cbe, query(is("FOO")));

        assertThat(cbe, batchSize(10));

        // parameter names/indexes
        assertThat(cbe, batch(0, paramNames("foo", "bar", "baz")));
        assertThat(cbe, batch(0, paramNames(hasItem("foo"))));
        assertThat(cbe, batch(0, paramIndexes(1, 2, 3)));
        assertThat(cbe, batch(0, paramIndexes(hasItem(1))));

        // parameters with map matcher
        assertThat(cbe, batch(0, paramsByName(hasEntry("foo", (Object) "FOO"))));
        assertThat(cbe, batch(0, paramsByIndex(hasEntry(1, (Object) "FOO"))));

        // parameters
        assertThat(cbe, batch(0, param("foo", is((Object) 100))));
        assertThat(cbe, batch(0, param("foo", Integer.class, is(100))));
        assertThat(cbe, batch(0, paramAsInteger("foo", is(100))));
        assertThat(cbe, batch(0, param(1, is((Object) 100))));
        assertThat(cbe, batch(0, param(1, Integer.class, is(100))));
        assertThat(cbe, batch(0, paramAsInteger(1, is(100))));

        // setNull parameters
        assertThat(cbe, batch(0, nullParam("bar")));
        assertThat(cbe, batch(0, nullParam("bar", Types.INTEGER)));
        assertThat(cbe, batch(0, nullParam(2)));
        assertThat(cbe, batch(0, nullParam(2, Types.INTEGER)));

        assertThat(cbe, batch(0, allOf(paramAsInteger(1, is(100)), paramAsInteger("foo", is(100)),
                nullParam("bar"))));

        // registerOut parameters
        assertThat(cbe, batch(0, outParamNames(hasItem("foo"))));
        assertThat(cbe, batch(0, outParamIndexes(hasItem(10))));
        assertThat(cbe, batch(0, outParam("foo", Types.INTEGER)));
        assertThat(cbe, batch(0, outParam("foo", JDBCType.INTEGER)));
        assertThat(cbe, batch(0, outParam(10, Types.INTEGER)));
        assertThat(cbe, batch(0, outParam(10, JDBCType.INTEGER)));
        assertThat(cbe,
                batch(0, allOf(outParam("foo", JDBCType.INTEGER), outParam(10, Types.INTEGER))));

        assertThat(cbe, batch(0,
                allOf(paramAsInteger("foo", is(100)), outParam("bar", Types.INTEGER),
                        nullParam("baz"))));
    }

}
