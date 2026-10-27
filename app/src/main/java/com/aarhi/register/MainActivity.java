package com.aarhi.register;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import java.util.List;

/**
 * Opens the shared Aarhi Project Register in Chrome as a full-screen custom tab.
 * Chrome's own claude.ai sign-in is used, so data stays shared between Hirok and Nilanjan.
 */
public class MainActivity extends Activity {
    static final String URL = "https://claude.ai/artifact/MRHQ5kDmsCKXmJyRH1S4PT";
    static final int NAVY = 0xFF1B2B4C;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Uri uri = Uri.parse(URL);
        Intent tab = new Intent(Intent.ACTION_VIEW, uri);
        Bundle session = new Bundle();
        session.putBinder("android.support.customtabs.extra.SESSION", null);
        tab.putExtras(session);
        tab.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", NAVY);
        tab.putExtra("androidx.browser.customtabs.extra.NAVIGATION_BAR_COLOR", NAVY);
        tab.putExtra("android.support.customtabs.extra.TITLE_VISIBILITY", 1);
        tab.putExtra("androidx.browser.customtabs.extra.SHARE_STATE", 2);
        String pkg = customTabBrowser();
        if (pkg != null) tab.setPackage(pkg);
        try {
            startActivity(tab);
        } catch (Exception e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception e2) {
                Toast.makeText(this, "Install Chrome to open the Aarhi Register.", Toast.LENGTH_LONG).show();
            }
        }
        finish();
        overridePendingTransition(0, 0);
    }

    /** Prefer Chrome (where claude.ai is usually signed in), else any browser that supports custom tabs. */
    private String customTabBrowser() {
        PackageManager pm = getPackageManager();
        String[] preferred = { "com.android.chrome", "com.chrome.beta", "com.chrome.dev" };
        for (String p : preferred) {
            try { pm.getPackageInfo(p, 0); return p; } catch (PackageManager.NameNotFoundException ignored) { }
        }
        List<ResolveInfo> services = pm.queryIntentServices(new Intent("android.support.customtabs.action.CustomTabsService"), 0);
        return services.isEmpty() ? null : services.get(0).serviceInfo.packageName;
    }
}
