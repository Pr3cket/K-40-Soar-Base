<div align="center">
<p>
    <img width="256" src="logo.png" alt="K">
</p>

[Official Website of Soar Client](https://www.soarclient.com/)

[Chinese Version | 中文版](README.md)
</div>

# K-40

K-40 is a hacked client branch for Minecraft 1.8.9 developed based on Soar Client. Soar Client was written by EldoDebug, and the K-40 branch is maintained by Nan7i.

## Copyright Division

  - The original author EldoDebug retains all rights to the original Soar Client.
  - The secondary distributor Nan7i retains all rights to the overall derivative code.

## Open Source License

This project adopts the GPL-3.0 license. Any unauthorized secondary distribution and modification are prohibited. For details, please refer to the [LICENSE](licenses/LICENSE).
 
Special statement: The original project adopted the MIT license. For details, please refer to the [LICENSE-MIT](licenses/LICENSE-MIT).

## Project Contributions

  - Ported sound subtitles. Spent about 10h+ tidying up and converting IDs that didn't match due to the significant changes from Minecraft 1.8.9 to 1.9.
  - Wrote shaders and restored blurred rendering.
  - Switched Soar Client from Tweak Client to use Forge.
  - Optimized the module system.
  - Optimized the language system.
  - Optimized the cape system.
  - Integrated multiple well - known Minecraft mods ported by Nan7i, such as Apple Skin, Mo' Bends, Entity Culling, etc. Here, we would like to express our gratitude to the mod developers from different countries. Thank you for your creativity and dedication, which make the Minecraft world more colorful.
  - Integrated NetEase Cloud Music player.
  - Implemented offline account name modification.
  - Added the fakeplayer command from Carpet (for debugging only).
  - Optimized modules, removed redundant modules and events.
  - Fixed 50+ known issues covering aspects such as user interface, performance, and code maturity.
  - Almost modified all files, making the code more standardized, more readable, and with better algorithms and logic.

## Special Thanks

  - Some of the icons are from https://iconfont.cn. For license details, please refer to the [terms.alicdn.txt](licenses/terms.alicdn.txt).
  - The K logo for this project is authored by https://iconfont.cn/user/detail?spm=a313x.manage_type_mylikes.0.d214f71f6.74943a81nz2UXA&uid=4280358&nid=V6c7dllIc3yP.
  - The music player codes are from https://github.com/FengLiuFeseliud/CloudMusic-Mod (MIT license), Thank FengLiuFeseliud for the open-source sharing.
  - The cape materials are from https://minecraftcapes.net/gallery, Thank this platform for providing rich cape resources.

## Start

  - Environment Configuration (IDEA): Run `genIntellijRuns.bat`.
  - Debugging: Run `runClient.bat`.
  - Packaging: Run `releaseCache.bat`.
