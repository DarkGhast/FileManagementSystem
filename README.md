#  文件管理系统

##  功能

1. 上传、下载文件，对保存在服务器的文件的文件名进行修改
2. 管理用户信息

##  框架

### 前端

1. 使用Bootstrap框架进行UI设计

###  后端

1. 使用Spring MVC进行Controller的管理
2. 使用Mybatis进行数据库的访问
3. 使用spring进行bean的管理

## 技术

### 前端

1. 使用JQuery的ajax实现前后端分离的交互
2. 采取分页查询，更加美观的同时提高效率

### 后端

#### 上传功能

1. 采用散列存储，避免同一个目录出现太多文件，优化存储结构
2. 通过读取文件的MD5+Sha1+size，在存储介质中存在相同文件的情况下，不再将上传的文件进行存储，而是直接将文件信息存入数据库，并指向介质中的文件。降低存储压力的同时，由于使用该技术，用户上传相同文件时用时大幅度缩短，提升用户体验。
3. 采取`时间戳-SessionID-GUID-`的字符串作为前缀，保证同名文件不会被覆盖保存

####  整体

1. 使用过滤器防止非权限用户访问敏感资源




------

# TODO

## 功能实现

###  外链保存

> 1. 保存外链的下载链接以及提取码（单纯的记录，另外要满足自动将提取码复制到剪切板）
>
> 2. *外链离线下载*
>
>    > 1. 首先，需要提取码的无法完成下载，各个网盘逻辑不同
>    > 2. 怎么判断链接打开后是下载还是网页

### 文件传输

***断点续传下载***

> Struts框架不支持断点续传，需要自己写servlet

###  管理员的文件级联删除

> 相同识别码的文件一次删除

###  *文件夹形式的虚拟存储*

> 1. 文件的信息中需添加**所属目录**字段
> 2. 展现给管理员的应该是什么样子的？
> 3. *移动文件位置*
> 4. 目录的存储方式
>    >方案
>    >1. 作为目录单独存放一张表，但是不易进行**管理**操作
>    >2. 作为文件，和文件同存一张表，以**文件类型**（文件/目录）进行区分，但是可能会造成数据库浪费大量空间
>    >
>    >计划
>    >
>    >1. 设置总根目录为0
>    >2. 在每个用户被创建时创建一个用户的根目录，其父目录为0

###  *权限相关（大版本更新）*

#### 方法

>------
>
>使用Spring Security还是Apache Shiro？
>
>[Spring Security框架的简单使用（权限控制，登录，登录加密）](https://www.cnblogs.com/qianyi525/p/13837440.html)
>
>### **相同点：**
>
>  1：认证功能
>
>  2：授权功能
>
>  3：加密功能
>
>  4：会话管理
>
>  5：缓存支持
>
>  6：rememberMe功能.......
>
>### **不同点：**
>
>####    **优点：**
>
>   1：Spring Security基于Spring开发，项目中如果使用Spring作为基础，配合Spring Security做权限更加方便，而Shiro需要和Spring进行整合开发
>
>   2：Spring Security功能比Shiro更加丰富些，例如安全防护
>
>   3：Spring Security社区资源比Shiro丰富
>
>####    **缺点：**
>
>   1：Shiro的配置和使用比较简单，Spring Security上手复杂
>
>   2：Shiro依赖性低，不需要任何框架和容器，可以独立运行，而Spring Security依赖于Spring容器 
>
>------

#### TODO

完成后端的文件条件查询

>------
>
>#### 用户与权限（需要一套做完）
>
>> 1. **用户信息加密传输**
>> 2. **权限系统**
>>    > 1. 超级管理员，管理所有用户/管理员与文件（global administrator）
>>    > 2. 管理员，管理普通用户与所有文件（administrator）
>>    > 3. 普通用户，管理自己的文件（user）
>
>------
>
>####  *文件分享功能（与权限系统同步）*
>> 1. 文件公开度（私有、公开）*（与权限系统紧密关联）*
>> 2. 文件分享
>>   3. *通过链接分享文件*
>>      > 1. 通过文件的保存名前缀，以某种可解密的加密方式加密后获得的字符串作为识别码
>>      > 2. 链接的格式定为 `域名/share/'加密' or '公开'?'识别码'=$识别码`
>>      >    
>>      >    > 加密链接输入提取码后返回失败或成功，返回成功时跳转的链接应检查使用链接者是通过正常输入提取码进入还是直接访问，直接访问时需跳转回需要输提取码的页面。
>>      > 3. *链接的有效性检测*
>>      >    > 1. 在文件被删除后需要返回**链接失效**的提示
>>      >    > 2. 在进入分享页面，点击保存按钮后，先检测文件是否失效，失效刷新页面以跳转到链接失效的页面。仍有效则进入保存流程。
>>   4. 需要模态框登录（分享链接应是**公开**的，因此需要进入分享页面后再登录）
>>      ------



------

# 更新日志

## <Version	1.3.0>

> ##  前端优化
>
> 1. ~~域名通过定义变量存储，方便部署~~
>
> 2. ~~已登录的用户单击主页按钮不再次登录~~
>
>    > .ajax获取当前用户名，为空则未登录，否则为已登录
>
> 3. ~~右上角显示当前登录的用户~~
>
> 4. ~~优化模态框显示~~
>
> ## 后端优化
>
> ###  上传
>
> 1. ~~文件确定唯一性的方式改为MD5+SHA1+size~~
> 2. ~~存储根目录通过配置文件读取~~
>
> ## 功能实现
>
> ~~修改用户登录方式为邮箱登录~~
>
> > 用户名应只作为昵称
>
> ~~用户删除时检查该用户是否上传过文件~~
>
> > 数据库中含有用户上传的文件时，若仍要删除用户，则需同时删除数据库中的文件信息

## <Version	1.3.7>	2021年7月1日17:04:15

> 1. 修复了新增/修改 用户/文件时用户名和密码/文件名可以为空的BUG（2020-07-01）
>
> 2. 修复了操作会得到多次反馈的问题
>
>    >在之前的版本为了分页查询，将整段代码另存为一个function，结果其中的bind多次为元素绑定反馈function。
>
> 3. 解决了导入spring mvc的servlet时会出现404的问题（）



## <Version	1.3.8>	2021年7月2日14:04:32

> 1. 修复了下载时文件名为乱码的问题

## <Version	1.3.9>	2021年7月2日17:06:49

> 1. 将spring mvc与spring security的配置文件分离，降低耦合度。
> 2. 修改了上传时选择文件的按钮

## <Version	1.3.10>	2021年7月3日09:11:11

> 1. 将需要rollback和commit的Service单独分离出来
> 2. 完成了Spring和Mybatis的整合

## <Version	1.4.0>	2021年7月3日11:42:08

> 1. 添加了日志功能，记录部分敏感操作

## <Version	1.4.1>	2021年7月3日14:58:15

> 1. 进行了代码清理

## <Version	1.5.0>	2021年7月5日07:48:05

> 1. 增加了条件查询功能

## <Version	1.6.0>	2021年7月5日10:25:08

> 1. 增加了页码显示
> 2. 增加了跳转功能
> 3. 增加了无数据显示

## <Version	1.6.1>	2021年7月5日16:28:58

> 1. 优化了前端页面布局

## <Version	1.6.2>	2021年7月8日11:03:06

> 1. 修复了代码清理导致的“==”变成“===”后无法正确判断的问题

## <Version	1.6.3>	2021年7月13日23:01:08

> 1. 项目部署时不再需要配置网址URL信息

## <Version	2.0.0>	2021年7月14日16:31:16

> 1. 全面弃用Struts，改用Spring MVC框架
