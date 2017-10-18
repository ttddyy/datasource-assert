# datasource-assert

Assertions for `DataSource` using [datasource-proxy].  
Also, includes custom assertions for `assertThat` in [Assertj][assertj] and [Hamcrest][hamcrest].

## How to use

Wrap your datasource to `ProxyTestDataSource`.

```java
@Test
public void myTest() {
  ProxyTestDataSource ds = new ProxyTestDataSource(actualDataSource);
  // rest of test 
}
```

If you want to reuse the same `ProxyTestDataSource` instance, you could `reset()` it between tests.

```java
@After  // @AfterEach, @AfterMethod
public void tearDown() {
   this.ds.reset();
}
``` 


### Regular assertions

For normal assertions such as junit/testng `assertEquals`, use methods from `ProxyTestDataSource`. 

```java
assertEquals(3, ds.getQueryExecutions().size());
PreparedExecution pe = ds.getFirstPrepared();
assertTrue(pe.isSuccess());
```

### AssertJ

Static import `assertThat` from `DataSourceProxyAssertions`.

```java
import static net.ttddyy.dsproxy.asserts.assertj.DataSourceProxyAssertions.assertThat;
```

```java
assertThat(ds.getQueryExecutions()).hasSize(1);
assertThat(ds.getQueryExecutions().get(0)).isStatement().asStatement().query().isEqualTo("SELECT id FROM emp");
```

### Hamcrest

Use matchers from `DataSourceProxyMatchers`.

```java
assertThat(ds, statementCount(3));
assertThat(ds, executions(1, is(statement())));
assertThat((StatementExecution) statements.get(0), query(is("SELECT id FROM emp")));
```

## Usage examples

### Regular assertions(assertEquals, etc)
- [SimpleAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/asserts/SimpleAssertionApiCheck.java)

### With AssertJ
- [AssertJAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/asserts/assertj/AssertJAssertionApiCheck.java)

### With Hamcrest
- [HamcrestAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/asserts/hamcrest/HamcrestAssertionApiCheck.java)


## Artifacts

### SNAPSHOT (via JitPack)

[![](https://jitpack.io/v/ttddyy/datasource-assert.svg)](https://jitpack.io/#ttddyy/datasource-assert)



```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>om.github.ttddyy</groupId>
    <artifactId>datasource-assert</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

----

[datasource-proxy]: https://github.com/ttddyy/datasource-proxy
[assertj]: http://joel-costigliola.github.io/assertj/
[hamcrest]: http://hamcrest.org