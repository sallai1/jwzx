package com.example.sallai;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class receive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
          if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
              long downloadid = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
              istallapk(context,downloadid);
          }

    }

    private void istallapk(Context context , long downloadid) {


    }
}
