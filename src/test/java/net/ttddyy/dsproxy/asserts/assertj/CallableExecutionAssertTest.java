package net.ttddyy.dsproxy.asserts.assertj;

import net.ttddyy.dsproxy.asserts.CallableExecution;
import org.junit.Before;
import org.junit.Test;

import java.sql.JDBCType;
import java.sql.Types;

import static net.ttddyy.dsproxy.asserts.ParameterKeyValueUtils.createRegisterOut;
import static net.ttddyy.dsproxy.asserts.ParameterKeyValueUtils.createSetNull;
import static net.ttddyy.dsproxy.asserts.ParameterKeyValueUtils.createSetParam;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter.nullParam;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter.outParam;
import static net.ttddyy.dsproxy.asserts.assertj.data.ExecutionParameter.param;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Tadaya Tsuyukubo
 */
public class CallableExecutionAssertTest {

    private CallableExecution ce;
    private CallableExecutionAssert ceAssert;

    @Before
    public void setUp() {
        this.ce = new CallableExecution();
        this.ceAssert = new CallableExecutionAssert(this.ce);
    }

    @Test
    public void testIsSuccess() {
        CallableExecution ce = new CallableExecution();

        // success case
        this.ce.setSuccess(true);
        this.ceAssert.isSuccess();

        // failure case
        this.ce.setSuccess(false);
        try {
            this.ceAssert.isSuccess();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Successful execution> but was: <Failure execution>\n");
        }

    }

    @Test
    public void testIsFailure() {
        // success case
        this.ce.setSuccess(false);
        this.ceAssert.isFailure();

        // failure case
        this.ce.setSuccess(true);
        try {
            this.ceAssert.isFailure();
            fail("asserts should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: <Failure execution> but was: <Successful execution>\n");
        }

    }


