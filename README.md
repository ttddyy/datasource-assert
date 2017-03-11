# datasource-assertions

Assertions for `DataSource` using [datasource-proxy].  
Also, includes custom assertions for `assertThat` in [Assertj][assertj] and [Hamcrest][hamcrest].

## Usage examples

### Regular assertions(assertEquals, etc)
- [SimpleAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/test/SimpleAssertionApiCheck.java)

### With AssertJ
- [AssertJAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/test/assertj/AssertJAssertionApiCheck.java)

### With Hamcrest
- [HamcrestAssertionApiCheck](../master/src/test/java/net/ttddyy/dsproxy/test/hamcrest/HamcrestAssertionApiCheck.java)

----

[datasource-proxy]: https://github.com/ttddyy/datasource-proxy
[assertj]: http://joel-costigliola.github.io/assertj/
[hamcrest]: http://hamcrest.org