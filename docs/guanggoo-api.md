# 过早客 API 列表

<!-- vim-markdown-toc GFM -->

* [登录](#登录)
* [获取主题列表](#获取主题列表)
* [主题详情](#主题详情)
* [获取节点列表](#获取节点列表)
* [评论](#评论)
* [个人信息页](#个人信息页)
    * [发表过的主题列表](#发表过的主题列表)
    * [回复列表](#回复列表)
    * [收藏列表](#收藏列表)
* [收藏](#收藏)
    * [收藏主题](#收藏主题)
    * [取消收藏](#取消收藏)
* [发表新主题](#发表新主题)
* [评论点赞](#评论点赞)
* [关注/取消关注](#关注取消关注)
* [消息提醒](#消息提醒)
    * [判断是否有新消息提醒](#判断是否有新消息提醒)
    * [消息提醒列表](#消息提醒列表)
* [屏蔽](#屏蔽)
    * [已屏蔽用户列表](#已屏蔽用户列表)
    * [判断是否屏蔽了某用户](#判断是否屏蔽了某用户)
    * [屏蔽用户](#屏蔽用户)
    * [取消屏蔽](#取消屏蔽)

<!-- vim-markdown-toc -->

## 登录

## 获取主题列表

默认排序：<https://www.guozaoke.com/>

最新话题：<https://www.guozaoke.com/?tab=latest>

精华主题：<https://www.guozaoke.com/?tab=elite>

节点主题列表：<https://www.guozaoke.com/node/xxxxx>

## 主题详情

URL: <https://www.guozaoke.com/t/25804>

## 获取节点列表

使用 GET 方法访问 <https://www.guozaoke.com/nodes>。

```html
<div class="nodes-cloud ...">
...
    <ul>
        <li>
            <label for>生活百科</label>
            <span class="nodes">
                <a href="/node/house">楼市房产</a>
                ...
            </span>
        </li>
        ...
    </ul>
...
</div>
```

## 评论

给主题 <https://www.guozaoke.com/t/25804> 评论 `太感动了……`。

**Request**

```
POST /t/25804 HTTP/1.1
Host: www.guozaoke.com
Content-Length: 111
Cache-Control: max-age=0
Origin: https://www.guozaoke.com
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36
Content-Type: application/x-www-form-urlencoded
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Referer: https://www.guozaoke.com/t/25804
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.8,en;q=0.6
Cookie: _xsrf=c449edebf6e442c5805ddf8d532770bd; verification="YTIxZTQwNDNlZDUyYmUwN2ZkMzQyYWI5NmNiNjdkODg0ODk1MWU4NGRhZjQ0MWYwMGM4MmRmNzliNGRmY2FkNA==|1506434801|958c9c6b5a47ee05bedbe085525b991f11098948"; session_id="YmY2OTM5NTlhYzI4ZTBmZTU0ZmJlOTBjMGVlYzYyOTY0ZDM2ZWVjNDAxNmU1NDU4ZDkwMGU4MzhhY2M4YzU2Ng==|1506434801|35ce40e8d515393026f726a996352bf580932a16"; user="MTE1NTQ=|1506434801|0809d1c35277d991395b664de4056e86f74c8e38"; _gat=1; Hm_lvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1506434797; Hm_lpvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1506434921; _ga=GA1.2.211778315.1506434797; _gid=GA1.2.1607821281.1506434797

tid=25804&content=%E5%A4%AA%E6%84%9F%E5%8A%A8%E4%BA%86%E2%80%A6%E2%80%A6&_xsrf=c449edebf6e442c5805ddf8d532770bd
```

**Response**

```
HTTP/1.1 302 Found
Date: Tue, 26 Sep 2017 14:09:01 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 0
Connection: keep-alive
Location: /t/25804#reply3
Server: TornadoServer/3.2
```

## 个人信息页

URL: <https://www.guozaoke.com/u/mzlogin>

可以从其它页面获取到。

获取用户基本信息：

```html
<div class="user-page">
    <div class="profile container-box">
        <div class="ui-header">
            <a href="/u/mzlogin">
                <img src="http://cdn.guozaoke.com/static/avatar/54/m_2fad3826-a776-11e6-a0b7-00163e020f08.png" alt="" class="avatar">
            </a>
            <div class="username">mzlogin</div>
            <div class="website"><a href="http://mazhuang.org">http://mazhuang.org</a></div>
            <div class="user-number">
                <div class="number">过早客第11554号成员</div>
                <div class="since">入住于2016-11-11</div>
            </div>
        </div>
        <div class="ui-content">
            <dl>
                <dt>ID</dt>
                <dd>mzlogin</dd>
            </dl>
            <dl>
                <dt>昵称</dt>
                <dd>mzlogin</dd>
            </dl>
            <dl>
                <dt>Email</dt>
                <dd>mzlo***@qq.com</dd>
            </dl>
            <dl>
                <dt>Blog</dt>
                <dd><a href="http://mazhuang.org">http://mazhuang.org</a></dd>
            </dl>
        </div>
    </div>
</div>
```

获取用户活动统计：

```html
<div class="usercard container-box">
    <div class="ui-content">
        <div class="status status-topic">
            <strong><a href="/u/mzlogin/topics">0</a></strong> 主题
        </div>
        <div class="status status-reply">
            <strong><a href="/u/mzlogin/replies">15</a></strong> 回复
        </div>
        <div class="status status-favorite">
            <strong><a href="/u/mzlogin/favorites">1</a></strong> 收藏
        </div>
        <div class="status status-reputation">
            <strong>0</strong> 信用
        </div>
    </div>
</div>
```

展示在用户个人信息页的基本信息这些就够了，发表过的主题列表和回复列表只显示数量，另外用页面单独列出，参考 <https://github.com/shitoudev/v2ex>。

### 发表过的主题列表

URL: <https://www.guozaoke.com/u/mzlogin/topics>

### 回复列表

URL: <https://www.guozaoke.com/u/mzlogin/replies>

```html
<div class="replies-lists ...">
    ...
    <div class="ui-content">
        <div class="reply-item">
            <div class="main">
                <span class="title">
                    回复了 dc2012ms 创建的主题
                    <a href="/t/25910">光谷是真心好！！！</a>
                </span>
                <div class="content">
                    <p>这几天不知道怎么样了</p>
                </div>
            </div>
        </div>
        <div class="reply-item">
            ...
        </div>
        ...
    </div>

    <div class="ui-footer">
        ...
        <ul class="pagination">
            <li class="disabled">
                <a href="/u/mzlogin/replies?p=1">上一页</a>
            </li>
            <li class="active">
                <a href="javascript:;">1</a>
            </li>
            ...
        </ul>
        ...
    </div>
</div>
```

### 收藏列表

URL: <https://www.guozaoke.com/u/mzlogin/favorites>

## 收藏

### 收藏主题

**Request**

```
GET https://www.guozaoke.com/favorite?topic_id=25869 HTTP/1.1
Accept: application/json, text/javascript, */*; q=0.01
X-Requested-With: XMLHttpRequest
Referer: https://www.guozaoke.com/t/25869
Accept-Language: zh-Hans-CN,zh-Hans;q=0.5
Accept-Encoding: gzip, deflate
User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; LCTE; rv:11.0) like Gecko
Host: www.guozaoke.com
Connection: Keep-Alive
Cookie: Hm_lvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1506732697; Hm_lpvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1506732725; _ga=GA1.2.427913396.1506732727; _gid=GA1.2.1767756529.1506732727; _gat=1; _xsrf=7deabf7bde3b45149513f4bb58e77c63; verification="NDhhMDQ2OWQ4YTkzZDk2YzBiNjg0NjA5NzQzYjY0YWFiNDRiYzQ2YTQxYmZkZDE5Y2MxZTdmMWIxMTJkNWY2MQ==|1506732718|f218f25cb28c6332fc89c0c881566733d75da0ec"; session_id="YmQ4ZmRhYzEwZjI0OTA5ZjZhMGI2ZTI0NzIxYWU4MGI5MjNmNDJiMWUwM2VkM2E0OWM2OTk0YjJkMmNlZTE5Yg==|1506732718|1be79cbef19d9f452faa5914288b97b47319562b"; user="MTE1NTQ=|1506732718|a881d8d2377805df8bd0e7917f7442de47416f02"


```

**Response**

```
HTTP/1.1 200 OK
Date: Sat, 30 Sep 2017 00:52:15 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 45
Connection: keep-alive
Etag: "f071a3a1e35db1fc03c43ec254ea523dc9017b4c"
Server: TornadoServer/3.2

{"message": "favorite_success", "success": 1}
```

### 取消收藏

**Request**

```
GET /unfavorite?topic_id=25884 HTTP/1.1
Host: www.guozaoke.com
Connection: keep-alive
Accept: application/json, text/javascript, */*; q=0.01
X-Requested-With: XMLHttpRequest
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36
Referer: https://www.guozaoke.com/t/25884
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.8,en;q=0.6
Cookie: verification="MWZkMTQ4YWMyZWI5OTViZDEyNzM0NTQ2MWI5MzY5MDJmOTQ2NTVmMmI5MzlhZWQzMTc4YmY3ZjFjOWYwNmUwNQ==|1505449242|cd6806b985b1659d0e1e813e5edb92aa2f05de4b"; session_id="ZTExYWY4NDAzZTA1NDg0NWViYzhhMTM2ZDUyNDQ2MjI0OGQwYWM1MjU5NWRlZDBjMGQwMjdjNjdkZWQ3NWMxYw==|1505449242|898c284efed1c85cb6a80211ff7b88d963456e8a"; user="MTE1NTQ=|1505449242|8338edc07e6b8ef60f11a6cf627b8e7ed96b26a5"; _xsrf=8958ec90a24b43328d83341349212956; _gat=1; _ga=GA1.2.606805923.1500596270; _gid=GA1.2.2032501243.1506493190; Hm_lvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1506730927,1506732617,1506732674,1506734767; Hm_lpvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1506734780
```

**Response**

```
HTTP/1.1 500 Internal Server Error
Date: Sat, 30 Sep 2017 01:26:38 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 93
Connection: keep-alive
Server: TornadoServer/3.2

<html><title>500: Internal Server Error</title><body>500: Internal Server Error</body></html>
```

## 发表新主题

**Request**

比如向「汤逊湖」节点发表新主题：

```
POST /t/create/water HTTP/1.1
Host: www.guozaoke.com
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Encoding: gzip, deflate
Accept-Language: zh-cn
Content-Type: application/x-www-form-urlencoded
Origin: https://www.guozaoke.com
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Safari/604.1.38
Connection: keep-alive
Upgrade-Insecure-Requests: 1
Referer: https://www.guozaoke.com/t/create/water
Content-Length: 392
Cookie: session_id="ZWY4NjQ5ZWIyZDYxNmMwYmJlYThhYjc4N2U3NTg0MDJjM2Y5MDgyNzFiNjgyNDAzZmUyOTYyZDQ5ZWFiOTMwOQ==|1507450364|cfc142508ad48aa5d8a30f0024bf3cb9183a2a56"; user="MjI3OTk=|1507450364|916f75319cd3777a44262f6ce288a9eadb506120"; verification="Y2VkZGU3MGExZTEzNDM4M2RlMTEwM2QyNWU4NmZjMzRiZTk0MGVkMzY1ODVlOTcxMjVhMDg0YjgxYjNlMTdhNg==|1507450364|8d10c5e88e426f98f8fcf43073e1169fce22e229"; _xsrf=5f4c4ebdf42d423fa296649d83d3560e; Hm_lpvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1507450519; Hm_lvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1506702379,1507085489,1507176778,1507275950; _ga=GA1.2.497625709.1478803477; _gid=GA1.2.652948611.1507390198

title=%E5%81%87%E6%9C%9F%E8%BF%9B%E5%BA%A6%E6%9D%A1%E5%90%9B%E5%B0%B1%E8%A6%81%E9%98%B5%E4%BA%A1%E4%BA%86%EF%BC%8C%E5%A4%A7%E5%AE%B6%E4%BC%99%E8%BF%99%E4%BC%9A%E5%84%BF%E5%BF%83%E6%83%85%E5%A6%82%E4%BD%95%EF%BC%9F&content=RT%EF%BC%8C%0D%0A%0D%0A%E5%8F%8D%E6%AD%A3%E6%88%91%E7%9C%9F%E7%9A%84%E5%A5%BD%E6%83%B3%E5%86%8D%E7%98%AB%E5%87%A0%E5%A4%A9%E3%80%82&_xsrf=5f4c4ebdf42d423fa296649d83d3560e
```

**Response**

```
HTTP/1.1 302 Found
Date: Sun, 08 Oct 2017 08:16:34 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 0
Connection: keep-alive
Location: /
Server: TornadoServer/3.2

```

## 评论点赞

**Request**

比如赞 <https://www.guozaoke.com/t/30372> 的 12 楼：

```
GET /replyVote?reply_id=203544 HTTP/1.1
Host: www.guozaoke.com
Connection: keep-alive
Accept: application/json, text/javascript, */*; q=0.01
X-Requested-With: XMLHttpRequest
User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36
Referer: https://www.guozaoke.com/t/30372
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7
Cookie: _ga=GA1.2.606805923.1500596270; _gid=GA1.2.1973807385.1523923870; Hm_lvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1523923870; _xsrf=a05bac9512b94633a61b75e9b7ad4f29; verification="MzU4ZjAwZmY1YjE3YzIwMzkxZDYwZmNlM2QxZDU2NWMyOGNhZDQyMjI0YzYwMTAxNjBlZDgxYTIwOGM1ZGUzYg==|1523923873|8f5b1363dc248e86bf2c60884ab9f949bdacab39"; session_id="NzhmZDlkNjIzOWFjNjlkYmY1ZGExMTJkMDFhMTI2ZTgwM2I3MzQ4ZjRiYzE2ZjJkODA5ZjVmYTUxNDNlNTEyZQ==|1523923873|0f58766c33ef471d1ebcbace76b5193d212b2423"; user="MTE1NTQ=|1523923873|262509f7375f8b899a9eb2a98653ea1a47bc7917"; _gat=1; Hm_lpvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1523923936
```

**Response**

如果还未赞过该评论：

```
{"message": "thanks_for_your_vote", "success": 1}
```

如果已经赞过该评论：

```
{"message": "already_voted", "success": 0}
```

## 关注/取消关注

关注和取消关注都是发相同的请求，请求之后都是需要重新请求个人信息页来判断是否关注和取消关注成功。

**Request**

```html
GET /f/user/youngway HTTP/1.1
Host: www.guozaoke.com
Connection: keep-alive
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Mobile Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Referer: https://www.guozaoke.com/u/youngway
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7
Cookie: _ga=GA1.2.211778315.1506434797; _xsrf=5f39918216ed46fa81444294037731d3; Hm_lvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1524528530,1524544856,1524643298,1524711283; _gid=GA1.2.2070239126.1524922970; verification="MjQ5NWYzNWQ5YTZkODA1ZmY5ZThlYzYzNTc0NjQ5MmU2OTE4ZGE0YmNhZjNiNDY3NmY4ZmQ5ZTZhZGRjMzk0OA==|1524985028|2bb9253ff306142259330ab8610db9b763694ae7"; session_id="MTYyODUxYzc4NzcwNTQ0NDFmYWQ4OWJmOGU1ZjgwNjVlNjBmZmVmMjBiYWFjMTNmOTZiOTg2YjdhYTRmZGVkZA==|1524985028|3320799a0d0b37e855c74088c3fed35e65b83a23"; user="MTE1NTQ=|1524985028|1be1826342cb95dbd6e3255145407b7bc3e48c6c"; Hm_lpvt_fc1abeddfec5c3ea88cf6cdae32cdde7=1524987630
```

**Response**

```html
HTTP/1.1 302 Found
Date: Sun, 29 Apr 2018 07:46:00 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 0
Connection: keep-alive
Location: /u/youngway
Server: TornadoServer/3.2
```

Web 上会自动跳转到个人信息页，已经关注的情况：

```html
<div class="user-page">
  <div class="profile container-box">
    <div class="ui-header">
      <a href="/u/youngway">
        <img src="http://cdn.guozaoke.com/static/avatar/52/m_default.png" alt="" class="avatar" />
      </a>
      <div class="username">youngway</div>
      <span class="label label-success" style="margin-left:4px"><a href="/f/user/youngway" style="color:#FFFFFF">取消关注</a></span>
      <div class="user-number">
        <div class="number">过早客第27152号成员</div>
        <div class="since">入住于2018-04-25</div>
      </div>
    </div>
    <div class="ui-content">
      <dl>
        <dt>ID</dt>
        <dd>youngway</dd>
      </dl>
      <dl>
        <dt>Email</dt>
        <dd>1067582***@qq.com</dd>
      </dl>
    </div>
  </div>
</div>
```

```html
<div class="user-page">
  <div class="profile container-box">
    <div class="ui-header">
      <a href="/u/youngway">
        <img src="http://cdn.guozaoke.com/static/avatar/52/m_default.png" alt="" class="avatar" />
      </a>
      <div class="username">youngway</div>
      <span class="label label-success" style="margin-left:4px"><a href="/f/user/youngway" style="color:#FFFFFF">+关注</a></span>
      <div class="user-number">
        <div class="number">过早客第27152号成员</div>
        <div class="since">入住于2018-04-25</div>
      </div>
    </div>
    <div class="ui-content">
      <dl>
        <dt>ID</dt>
        <dd>youngway</dd>
      </dl>
      <dl>
        <dt>Email</dt>
        <dd>1067582***@qq.com</dd>
      </dl>
    </div>
  </div>
</div>
```

## 消息提醒

### 判断是否有新消息提醒

这个不需要在特定的页面判断，基本所有页面都带有这个信息，实际编码的时候，选部分页面加入判断逻辑和发出通知即可。

```xml
<body>
<nav class="navbar navbar-default top-navbar">
<div class="container-fluid container">
<a href="/notifications" class="notification-indicator tooltipped downwards contextually-unread" title="mzlogin，你有4条未读提醒，去看看吧">
<span class="mail-status unread"></span>
</a>
</div>
</nav>
</body>
```

有未读消息时，a 标签才会有 `contextually-unread` 类，在 title 里有未读消息条数信息。

### 消息提醒列表

消息提醒分两种类型:

1. 别人 AT 你；

2. 别人回复了你的主题；

```xml
<div class="notifications container-box">
<div class="ui-content">
<div class="notification-item">
    <a href="/u/kanqn">
        <img src="http://cdn.guozaoke.com//static/avatar/60/m_a649dc2e-cdd0-11e7-a0b7-00163e020f08.png" alt="" class="avatar">
    </a>
    <div class="main">
        <span class="title"><a href="/u/kanqn">kanqn</a> 回复了你的主题 <a href="/t/36049">一份简明的 Markdown 笔记与教程</a></span>
        <div class="content"><p>这几天在朋友圈看到推文了<br>
            嗯，学好了，又可以去装逼了。<br>
            就是这样(๑•̀ㅁ•́๑)✧</p>
        </div>
    </div>
</div>
<div class="notification-item">
    <a href="/u/jiujiujiu">
        <img src="http://cdn.guozaoke.com//static/avatar/84/m_7d822faa-51cd-11e7-a0b7-00163e020f08.png" alt="" class="avatar">
    </a>
    <div class="main">
        <span class="title"><a href="/u/jiujiujiu">jiujiujiu</a> 回复了你的主题 <a href="/t/36049">一份简明的 Markdown 笔记与教程</a></span>
        <div class="content"><p>感谢！学习！</p>
        </div>
    </div>
</div>
<div class="notification-item">
    <a href="/u/binjoo">
        <img src="http://cdn.guozaoke.com//static/avatar/13/m_51e9b48c-b8ca-11e5-a0b7-00163e020f08.png" alt="" class="avatar">
    </a>
    <div class="main">
        <span class="title"><a href="/u/binjoo">binjoo</a> 回复了你的主题 <a href="/t/36049">一份简明的 Markdown 笔记与教程</a></span>
        <div class="content"><p>用markdown有几年了，才知道后面加空格还能换行的。</p>
        </div>
    </div>
</div>
<div class="notification-item">
    <a href="/u/kanqn">
    <img src="http://cdn.guozaoke.com//static/avatar/60/m_a649dc2e-cdd0-11e7-a0b7-00163e020f08.png" alt="" class="avatar">
    </a>
    <div class="main">
        <span class="title"><a href="/u/kanqn">kanqn</a> 在 <a href="/t/35624">Visio学习视频有吗？</a> 中提到了你</span>
        <div class="content"><p><a target="_blank" href="/u/mzlogin" class="user-mention">@mzlogin</a> 那三个是下午和傍晚总共花了一个半小时画的</p>
        </div>
    </div>
</div>
```

## 屏蔽

### 已屏蔽用户列表

URL: https://www.guozaoke.com/setting/blockedUser

```html
<div class="member-lists container-box">
    <div class="ui-header">
        <span class="title">已屏蔽用户</span>
    </div>
    <div class="ui-content">
        <div class="member">
            <a href="/u/zuoluo032">
            <img src="http://cdn.guozaoke.com//static/avatar/66/m_default.png" alt="" class="avatar">
            </a>
            <span class="username">
            <a href="/u/zuoluo032">zuoluo032</a>
            </span>
        </div>
        <div class="member">
            <a href="/u/muchen">
            <img src="http://cdn.guozaoke.com//static/avatar/4/m_60714b1c-84d5-11e8-a0b7-00163e134dca.png" alt="" class="avatar">
            </a>
            <span class="username">
            <a href="/u/muchen">muchen</a>
            </span>
        </div>
    </div>
</div>
```

### 判断是否屏蔽了某用户

打开用户个人资料页

```html
<div class="col-md-3 sidebar-right mt10">
    <div class="user-page">
    <!-- some contents -->
        <div class="self-introduction container-box mt10">
            <div class="ui-content">
                <strong><a href="/u/30804/block">屏蔽此帐号</a></strong>
            </div>
        </div>

    </div>
</div>
```

### 屏蔽用户

**request**

```
GET /u/30804/block HTTP/1.1
Host: www.guozaoke.com
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,fr;q=0.6,nb;q=0.5
Cookie: _ga=GA1.2.1046187808.1560091264; _xsrf=1b707684cfa94e958081aeb741c8df0a; _dx_uzZo5y=5b18d5da2f646d27d30273c14413d1af94b32512640168269d58b8bb9310e77eba7df874; verification="NDIzNzRlODJkYjJjMWY0MDFmZGIzODRmMjczMWYxZTgxMWM1OTBhMjhmMmFlNDk2ZjhlOTI3ZGZhNTA2ZWMzZA==|1561176819|237f2d37d4ee90bccde6d5ecc04f4ae12e4b07f8"; session_id="NDI3MzVkOWU0ZGU2OGVlMjc0ZTJlNTcwNTFhNDIzOTdhOTdjZjA5YWI5ODY2MWJjNTZiYTgzNzNjZGQxOWYxNA==|1561176819|fea2f0097c76cf817e6264384d87e967223ad2be"; user="MjI4Nzg=|1561176819|98fbc5bfc4b3a21385bc147aba676907012b70d4"; _dx_app_64dbb3e602c0f5a34ad58387da5aa310=5d18c775kgRMtSyRBXkBlSFmTpKZnxND7Ffev141; _gid=GA1.2.687290872.1563660719; _gat=1
```

**response**

```
HTTP/1.1 302 Found
Date: Sat, 20 Jul 2019 22:17:24 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 0
Connection: keep-alive
Location: /u/muchen
Server: TornadoServer/3.2
```

### 取消屏蔽

**request**

```
GET /u/30804/unblock HTTP/1.1
Host: www.guozaoke.com
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,fr;q=0.6,nb;q=0.5
Cookie: _ga=GA1.2.1046187808.1560091264; _xsrf=1b707684cfa94e958081aeb741c8df0a; _dx_uzZo5y=5b18d5da2f646d27d30273c14413d1af94b32512640168269d58b8bb9310e77eba7df874; verification="NDIzNzRlODJkYjJjMWY0MDFmZGIzODRmMjczMWYxZTgxMWM1OTBhMjhmMmFlNDk2ZjhlOTI3ZGZhNTA2ZWMzZA==|1561176819|237f2d37d4ee90bccde6d5ecc04f4ae12e4b07f8"; session_id="NDI3MzVkOWU0ZGU2OGVlMjc0ZTJlNTcwNTFhNDIzOTdhOTdjZjA5YWI5ODY2MWJjNTZiYTgzNzNjZGQxOWYxNA==|1561176819|fea2f0097c76cf817e6264384d87e967223ad2be"; user="MjI4Nzg=|1561176819|98fbc5bfc4b3a21385bc147aba676907012b70d4"; _dx_app_64dbb3e602c0f5a34ad58387da5aa310=5d18c775kgRMtSyRBXkBlSFmTpKZnxND7Ffev141; _gid=GA1.2.687290872.1563660719
```

**response**

```
HTTP/1.1 302 Found
Date: Sat, 20 Jul 2019 22:20:14 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 0
Connection: keep-alive
Location: /u/muchen
Server: TornadoServer/3.2
```