    @Test
    public void testContainsParams() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("bar", "BAR"));

        // successful call
        DataSourceAssertAssertions.assertThat(ce).containsParams(param(1, "foo"), param("bar", "BAR"));
        DataSourceAssertAssertions.assertThat(ce).containsParams(param(1, "foo"));
        DataSourceAssertAssertions.assertThat(ce).containsParams(param("bar", "BAR"));

        // value is wrong for index
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParams(param(1, "fooABC"));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: parameters \n<{1=foo, bar=BAR}>\nto contain:\n<[1=fooABC]>\nbut could not find:\n<[1=fooABC]>");
        }

        // value is wrong for name
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParams(param("bar", "BARABC"));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: parameters \n<{1=foo, bar=BAR}>\nto contain:\n<[bar=BARABC]>\nbut could not find:\n<[bar=BARABC]>");
        }

        // no param index key
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParams(param(2, "bar"));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, bar]>\n(params=[1, bar], set-null=[], register-out=[])\nto contain:\n<params=[2]>\nbut could not find:\n<params=[2]>");
        }

        // no param name key
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParams(param("foo", "FOO"));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, bar]>\n(params=[1, bar], set-null=[], register-out=[])\nto contain:\n<params=[foo]>\nbut could not find:\n<params=[foo]>");
        }

    }

    @Test
    public void containsParamsWithMixedParameterTypes() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful call
        DataSourceAssertAssertions.assertThat(ce).containsParams(param(1, "foo"), param("foo", "FOO"),
                nullParam(2, Types.VARCHAR), nullParam("bar", Types.DATE),
                outParam(3, Types.BOOLEAN), outParam("baz", Types.BIGINT));

        try {
            DataSourceAssertAssertions.assertThat(ce).containsParams(nullParam(1, Types.ARRAY));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n" +
                    "<[1, 2, 3, bar, baz, foo]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\n" +
                    "to contain:\n" +
                    "<set-null=[1]>\n" +
                    "but could not find:\n" +
                    "<set-null=[1]>");
        }
    }

    @Test
    public void paramKeysWithContainsParamsExactly() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("bar", "BAR"));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(param(1, "foo"), param("bar", "BAR"));

        // missing one param key (index)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(param(1, "foo"));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, bar]>\n" +
                    "(params=[1, bar], set-null=[], register-out=[])\n" +
                    "to be exactly:\n" +
                    "<[1]>\n" +
                    "(params=[1], set-null=[], register-out=[])\n" +
                    "but missing keys:\n" +
                    "<>\n" +
                    "extra keys:\n" +
                    "<params=[bar]>");
        }

        // missing one param key (name)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(param("bar", "BAR"));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, bar]>\n" +
                    "(params=[1, bar], set-null=[], register-out=[])\n" +
                    "to be exactly:\n" +
                    "<[bar]>\n" +
                    "(params=[bar], set-null=[], register-out=[])\n" +
                    "but missing keys:\n" +
                    "<>\n" +
                    "extra keys:\n" +
                    "<params=[1]>");
        }
    }


    @Test
    public void paramKeysWithContainsParamsExactlyForSetNullParameters() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetNull(1, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(nullParam(1, Types.VARCHAR), nullParam("bar", Types.DATE));

        // missing one param key (index)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(nullParam(1, Types.VARCHAR));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, bar]>\n" +
                    "(params=[], set-null=[1, bar], register-out=[])\n" +
                    "to be exactly:\n" +
                    "<[1]>\n" +
                    "(params=[], set-null=[1], register-out=[])\n" +
                    "but missing keys:\n" +
                    "<>\n" +
                    "extra keys:\n" +
                    "<set-null=[bar]>");
        }

        // missing one param key (name)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(nullParam("bar", Types.DATE));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, bar]>\n" +
                    "(params=[], set-null=[1, bar], register-out=[])\n" +
                    "to be exactly:\n" +
                    "<[bar]>\n" +
                    "(params=[], set-null=[bar], register-out=[])\n" +
                    "but missing keys:\n" +
                    "<>\n" +
                    "extra keys:\n" +
                    "<set-null=[1]>");
        }
    }

    @Test
    public void paramKeysWithContainsParamsExactlyForRegisterOutParameters() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createRegisterOut(1, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("bar", Types.DOUBLE));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(outParam(1, Types.BOOLEAN), outParam("bar", Types.DOUBLE));

        // missing one param key (name)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(outParam(1, Types.BOOLEAN));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, bar]>\n(params=[], set-null=[], register-out=[1, bar])\nto be exactly:\n<[1]>\n(params=[], set-null=[], register-out=[1])\nbut missing keys:\n<>\nextra keys:\n<register-out=[bar]>");
        }

        // missing one param key (index)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamsExactly(outParam("bar", Types.DOUBLE));
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, bar]>\n(params=[], set-null=[], register-out=[1, bar])\nto be exactly:\n<[bar]>\n(params=[], set-null=[], register-out=[bar])\nbut missing keys:\n<>\nextra keys:\n<register-out=[1]>");
        }

    }

    @Test
    public void containsParamNames() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamNames("foo", "bar", "baz");
        DataSourceAssertAssertions.assertThat(ce).containsParamNames("bar");

        // missing param key
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamNames("zzz", "bar");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\nto contain:\n<[bar, zzz]>\nbut could not find:\n<[zzz]>");
        }
    }

    @Test
    public void containsParamName() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamName("foo");
        DataSourceAssertAssertions.assertThat(ce).containsParamName("bar");

        // missing param key
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamName("zzz");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\nto contain:\n<[zzz]>\nbut could not find:\n<[zzz]>");
        }
    }


    @Test
    public void paramKeyIndexes() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamIndexes(1, 2, 3);
        DataSourceAssertAssertions.assertThat(ce).containsParamIndexes(2, 3, 1);
        DataSourceAssertAssertions.assertThat(ce).containsParamIndexes(2, 3);

        // missing one param key (index)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamIndexes(1, 2, 100);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\nto contain:\n<[1, 2, 100]>\nbut could not find:\n<[100]>");
        }

    }

    @Test
    public void paramKeyIndex() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamIndex(1);
        DataSourceAssertAssertions.assertThat(ce).containsParamIndex(2);
        DataSourceAssertAssertions.assertThat(ce).containsParamIndex(3);

        // missing param key (index)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamIndex(100);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\nto contain:\n<[100]>\nbut could not find:\n<[100]>");
        }

    }

    @Test
    public void containsParamKeys() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamKeys(1);
        DataSourceAssertAssertions.assertThat(ce).containsParamKeys("bar");
        DataSourceAssertAssertions.assertThat(ce).containsParamKeys(1, "bar");
        DataSourceAssertAssertions.assertThat(ce).containsParamKeys(1, 2, 3, "foo", "bar", "baz");

        // missing keys
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamKeys(1, 2, 100, "zzz", "bar");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\nto contain:\n<[1, 2, 100, bar, zzz]>\nbut could not find:\n<[100, zzz]>");
        }

        // wrong key type
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamKeys((double) 10.01);
            fail("exception should be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("param key should be int or String");
        }

    }

    @Test
    public void containsParamKey() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful case
        DataSourceAssertAssertions.assertThat(ce).containsParamKey(1);
        DataSourceAssertAssertions.assertThat(ce).containsParamKey(2);
        DataSourceAssertAssertions.assertThat(ce).containsParamKey(3);
        DataSourceAssertAssertions.assertThat(ce).containsParamKey("foo");
        DataSourceAssertAssertions.assertThat(ce).containsParamKey("bar");
        DataSourceAssertAssertions.assertThat(ce).containsParamKey("baz");

        // missing keys
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamKey(100);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\nto contain:\n<[100]>\nbut could not find:\n<[100]>");
        }
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamKey("zzz");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\nto contain:\n<[zzz]>\nbut could not find:\n<[zzz]>");
        }

        // wrong key type
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParamKey(10.01);
            fail("exception should be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("param key should be int or String");
        }

    }

    @Test
    public void testContainsParam() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful call
        DataSourceAssertAssertions.assertThat(ce).containsParam(1, "foo").containsParam("foo", "FOO");

        // index with wrong value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam(1, "WRONG");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: parameters \n<{1=foo, 2=NULL(VARCHAR), 3=OUTPUT(BOOLEAN[16]), bar=NULL(DATE), baz=OUTPUT(BIGINT[-5]), foo=FOO}>\nto contain:\n<[1=WRONG]>\nbut could not find:\n<[1=WRONG]>");
        }

        // name with wrong value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam("foo", "WRONG");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: parameters \n<{1=foo, 2=NULL(VARCHAR), 3=OUTPUT(BOOLEAN[16]), bar=NULL(DATE), baz=OUTPUT(BIGINT[-5]), foo=FOO}>\nto contain:\n<[foo=WRONG]>\nbut could not find:\n<[foo=WRONG]>");
        }

        // no index and value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam(100, "WRONG");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<params=[100]>\nbut could not find:\n<params=[100]>");
        }

        // no name and value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam("WRONG", "WRONG");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<params=[WRONG]>\nbut could not find:\n<params=[WRONG]>");
        }

        // index null param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam(2, Types.VARCHAR);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, bar, baz, foo]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\n" +
                    "to contain:\n" +
                    "<params=[2]>\n" +
                    "but could not find:\n" +
                    "<params=[2]>");
        }

        // name null param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam("bar", Types.DATE);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, bar, baz, foo]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\n" +
                    "to contain:\n" +
                    "<params=[bar]>\n" +
                    "but could not find:\n" +
                    "<params=[bar]>");
        }

        // index out param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam(3, Types.BOOLEAN);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, bar, baz, foo]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\n" +
                    "to contain:\n" +
                    "<params=[3]>\n" +
                    "but could not find:\n" +
                    "<params=[3]>");
        }

        // name out param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsParam("baz", Types.BIGINT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, bar, baz, foo]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\n" +
                    "to contain:\n" +
                    "<params=[baz]>\n" +
                    "but could not find:\n" +
                    "<params=[baz]>");
        }

    }

    @Test
    public void testContainsNullParam() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut("baz", Types.BIGINT));

        // successful call
        DataSourceAssertAssertions.assertThat(ce)
                .containsNullParam(2)
                .containsNullParam(2, Types.VARCHAR)
                .containsNullParam("bar")
                .containsNullParam("bar", Types.DATE)
        ;

        // index with wrong value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam(2, Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: parameters \n<{1=foo, 2=NULL(VARCHAR), 3=OUTPUT(BOOLEAN[16]), bar=NULL(DATE), baz=OUTPUT(BIGINT[-5]), foo=FOO}>\nto contain:\n<[2=NULL(BIT)]>\nbut could not find:\n<[2=NULL(BIT)]>");
        }

        // name with wrong value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam("bar", Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: parameters \n<{1=foo, 2=NULL(VARCHAR), 3=OUTPUT(BOOLEAN[16]), bar=NULL(DATE), baz=OUTPUT(BIGINT[-5]), foo=FOO}>\nto contain:\n<[bar=NULL(BIT)]>\nbut could not find:\n<[bar=NULL(BIT)]>");
        }

        // no index and value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam(100, Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[100]>\nbut could not find:\n<set-null=[100]>");
        }

        // no index with no value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam(100);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[100]>\nbut could not find:\n<set-null=[100]>");
        }

        // no name and value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam("WRONG", Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[WRONG]>\nbut could not find:\n<set-null=[WRONG]>");
        }
        // no name with no value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam("WRONG");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[WRONG]>\nbut could not find:\n<set-null=[WRONG]>");
        }

        // specifying index for param (not for set-null)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam(1);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[1]>\nbut could not find:\n<set-null=[1]>");
        }

        // specifying name in param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam("foo");
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[foo]>\nbut could not find:\n<set-null=[foo]>");
        }

        // specifying index in out-param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam(3, Types.BOOLEAN);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[3]>\nbut could not find:\n<set-null=[3]>");
        }

        // specifying name in out-param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsNullParam("baz", Types.BIGINT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpecting: callable parameter keys\n<[1, 2, 3, bar, baz, foo]>\n(params=[1, foo], set-null=[2, bar], register-out=[3, baz])\nto contain:\n<set-null=[baz]>\nbut could not find:\n<set-null=[baz]>");
        }

    }

    @Test
    public void testContainsOutParam() {
        CallableExecution ce = new CallableExecution();
        ce.getAllParameters().add(createSetParam(1, "foo"));
        ce.getAllParameters().add(createSetParam("foo", "FOO"));
        ce.getAllParameters().add(createSetNull(2, Types.VARCHAR));
        ce.getAllParameters().add(createSetNull("bar", Types.DATE));
        ce.getAllParameters().add(createRegisterOut(3, Types.BOOLEAN));
        ce.getAllParameters().add(createRegisterOut(4, JDBCType.BIGINT));
        ce.getAllParameters().add(createRegisterOut("baz", Types.DATE));
        ce.getAllParameters().add(createRegisterOut("qux", JDBCType.VARCHAR));

        // successful call
        DataSourceAssertAssertions.assertThat(ce)
                .containsOutParam(3, Types.BOOLEAN)
                .containsOutParam(4, JDBCType.BIGINT)
                .containsOutParam("baz", Types.DATE)
                .containsOutParam("qux", JDBCType.VARCHAR)
        ;

        // correct index with wrong value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam(3, Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: parameters \n" +
                    "<{1=foo, 2=NULL(VARCHAR), 3=OUTPUT(BOOLEAN[16]), 4=OUTPUT(BIGINT), bar=NULL(DATE), baz=OUTPUT(DATE[91]), foo=FOO, qux=OUTPUT(VARCHAR)}>\n" +
                    "to contain:\n" +
                    "<[3=OUTPUT(BIT[-7])]>\n" +
                    "but could not find:\n" +
                    "<[3=OUTPUT(BIT[-7])]>");
        }

        // correct name with wrong value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam("baz", Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: parameters \n" +
                    "<{1=foo, 2=NULL(VARCHAR), 3=OUTPUT(BOOLEAN[16]), 4=OUTPUT(BIGINT), bar=NULL(DATE), baz=OUTPUT(DATE[91]), foo=FOO, qux=OUTPUT(VARCHAR)}>\n" +
                    "to contain:\n" +
                    "<[baz=OUTPUT(BIT[-7])]>\n" +
                    "but could not find:\n" +
                    "<[baz=OUTPUT(BIT[-7])]>");
        }

        // no index and value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam(100, Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, 4, bar, baz, foo, qux]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, 4, baz, qux])\n" +
                    "to contain:\n" +
                    "<register-out=[100]>\n" +
                    "but could not find:\n" +
                    "<register-out=[100]>");
        }

        // no name and value
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam("WRONG", Types.BIT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, 4, bar, baz, foo, qux]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, 4, baz, qux])\n" +
                    "to contain:\n" +
                    "<register-out=[WRONG]>\n" +
                    "but could not find:\n" +
                    "<register-out=[WRONG]>");
        }

        // specifying index for param (not for set-null)
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam(1, Types.BOOLEAN);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, 4, bar, baz, foo, qux]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, 4, baz, qux])\n" +
                    "to contain:\n" +
                    "<register-out=[1]>\n" +
                    "but could not find:\n" +
                    "<register-out=[1]>");
        }

        // specifying name in param
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam("foo", Types.BOOLEAN);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, 4, bar, baz, foo, qux]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, 4, baz, qux])\n" +
                    "to contain:\n" +
                    "<register-out=[foo]>\n" +
                    "but could not find:\n" +
                    "<register-out=[foo]>");
        }

        // specifying index in set-null
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam(2, Types.BOOLEAN);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, 4, bar, baz, foo, qux]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, 4, baz, qux])\n" +
                    "to contain:\n" +
                    "<register-out=[2]>\n" +
                    "but could not find:\n" +
                    "<register-out=[2]>");
        }

        // specifying name in set-null
        try {
            DataSourceAssertAssertions.assertThat(ce).containsOutParam("bar", Types.BIGINT);
            fail("exception should be thrown");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\n" +
                    "Expecting: callable parameter keys\n" +
                    "<[1, 2, 3, 4, bar, baz, foo, qux]>\n" +
                    "(params=[1, foo], set-null=[2, bar], register-out=[3, 4, baz, qux])\n" +
                    "to contain:\n" +
                    "<register-out=[bar]>\n" +
                    "but could not find:\n" +
                    "<register-out=[bar]>");
        }

    }

    @Test
    public void testQuery() {
        CallableExecution ce = new CallableExecution();
        ce.setQuery("SELECT");

        DataSourceAssertAssertions.assertThat(ce).query().isEqualTo("SELECT");
    }

}
