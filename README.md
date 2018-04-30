# guanggoo-android

[光谷社区](http://www.guanggoo.com) 第三方客户端。

**目录**
<!-- vim-markdown-toc GFM -->

* [下载地址](#下载地址)
* [屏幕截图](#屏幕截图)
* [功能列表](#功能列表)
* [界面特性](#界面特性)
* [优化](#优化)
* [API](#api)
* [参与贡献](#参与贡献)
* [相关项目](#相关项目)
* [License](#license)

<!-- vim-markdown-toc -->

## 下载地址

| Google Play                                                                                                                                                                                | 小米应用商店                                                                                                                                                    | 酷安                                                                                                                                             | GitHub                                                           |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------|
| <a href="https://play.google.com/store/apps/details?id=org.mazhuang.guanggoo" target="_blank"><img alt="Google Play" width="183px" height="39px" src="./assets/image/play-store.png"/></a> | <a href="http://app.mi.com/details?id=org.mazhuang.guanggoo" target="_blank"><img alt="小米应用商店" height="39px" src="./assets/image/xiaomi-market.png"/></a> | <a href="https://www.coolapk.com/apk/org.mazhuang.guanggoo" target="_blank"><img alt="酷安" height="39px" src="./assets/image/coolapk.png"/></a> | [Releases](https://github.com/mzlogin/guanggoo-android/releases) |

> 如需 iOS 版请移步 [相关项目](#相关项目)

## 屏幕截图

<img width="360" src="http://mazhuang.org/guanggoo-android/screenshots/topic-list.png" align=center /> <img width="360" src="http://mazhuang.org/guanggoo-android/screenshots/topic-detail.png" align=center />

<img width="360" src="http://mazhuang.org/guanggoo-android/screenshots/nodes-list.png" align=center /> <img width="360" src="http://mazhuang.org/guanggoo-android/screenshots/drawer.png" align=center />

## 功能列表

- [x] 登录
- [x] 首页主题列表
    三种视图：默认排序、最新话题、精华主题
- [x] 主题详情
    - [x] 主题内容
    - [x] 评论列表
- [x] 节点列表
- [x] 节点主题列表
- [x] 评论
    - [x] 文字评论
    - [x] emoji
    - [x] 艾特
        目前仅支持长按头像或用户名，还有点击评论的回复按钮
    - [x] 点赞
- [x] 分享主题链接
- [x] 新手指南
- [x] 个人信息页
    - [x] 个人基本信息
    - [x] 个人回复列表
    - [x] 个人主题列表
    - [x] 其它用户信息
- [ ] 消息提醒
- [x] 收藏
- [x] 收藏的主题列表
- [x] 发表新主题
- [x] 登出
- [ ] 注册
- [ ] 搜索
- [ ] 屏蔽
- [ ] 已读/未读状态区分

## 界面特性

- [x] 主题详情支持动图和视频
- [x] ToolBar 设定
    - [x] 如果当前 Fragment 栈里的数量大于 1，就显示返回按钮，可以滑出 Drawer，否则显示菜单，锁定 Drawer
    - [x] 在合适的时候显示右侧菜单
- [x] 列表下拉刷新
- [x] 列表上滑加载更多
    - [x] 主题列表页自动加载
    - [x] 主题评论点击手动加载
- [x] Loading 动画
- [ ] 应用内处理图片与链接点开
- [x] 评论内容为空时评论按钮置灰
- [ ] 侧滑返回
- [ ] 评论按钮可以考虑做成 FloatActionButton
- [ ] 夜间模式

## 优化

- [ ] 添加缓存

## API

[docs/guanggoo-api.md](./docs/guanggoo-api.md)

因为光谷社区并未提供 API，所以是基于 DOM 解析获取数据。网站的前端界面改动有可能导致数据不可用，可以考虑做一个 API 监控脚本，定期测试 API 的可用性。

## 参与贡献

请参考 [如何参与贡献](./CONTRIBUTING.md)

## 相关项目

* [sjjvenu/guanggoo-iOS](https://github.com/sjjvenu/guanggoo-iOS)

    光谷社区 iOS 版，已经在 App Store 上架，基于 Swift。

* [cauil/react-native-guanggoo](https://github.com/cauil/react-native-guanggoo)

    光谷社区 iOS 版，基于 React Native。

## License

[Apache License 2.0](https://github.com/mzlogin/guanggoo-android/blob/master/LICENSE)
