package com.dolphy.tv;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import android.graphics.Color;
import android.view.ViewGroup;

public class MainActivity extends Activity {
    private ConsumerIrManager ir;
    private WebView webView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(13,21,17));
        getWindow().setNavigationBarColor(Color.rgb(13,21,17));
        ir = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(13,21,17));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new IrBridge(), "DolphyIR");
        setContentView(webView, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.loadUrl("file:///android_asset/index.html");
    }

    private class IrBridge {
        @JavascriptInterface
        public boolean isAvailable() {
            return ir != null && ir.hasIrEmitter();
        }

        @JavascriptInterface
        public String sendNec(String hex) {
            if (ir == null || !ir.hasIrEmitter()) return "NO_IR";
            if (hex == null) return "BAD_CODE";
            String value = hex.trim().replace("0x", "").replace("0X", "").toUpperCase();
            if (!value.matches("[0-9A-F]{8}")) return "BAD_CODE";
            long code;
            try { code = Long.parseLong(value, 16); }
            catch (Exception e) { return "BAD_CODE"; }

            int[] pattern = new int[67];
            int p = 0;
            pattern[p++] = 9000;
            pattern[p++] = 4500;
            for (int bit = 0; bit < 32; bit++) {
                boolean one = ((code >>> bit) & 1L) == 1L;
                pattern[p++] = 560;
                pattern[p++] = one ? 1690 : 560;
            }
            pattern[p] = 560;
            try {
                ir.transmit(38000, pattern);
                return "OK";
            } catch (Exception e) {
                return "SEND_ERROR";
            }
        }
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
