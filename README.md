<div align="center">
<p>
    <img width="256" src="logo.png" alt="K">
</p>

[Soar Client 官方网站](https://www.soarclient.com/)

[English Version | 英文版](README-EN.md)
</div>

# 钾-40

K-40 是基于 Soar Client 开发的适用于 Minecraft 1.8.9 的作弊客户端分支。Soar Client 由 EldoDebug 编写，而 K-40 分支由 南起 维护。

## 版权划分

  - 原作者 EldoDebug 对原始 Soar Client 保留所有权益。
  - 二次分发作者 南起 对整体衍生代码保留所有权益。

## 开源协议

本项目采用 GPL-3.0 协议，禁止任何未经授权的二次分发与修改。详情请参阅 [LICENSE](licenses/LICENSE)。

特别声明：原项目采用 MIT 协议。详情请参阅 [LICENSE-MIT](licenses/LICENSE-MIT)。

## 项目贡献

  - 移植声音字幕。花费约 10h+ 整理并转换因 Minecraft 1.8.9 到 1.9 的巨大改动而不匹配的ID。
  - 编写着色器，恢复模糊渲染。
  - 将 Soar Client 从 Tweak Client 改用 Forge。
  - 优化模块系统。
  - 优化语言系统。
  - 优化披风系统。
  - 内置多个由 南起 移植的知名 Minecraft 模组，如：苹果皮、更多弯曲、实体渲染机制优化等。在此向这些来自不同国家的模组开发者致谢，感谢你们的创意与付出，让 Minecraft 的世界更加丰富多彩。
  - 内置网易云音乐播放器。
  - 实现离线账户名修改。
  - 地毯端的生成假人指令（仅用于调试）。
  - 优化模块，移除冗余模块与事件。
  - 修复 50+ 已知问题，涵盖用户界面、性能、代码成熟度等方面。
  - 几乎修改了所有文件，使它拥有更规范的代码，更高的可读性，更优的算法与逻辑。

## 特别致谢

  - 部分图标来源于 https://iconfont.cn ，协议详情请参阅 [terms.alicdn.txt](licenses/terms.alicdn.txt)。
  - 本项目的 K 徽标作者 https://iconfont.cn/user/detail?spm=a313x.manage_type_mylikes.0.d214f71f6.74943a81nz2UXA&uid=4280358&nid=V6c7dllIc3yP 。
  - 音乐播放器代码来源于 https://github.com/FengLiuFeseliud/CloudMusic-Mod （MIT 协议），感谢 ✟昇天✟ 的开源分享。
  - 披风素材来源于 https://minecraftcapes.net/gallery ，感谢该平台提供丰富的披风资源。

## 开始

  - 环境配置（IDEA）：运行 `genIntellijRuns.bat`。
  - 调试：运行 `runClient.bat`。
  - 打包：运行 `releaseCache.bat`。
