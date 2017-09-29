# 光谷社区 API 列表

<!-- vim-markdown-toc GFM -->

* [登录](#登录)
* [获取主题列表](#获取主题列表)
* [主题详情](#主题详情)
* [获取节点列表](#获取节点列表)
* [评论](#评论)
* [个人信息页](#个人信息页)
    * [发表过的主题列表](#发表过的主题列表)
    * [回复列表](#回复列表)

<!-- vim-markdown-toc -->

## 登录

## 获取主题列表

默认排序：<http://www.guanggoo.com/>

最新话题：<http://www.guanggoo.com/?tab=latest>

精华主题：<http://www.guanggoo.com/?tab=elite>

节点主题列表：<http://www.guanggoo.com/node/xxxxx>

## 主题详情

URL: <http://www.guanggoo.com/t/25804>

## 获取节点列表

使用 GET 方法访问 <http://www.guanggoo.com/nodes>。

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

给主题 <http://www.guanggoo.com/t/25804> 评论 `太感动了……`。

**Request**

```
POST /t/25804 HTTP/1.1
Host: www.guanggoo.com
Content-Length: 111
Cache-Control: max-age=0
Origin: http://www.guanggoo.com
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36
Content-Type: application/x-www-form-urlencoded
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Referer: http://www.guanggoo.com/t/25804
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

URL: <http://www.guanggoo.com/u/mzlogin>

可以从其它页面获取到。

获取用户基本信息：

```html
<div class="user-page">
    <div class="profile container-box">
        <div class="ui-header">
            <a href="/u/mzlogin">
                <img src="http://cdn.guanggoo.com/static/avatar/54/m_2fad3826-a776-11e6-a0b7-00163e020f08.png" alt="" class="avatar">
            </a>
            <div class="username">mzlogin</div>
            <div class="website"><a href="http://mazhuang.org">http://mazhuang.org</a></div>
            <div class="user-number">
                <div class="number">光谷社区第11554号成员</div>
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

URL: <http://www.guanggoo.com/u/mzlogin/topics>

### 回复列表

URL: <http://www.guanggoo.com/u/mzlogin/replies>
