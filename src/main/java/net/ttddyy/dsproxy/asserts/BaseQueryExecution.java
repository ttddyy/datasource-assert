package net.ttddyy.dsproxy.asserts;

/**
 * Base implementation class for {@link QueryExecution}.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.0
 */
public abstract class BaseQueryExecution implements QueryExecution {

    private boolean success;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

}
