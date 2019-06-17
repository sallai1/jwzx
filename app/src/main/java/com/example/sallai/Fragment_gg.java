package com.example.sallai;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class Fragment_gg  extends Fragment {

    private WebView web;
    public ImageView btn_1;
    public ImageView btn_2;
    private View.OnClickListener btn;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          if (savedInstanceState==null) {
              view = inflater.inflate(R.layout.fragment_gg, null);
              btn_1 = (ImageView) view.findViewById(R.id.image_1);
              btn_2 = (ImageView) view.findViewById(R.id.image_2);
              return view;
          }else {
              return null;
          }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        web = view.findViewById(R.id.web_1);
        web.loadUrl("http://s3411982.bnana.top/wx_room/jwgg.php");
        String ua = web.getSettings().getUserAgentString();
        web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1; MZ-m1 metal Build/LMY47I) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0");
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url); //在当前的webview中跳转到新的url

                return true;
            }
        });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              web.goBack();

            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.reload();
                Toast.makeText(getActivity(), "正在刷新", Toast.LENGTH_LONG).show();
            }
        });
    }

}
