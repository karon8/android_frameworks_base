/*
 * Copyright (C) 2013 The Mokee Open Source Project
 * Copyright (C) 2015 The Android One Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.internal.util.one;

import android.text.TextUtils;

public final class PhoneLocation {

    private static String PHONE;
    private static String LOCATION;
    private static String LIBPATH = "one-phoneloc-jni";

    public PhoneLocation() {
    }

    static {
        System.loadLibrary(LIBPATH);
    }

    static native String getPhoneLocationJni(String num);

    private synchronized static String doGetLocationFromPhone(String num) {
        if (null == num) {
            return null;
        }
        if (num.equals(PHONE)) {
            return LOCATION;
        }
        LOCATION = getPhoneLocationJni(num);
        PHONE = num;
        return LOCATION;
    }

    private static String getPosFromPhone(String num, int i) {
        String s = doGetLocationFromPhone(num);
        String[] ss = null;
        if (null != s) {
            ss = s.split(",");
            if (ss.length == 2) {
                return ss[i];
            }
        }
        return null;
    }

    public static String getCodeFromPhone(String num) {
        return getPosFromPhone(num, 0);
    }

    public static String getCityFromPhone(CharSequence number) {
	if (TextUtils.isEmpty(number)) return "";
        String phoneLocation = getPosFromPhone(number.toString().replaceAll("(?:-| )", ""), 1);
        return (TextUtils.isEmpty(phoneLocation) ? "" : phoneLocation);

    }
}
