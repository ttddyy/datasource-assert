package net.ttddyy.dsproxy.asserts;

/**
 * Framework exception from datasource-assert.
 *
 * @author Tadaya Tsuyukubo
 */
public class DataSourceAssertException extends RuntimeException {

    public DataSourceAssertException() {
    }

    public DataSourceAssertException(String message) {
        super(message);
    }

    public DataSourceAssertException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceAssertException(Throwable cause) {
        super(cause);
    }

    public DataSourceAssertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
