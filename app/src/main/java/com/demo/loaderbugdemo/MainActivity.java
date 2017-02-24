package com.demo.loaderbugdemo;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import junit.framework.Assert;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int timesOnLoadFinishedCalled = 0;
    private String deliveredString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                onClickSomeButton();
            }
        }, 200);
    }

    /**
     * For example, this may load a fragment with a Loader.
     * The loader may be delivering a Presenter to the fragment.
     * If onLoadFinished is called twice, the presenter will initialize itself twice,gi
     * which can be a problem for complex flows.
     */
    private void onClickSomeButton() {
        int LOADER_ID = 123;
        getSupportLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                return new SyncLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                timesOnLoadFinishedCalled++;
                deliveredString = data;
                Log.v("DEMO", "onLoadFinished called " + timesOnLoadFinishedCalled + " time(s).");

                if (timesOnLoadFinishedCalled > 1) {
                    Log.e("DEMO", "onLoadFinished called more than once!");
                    Assert.fail("onLoadFinished called more than once!");
                }
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {
                timesOnLoadFinishedCalled = 0;
                deliveredString = null;
            }
        });
    }
}
