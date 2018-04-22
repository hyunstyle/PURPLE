package com.hyunstyle.inhapet;

/*
 * Copyright 2017 Realm Inc. ( for bytesToHex ) / Copyright (C) 2018 Wasabeef (for dip2px )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.UUID;

public class Util {
    //private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    // Original source: https://stackoverflow.com/a/9855338/1389357
    public static String bytesToHex(byte[] bytes, String prefix) {

        char[] hexArray = prefix.toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Original source: https://github.com/wasabeef/glide-transformations/blob/master/example/src/main/java/jp/wasabeef/example/glide/Utils.java
    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static synchronized String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        String line = "";
        StringBuilder sb = new StringBuilder(); // string concatenation performance -> use StringBuilder (cuz String -> immutable)
        while ((line = bufferedReader.readLine()) != null)
            sb.append(line);
        inputStream.close();
        return sb.toString();
    }
}
