/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.busap.vcs.hlsmedia.http.file;

import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 处理site下需要的一些配置文件,比如flash的跨域
 */
public class SiteDefFileServerHandler extends HttpStaticFileServerHandler {

    static Set<String> mapppings = new HashSet<String>();

    static {
        mapppings.add("crossdomain.xml");
        // jwplayer
        mapppings.add("jwplayer.flash.swf");
        mapppings.add("jwplayer.js");
        mapppings.add("jwplayer.HLSProvider6.swf");

    }

    public SiteDefFileServerHandler() {
        super("/", "./", true);
    }

    /**
     * Returns {@code true} if the given message should be handled. If {@code false} it will be passed to the next
     * {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel.ChannelPipeline}.
     */
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (super.acceptInboundMessage(msg)) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            String uri = fullHttpRequest.getUri();
            String path = uri.substring(this.mappingUriPath.length(), uri.length());
            if (!StringUtils.contains(path, '/')) {
                String name = FilenameUtils.getName(uri);
                if (mapppings.contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }


}
