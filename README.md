### 模仿天猫整站 Spring Boot
---
> 参考how2j.cn上的实践项目，特此感谢。

环境：
1. idea
2. maven3
3. mysql5.5
4. springboot2.2.4

技术栈：
1. 前端：HTML、CSS、JavaScript、JQuery、Ajax、Json、Bootstrap、Vue.js
2. 后端：Spring、SpringMVC、Spring Boot
3. 中间件：redis、nginx、shiro

项目结构：<br/>
模仿天猫整站
- 前端展示
    - 首页
    - 分类页面
    - 产品页面
    - 购物车页面
    - 我的订单页面
    - 结算页面
    - 查询结果页面
    - 支付页面
    - 支付成功页面
    - 确认收货页面
    - 收货成功页面
    - 评价页面
    - 登录页面
    - 注册页面
- 后端管理
    - 分类管理
    - 属性管理
    - 产品管理
    - 产品图片管理
    - 产品属性值设置
    - 用户管理
    - 订单管理
    
开发流程：
1. 需求分析<br/>
首先确定要做哪些功能，需求分析包括前端和后端。<br/>
前端又分为单纯要展示的那些功能-需求分析-展示，以及会提交数据到服务端的那些功能-需求分析-交互。
2. 表结构设计<br/>
表结构设计是围绕功能需求进行，如果表结构设计有问题，那么将会影响功能的实现。
3. 原型<br/>
借助界面原型，可以低成本，高效率的与客户达成需求的一致性。
4. 后端-分类管理<br/>
接下来开始进行功能开发，按照模块之间的依赖关系，首先进行后端-分类管理功能开发。
5. 后端-其他管理<br/>
然后开发属性管理、产品管理、产品图片管理、产品属性值设置、用户管理、订单管理。
6. 前端-首页<br/>
接下来开始进行前端功能的开发，首先进行前端-首页功能开发。
7. 前端无需登录<br/>
从前端模块之间的依赖性，以及开发顺序的合理性来考虑，把前端功能分为了无需登录即可使用的功能，和需要登录才能访问的功能。 建立在前一步前端-首页的基础之上，开始进行一系列的无需登录功能开发。
8. 前端需要登录<br/>
最后是需要登录的前端功能。这部分功能基本上都是和购物相关的。

项目预览：
![](https://s1.ax1x.com/2020/09/23/wjSLbn.png)