/*
 * Copyright (C) 2014 Hari Krishna Dulipudi
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

package com.data.volley.toolbox;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.net.http.AndroidHttpClient;

import com.data.volley.Network;
import com.data.volley.NetworkResponse;
import com.data.volley.RequestQueue;
import com.data.volley.RequestTickle;
import com.data.volley.cache.DiskBasedCache;
import com.data.volley.misc.NetUtils;
import com.data.volley.misc.Utils;

public class VolleyTickle {

    /** Default on-disk cache directory. */
    private static final String DEFAULT_CACHE_DIR = "volley";

    /**
     * Creates a default instance of the worker pool and calls {@link com.data.volley.RequestTickle#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link com.data.volley.toolbox.HttpStack} to use for the network, or null for default.
     * @return A started {@link com.data.volley.RequestTickle} instance.
     */
    public static RequestTickle newRequestTickle(Context context, HttpStack stack) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        if (stack == null) {
            stack = Utils.hasHoneycomb() ?
                    new HurlStack() :
                    new HttpClientStack(AndroidHttpClient.newInstance(
                            NetUtils.getUserAgent(context)));
        }

        Network network = new BasicNetwork(stack);


        RequestTickle tickle = new RequestTickle(new DiskBasedCache(cacheDir), network);

        return tickle;
    }

    /**
     * Creates a default instance of the worker pool and calls {@link RequestTickle#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestTickle} instance.
     */
    public static RequestTickle newRequestTickle(Context context) {
        return newRequestTickle(context, null);
    }
    
    public static String parseResponse(NetworkResponse response){
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
		return parsed;
    }
}