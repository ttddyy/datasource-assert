# datasource-assert

![Maven Central][badge-maven-central]

`datasource-assert` provides assertion API for DataSource to validate query executions.

The assertion API is used for assertion methods such as `assertEquals` in [JUnit][junit] and [TestNG][testng].
Also, it comes with support for `assertThat` in [AssertJ][assertj] and [Hamcrest][hamcrest].


## Artifacts

### Released Version

```xml
<dependency>
  <groupId>net.ttddyy</groupId>
  <artifactId>datasource-assert</artifactId>
  <version>[LATEST_VERSION]</version>
</dependency>
```

## Documentation

- User Guide
  - [Latest Released Version][user-guide-latest]
  - [Snapshot Version][user-guide-snapshot]
- Javadoc
  - [Latest Released Version][javadoc-latest]
  - [Snapshot Version][javadoc-snapshot]


## How to use

Wrap your datasource with `ProxyTestDataSource`.

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

For normal assertions such as [JUnit][junit]/[TestNG][testng] `assertEquals`, use methods from `ProxyTestDataSource`. 

```java
assertEquals(3, ds.getQueryExecutions().size());
PreparedExecution pe = ds.getFirstPrepared();
assertTrue(pe.isSuccess());
```

See more details on ["Usage examples"](#usage-examples).

### AssertJ

Static import `assertThat` from `DataSourceAssertAssertions`.

```java
import static net.ttddyy.dsproxy.asserts.assertj.DataSourceAssertAssertions.assertThat;
```

```java
assertThat(ds.getQueryExecutions()).hasSize(1);
assertThat(ds.getQueryExecutions().get(0)).isStatement().asStatement().query().isEqualTo("SELECT id FROM emp");
assertThat(ds.getFirstPrepared()).containsParam(1, "foo");
```

See more details on ["Usage examples"](#usage-examples).

### Hamcrest

Use matchers from `DataSourceAssertMatchers`.

```java
assertThat(ds, statementCount(3));
assertThat(ds, executions(1, is(statement())));
assertThat((StatementExecution) statements.get(0), query(is("SELECT id FROM emp")));
assertThat(ds.getFirstPrepared(), paramAsInteger(1, is(100)));
```

See more details on ["Usage examples"](#usage-examples).

## Usage examples

### Regular assertions(assertEquals, etc)
- [SimpleAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/asserts/api/SimpleAssertionApiCheck.java)

### With AssertJ
- [AssertJAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/asserts/api/AssertJAssertionApiCheck.java)

### With Hamcrest
- [HamcrestAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/asserts/api/HamcrestAssertionApiCheck.java)


----

[badge-maven-central]: https://maven-badges.herokuapp.com/maven-central/net.ttddyy/datasource-assert/badge.svg
[user-guide-latest]:   https://ttddyy.github.io/datasource-assert/docs/current/user-guide/index.html
[user-guide-snapshot]: https://ttddyy.github.io/datasource-assert/docs/snapshot/user-guide/index.html
[javadoc-latest]:      https://ttddyy.github.io/datasource-assert/docs/current/api/index.html
[javadoc-snapshot]:    https://ttddyy.github.io/datasource-assert/docs/snapshot/api/index.html
[datasource-proxy]:    https://github.com/ttddyy/datasource-proxy
[junit]:     http://junit.org/
[testng]:    http://testng.org/
[assertj]:   http://joel-costigliola.github.io/assertj/
[hamcrest]:  http://hamcrest.org