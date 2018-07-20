package com.creative.atom.action;

import com.creative.atom.action.common.IGet;
import com.creative.atom.action.common.IGetList;
import com.creative.atom.action.common.IListValue;
import com.creative.atom.action.common.ISuccess;
import com.creative.atom.action.common.IValue;
import com.creative.atom.action.common.PullDataList;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangshouchao on 2018/1/27.
 */

public class SingletonThread {
    private final ThreadPoolExecutor mThreadPool;
    private volatile AtomicInteger mCount = new AtomicInteger(-1);

    public SingletonThread() {
        mThreadPool = new ThreadPoolExecutor(
                1,
                1,
                32,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new InnerThread(runnable);
                    }
                }
        );
    }

    public void close() {
        mCount.set(-1);
        mThreadPool.shutdownNow();
    }

    public void postRun(ISuccess success, Runnable runnable) {
        if (mThreadPool.isShutdown()) {
            return;
        }
        int count = mCount.incrementAndGet();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (count > mCount.get()) {
                    return;
                }
                runnable.run();
                if (success == null) {
                    return;
                }
                success.onSuccess();
            }
        });
    }

    public <T> void postGet(IGet<T> get, IValue<T> value) {
        if (mThreadPool.isShutdown()) {
            return;
        }
        int count = mCount.incrementAndGet();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (count > mCount.get()) {
                    get.onGet(null);
                    return;
                }
                T t = value.get();
                get.onGet(t);
            }
        });
    }

    public <T> void postGetList(IGetList<T> getList, IListValue listValue) {
        if (mThreadPool.isShutdown()) {
            return;
        }
        int count = mCount.incrementAndGet();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (count > mCount.get()) {
                    getList.onGetList(new PullDataList<>(0));
                    return;
                }
                PullDataList<T> list = listValue.getList();
                getList.onGetList(list);
            }
        });
    }

    private static class InnerThread extends Thread {
        private int runCount = 0;

        private InnerThread(Runnable runnable) {
            super(runnable);
        }

        @Override
        public void run() {
            super.run();
            System.out.println("Singleton thread run count:" + runCount);
        }
    }
}
