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
package com.busap.vcs.hlsmedia.http.hls;

import com.busap.vcs.hlsmedia.http.file.HttpStaticFileServerHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class HlsPlayFileServerHandler extends HttpStaticFileServerHandler {

    final HlsSettings settings;

    public HlsPlayFileServerHandler(HlsSettings settings) {
        super(settings.playMappingURIPath, settings.mappingLocalPath, true);
        this.settings = settings;
    }

    /**
     * 根据URI路径找到对应的文件
     * 比如 /hsl/play/x-xx-xxx-xxxx --> /hls/play/x/xx/xxx/xxxx.m3u8 --> mappingLocalPath/x/xx/xxx/xxxx.m3u8
     *
     * @param path 路径
     * @return
     */
    @Override
    public File find(String path) {
        // Decode the path.
        try {
            String requestUri = URLDecoder.decode(path, "UTF-8");
            String ext = FilenameUtils.getExtension(requestUri);
            String uuid = requestUri.substring(0, requestUri.length() - ext.length());
            // 除去?后的部分
            int idx = uuid.indexOf('?');
            if (idx >= 0) {
                uuid = uuid.substring(0, idx);
            }
            if (uuid.endsWith(".")) {
                uuid = uuid.substring(0, uuid.length() - 1);
            }
            String fullPath = UUIDFilenameUtils.toFullPath(uuid);
            if (StringUtils.isBlank(ext)) {
                ext = "m3u8";
            }
            // Convert to absolute path.
            path = FilenameUtils.normalize(mappingLocalPath + File.separator + fullPath + "." + ext);
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
        File file = new File(path);
        return file;
    }
}
