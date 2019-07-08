package com.gcstorage.parkinggather;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.fy.baselibrary.application.BaseActivityLifecycleCallbacks;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.statuslayout.OnStatusAdapter;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;

/**
 * DESCRIPTION：驻车采集 application
 * Created by fangs on 2019/7/1 16:22.
 */
public class ParkingGatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new ConfigUtils.ConfigBiuder()
                .setBgColor(R.color.appHeadBg)
                .setTitleColor(R.color.white)
                .setTitleCenter(true)
                .setBackImg(R.drawable.back_white)
                .setCer(CER)
                .setBASE_URL("http://120.79.194.253:8768/")
                .create(this);

        int designWidth = (int) ResUtils.getMetaData("rudeness_Adapter_Screen_width", 0);
        ScreenUtils.setCustomDensity(this, designWidth);

//        设置activity 生命周期回调
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks(designWidth, new OnStatusAdapter() {
            @Override
            public int errorViewId() {
                return R.layout.state_include_error;
            }

            @Override
            public int emptyDataView() {
                return R.layout.state_include_emptydata;
            }

            @Override
            public int netWorkErrorView() {
                return R.layout.state_include_networkerror;
            }

            @Override
            public int retryViewId() {
                return R.id.tvTry;
            }
        }));
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    String CER = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFejCCA2ICCQD3ki8IwpfN2zANBgkqhkiG9w0BAQsFADB/MQswCQYDVQQGEwJj\n" +
            "bjEOMAwGA1UECAwFaHViZWkxDjAMBgNVBAcMBXd1aGFuMQwwCgYDVQQKDANsenkx\n" +
            "DDAKBgNVBAsMA3R5eTESMBAGA1UEAwwJZ2NzdG9yYWdlMSAwHgYJKoZIhvcNAQkB\n" +
            "FhF0eXlAZ2NzdG9yYWdlLmNvbTAeFw0xODEyMTkxMDQwNDBaFw0yODEyMTYxMDQw\n" +
            "NDBaMH8xCzAJBgNVBAYTAmNuMQ4wDAYDVQQIDAVodWJlaTEOMAwGA1UEBwwFd3Vo\n" +
            "YW4xDDAKBgNVBAoMA2x6eTEMMAoGA1UECwwDdHl5MRIwEAYDVQQDDAlnY3N0b3Jh\n" +
            "Z2UxIDAeBgkqhkiG9w0BCQEWEXR5eUBnY3N0b3JhZ2UuY29tMIICIjANBgkqhkiG\n" +
            "9w0BAQEFAAOCAg8AMIICCgKCAgEA2uleojShK1CTzylyfAX6dXvLL1sXGxG/oWza\n" +
            "6pDL/uxGWVJPciHqQHq6uNECQ7iw+0OdXD+ddbFd3PTMXf4AID5wjhgjyGErLUtY\n" +
            "FV8EhjywR+THG7Y/HEHHm6HqgXElwYkY9GnUVBW9k5l46uYfy2rkc0Ymhm4fqKgq\n" +
            "NBmO3vAPdqcy5NDUC+55Rlb4+3igBgF9TswT0UKtYNWRgxtiVXpbnvZnO33uBqcs\n" +
            "kUbKILKmp8pL7S9Y575JyqZAtMai0zLlqWVUUOu3Pg2xmhhtWACwbcER0i9NJ1XX\n" +
            "x0CRfxL0dGGvoxm9xg/2sBJpRbL2PwsTwhE96YnlDlAGxJKWT8xdbqa7fCQ8yGgg\n" +
            "8wgk3fmEwo9mYfSBVVZw4fn+MJlfFeugyDkttc2XXa/qTAQhy5I/Iv8GzpEuXI5w\n" +
            "KPAhm57HgGms7D4U0jxonDVqT32CS3YpABVO3RKqQ6/G8Hg/FHvi6wTl0XFjkWKh\n" +
            "1R2XgDO3nOTavxa5CeHkZs0UqctDPE/Ox/EZalTVqqF6hiZaXAdj9kM+POEy3LOC\n" +
            "RqgQ9mA6M6F2vP8gLtTet4xs5C2DN0eUq/LbMgaTu5oT5UDFKYyCb8UZ/zDg3LAS\n" +
            "L+B78vMzq7jbeQWHvwJ4NLAiqZdiZUpsxkbLMomilzrK/BrHzg+1IoWQud32omAt\n" +
            "u092sxECAwEAATANBgkqhkiG9w0BAQsFAAOCAgEAouHdKc/Xvo40QkLYPgs359ld\n" +
            "9wmtcdO6FZ7uo1ZgNBZYnGRISiM2Wq+rdBhdqmczB2bdlmY0NgrIcoWdZxF+bX5G\n" +
            "3oWfLMXlo9LojYnwvp0Q8dKS1ktc1JQYbu15Sm3My32yxNBt8cEW6MFRWTaa6o6U\n" +
            "JSLPxFUpfEtQC71jZAnIUE6xvt/Hlokf6oauix2+rrRnomJZAoHJ7c0iVjMHVXv4\n" +
            "Ssm4CNdtgIBaEf4SNpxvhS7Uc3HlOhzeNDADyFMcQW94+dfmz4BlvOzIDMaO3Hwh\n" +
            "u//AKDdlyu25YhMnUj/fyZLMzUu6ii1T0IPS9cJORcoREsKcEXWwtful5tlxZimR\n" +
            "diGAAR71c58IWByyABOge2ZyM8MDK3yax3ZbPKnACMFgrnM+fUlmEzbqzBE+inUA\n" +
            "eRikB2ie6lzqqD7f59XUV7zkHWFt2FnaUknmw0OIZvTvJwuduc5s9JXkTBPwO+Fl\n" +
            "z+XMb418lFIfi3t423RdlqzXyvFJcYuVWqfyHyvMUx3mY8QkmrwoKkYLp6XU7Ldt\n" +
            "RcH6fRCmGCTjuhvB7BkMaoi0F39pDtbRAremKFV5t0ZR6x9wiBYEZ8V0QL8518p4\n" +
            "lZO5bMjfsmNVVykCnBwNS3+3KgCU+6wOT2TeL9hyhh7UpyEQjFoOA18feH0mUo8H\n" +
            "yewairmoRmnw2rqujzw=\n" +
            "-----END CERTIFICATE-----";
}
