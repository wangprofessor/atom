package com.creative.atom.action;

import com.creative.atom.action.common.PullDataList;
import com.creative.atom.action.common.CallbackException;
import com.creative.atom.action.common.IGet;
import com.creative.atom.action.common.IGetList;
import com.creative.atom.action.common.IPull;
import com.creative.atom.action.common.IPullList;
import com.creative.atom.action.common.ISend;
import com.creative.atom.action.common.ISuccessFailure;

/**
 * Created by wangshouchao on 2017/9/30.
 */

public class AsyncChecker {
    private int max;
    private int count = 0;
    private boolean autoAddMax;
    private volatile boolean isFailure = false;

    private final ISuccessFailure callback;

    public class SendCallback implements ISend {
        public SendCallback() {
            tryAddMax();
        }

        @Override
        public void onFailure(CallbackException e) {
            checkFailure(e);
        }

        @Override
        public void onSuccess() {
            checkSuccess();
        }

        @Override
        public void onTooMuch() {
            checkFailure(new CallbackException("too much", "async checker send"));
        }
    }

    public class PullCallback <T> implements IPull<T> {
        private IGet<T> get;
        private IPull<T> pull;

        public PullCallback(IGet<T> get) {
            this.get = get;
            tryAddMax();
        }

        public PullCallback(IPull<T> pull) {
            this.pull = pull;
            tryAddMax();
        }

        @Override
        public void onFailure(CallbackException e) {
            if (pull != null) {
                pull.onFailure(e);
            }
            checkFailure(e);
        }


        @Override
        public void onTooMuch() {
            if (pull != null) {
                pull.onTooMuch();
            }
            checkFailure(new CallbackException("too much", "async checker pull"));
        }

        @Override
        public void onGet(T data, int getType) {
            if (get != null) {
                get.onGet(data);
            }
            if (pull != null) {
                pull.onGet(data, getType);
            }
            checkSuccess();
        }
    }

    public class PullListCallback <T> implements IPullList<T> {
        private IGetList<T> getList;
        private IPullList<T> pullList;

        public PullListCallback(IGetList<T> getList) {
            this.getList = getList;
            tryAddMax();
        }

        public PullListCallback(IPullList<T> pullList) {
            this.pullList = pullList;
            tryAddMax();
        }

        @Override
        public void onFailure(CallbackException e) {
            if(pullList != null) {
                pullList.onFailure(e);
            }
            checkFailure(e);
        }


        @Override
        public void onTooMuch() {
            if(pullList != null) {
                pullList.onTooMuch();
            }
            checkFailure(new CallbackException("too much", "async checker pull list"));
        }

        @Override
        public void onGetList(PullDataList<T> dataList) {
            if(getList != null) {
                getList.onGetList(dataList);
            }
            if(pullList != null) {
                pullList.onGetList(dataList);
            }
            if(dataList.hasRemaining) {
                return;
            }
            checkSuccess();
        }
    }

    public AsyncChecker(ISuccessFailure callback) {
        this(0, callback);
        autoAddMax = true;
    }

    public AsyncChecker(int max, ISuccessFailure callback) {
        this.max = max;
        this.callback = callback;
        autoAddMax = false;
    }

    private void tryAddMax() {
        if(autoAddMax) {
            synchronized (this) {
                max++;
            }
        }
    }

    public synchronized void checkSuccess() {
        count++;
        if (count == max) {
            callback.onSuccess();
        } else if(count > max) {
//            throw new RuntimeException("count can not be large than max");
        }
    }

    public void checkFailure(CallbackException e) {
        if (isFailure) {
            return;
        }
        isFailure = true;
        callback.onFailure(e);
    }
}
