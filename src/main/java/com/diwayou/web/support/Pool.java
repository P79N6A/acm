package com.diwayou.web.support;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Closeable;

public abstract class Pool<T> implements Closeable {

    protected GenericObjectPool<T> internalPool;

    /**
     * Using this constructor means you have to set and initialize the internalPool yourself.
     */
    public Pool() {
    }

    public Pool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {
        initPool(poolConfig, factory);
    }

    @Override
    public void close() {
        destroy();
    }

    public boolean isClosed() {
        return this.internalPool.isClosed();
    }

    public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {

        if (this.internalPool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }

        this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }

    public T getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception nse) {
            throw new RuntimeException("Could not get a resource from the pool", nse);
        }
    }

    public void returnResource(T robot) {
        try {
            internalPool.returnObject(robot);
        } catch (Exception e) {
            throw new RuntimeException("Could not return the resource to the pool", e);
        }
    }

    public void destroy() {
        closeInternalPool();
    }

    protected void returnBrokenResourceObject(final T resource) {
        try {
            internalPool.invalidateObject(resource);
        } catch (Exception e) {
            throw new RuntimeException("Could not return the resource to the pool", e);
        }
    }

    protected void closeInternalPool() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not destroy the pool", e);
        }
    }

    public int getNumActive() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumActive();
    }

    public int getNumIdle() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumIdle();
    }

    public int getNumWaiters() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumWaiters();
    }

    public long getMeanBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMeanBorrowWaitTimeMillis();
    }

    public long getMaxBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMaxBorrowWaitTimeMillis();
    }

    private boolean poolInactive() {
        return this.internalPool == null || this.internalPool.isClosed();
    }

    public void addObjects(int count) {
        try {
            for (int i = 0; i < count; i++) {
                this.internalPool.addObject();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error trying to add idle objects", e);
        }
    }
}
