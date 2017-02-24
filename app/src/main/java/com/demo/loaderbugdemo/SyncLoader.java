package com.demo.loaderbugdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import java.util.Random;

public class SyncLoader extends Loader<String> {
    /**
     * The randomString that is maintained across config change and used by the view.
     */
    @Nullable private String randomString;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public SyncLoader(Context context) {
        super(context);
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        // If the randomString is non-null, we're coming from a config change and we re-use it
        if (randomString != null) {
            deliverResult(randomString);
        } else { // Otherwise, we get a new randomString
            forceLoad();
        }
    }

    /**
     * Subclasses must implement this to take care of requests to {@link #forceLoad()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onForceLoad() {
        randomString = Integer.toString(new Random().nextInt());
        deliverResult(randomString);
    }

    /**
     * Subclasses must implement this to take care of resetting their loader,
     * as per {@link #reset()}.  This is not called by clients directly,
     * but as a result of a call to {@link #reset()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onReset() {
        randomString = null;
    }
}
