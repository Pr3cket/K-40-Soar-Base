package com.lawab1ders.nan7i.kali.nanovg;

/*
 * Copyright (c) 2025 EldoDebug, Nan7i.南起
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.utils.IOUtils;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NSVGImage;
import org.lwjgl.nanovg.NanoSVG;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class AssetsManager implements InstanceAccess {

    @Getter
    private final HashMap<String, NVGAsset> imageCache = new HashMap<>();

    @Getter
    private final HashMap<Integer, Integer> glTextureCache = new HashMap<>();
    private final HashMap<String, NVGAsset> svgCache = new HashMap<>();

    //---------------------------------------------------------------------------
    // Image Loaders
    //---------------------------------------------------------------------------

    public boolean loadImage(long nvg, int texture, float width, float height) {
        if (!glTextureCache.containsKey(texture)) {
            // 我不知道这样做是否正确
            glTextureCache.put(
                    texture,
                    NanoVGGL2.nvglCreateImageFromHandle(nvg, texture, (int) width, -(int) height, 0)
            );
        }

        return true;
    }

    public boolean loadImage(long nvg, ResourceLocation location) {
        return loadImageInternal(nvg, location, location.getResourcePath());
    }

    public boolean loadImage(long nvg, File file) {
        return loadImageInternal(nvg, file, file.getName());
    }

    private boolean loadImageInternal(long nvg, Object resource, String cacheKey) {
        if (!imageCache.containsKey(cacheKey)) {
            int[] width = { 0 };
            int[] height = { 0 };
            int[] channels = { 0 };

            ByteBuffer image = null;

            if (resource instanceof ResourceLocation) {
                image = IOUtils.resourceToByteBuffer((ResourceLocation) resource);
            }
            else if (resource instanceof File) {
                image = IOUtils.resourceToByteBuffer((File) resource);
            }

            if (image == null) {
                return false;
            }

            ByteBuffer buffer = STBImage.stbi_load_from_memory(image, width, height, channels, 4);

            if (buffer == null) {
                return false;
            }

            imageCache.put(
                    cacheKey, new NVGAsset(
                            NanoVG.nvgCreateImageRGBA(
                                    nvg, width[0], height[0],
                                    NanoVG.NVG_IMAGE_REPEATX | NanoVG.NVG_IMAGE_REPEATY | NanoVG.NVG_IMAGE_GENERATE_MIPMAPS,
                                    buffer
                            ),
                            width[0], height[0]
                    )
            );

            return true;
        }

        return true;
    }

    public boolean loadSVG(long nvg, ResourceLocation location, float width, float height) {

        String name = location.getResourcePath() + "-" + width + "-" + height;

        if (!svgCache.containsKey(name)) {

            try {

                InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();

                if (inputStream == null) {
                    return false;
                }

                StringBuilder resultStringBuilder = new StringBuilder();

                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        resultStringBuilder.append(line);
                    }
                }

                CharSequence s = resultStringBuilder.toString();
                NSVGImage svg = NanoSVG.nsvgParse(s, "px", 96f);

                if (svg == null) {
                    return false;
                }

                long rasterizer = NanoSVG.nsvgCreateRasterizer();

                int w = (int) svg.width();
                int h = (int) svg.height();

                float scale = Math.max(width / w, height / h);
                w = (int) (w * scale);
                h = (int) (h * scale);

                ByteBuffer image = MemoryUtil.memAlloc(w * h * 4);
                NanoSVG.nsvgRasterize(rasterizer, svg, 0, 0, scale, image, w, h, w * 4);

                NanoSVG.nsvgDeleteRasterizer(rasterizer);
                NanoSVG.nsvgDelete(svg);

                svgCache.put(
                        name, new NVGAsset(
                                NanoVG.nvgCreateImageRGBA(
                                        nvg, w, h,
                                        NanoVG.NVG_IMAGE_REPEATX | NanoVG.NVG_IMAGE_REPEATY | NanoVG.NVG_IMAGE_GENERATE_MIPMAPS,
                                        image
                                ), w, h
                        )
                );

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    //---------------------------------------------------------------------------
    // Misc
    //---------------------------------------------------------------------------

    public int getImage(ResourceLocation location) {
        return imageCache.get(location.getResourcePath()).getImage();
    }

    public int getImage(File file) {
        return imageCache.get(file.getName()).getImage();
    }

    public int getImage(int texture) {
        return glTextureCache.get(texture);
    }

    public int getSVG(ResourceLocation location, float width, float height) {
        String name = location.getResourcePath() + "-" + width + "-" + height;

        return svgCache.get(name).getImage();
    }
}
