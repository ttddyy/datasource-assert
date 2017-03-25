# datasource-assert

Assertions for `DataSource` using [datasource-proxy].  
Also, includes custom assertions for `assertThat` in [Assertj][assertj] and [Hamcrest][hamcrest].

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