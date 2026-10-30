# sky-take-out（苍穹外卖）

基于 Spring Boot 的外卖点餐与商家管理后端服务：包含面向顾客的微信小程序端接口（浏览菜品/套餐、购物车、下单、支付、地址簿）和面向商家的后台管理端接口（员工、分类、菜品、套餐、订单、营业状态、数据统计与报表导出）。

## 技术栈

| 分类 | 内容 |
| --- | --- |
| 语言 / JDK | Java（源码使用 `javax.servlet`、`javax.websocket` 等 Java EE 命名空间，对应 Spring Boot 2.x 时代） |
| 框架 | Spring Boot 2.7.3（`spring-boot-starter-web`、`spring-boot-starter-data-redis`、`spring-boot-starter-cache`、`spring-boot-starter-websocket`） |
| 持久层 | MyBatis（`mybatis-spring-boot-starter` 2.2.0，XML Mapper + 注解）、PageHelper 1.3.0 分页 |
| 数据库 | MySQL（`mysql-connector-java`，库名 `sky_take_out`），Druid 1.2.1 连接池 |
| 缓存 | Redis（`RedisTemplate` 手动缓存 + Spring Cache 注解 `@Cacheable/@CacheEvict`） |
| 认证 | JWT（jjwt 0.9.1），后台端请求头 `token`、用户端请求头 `authentication` |
| 接口文档 | Knife4j 3.0.2 / Swagger 2（`/doc.html`） |
| 工具库 | Lombok 1.18.20、FastJSON 1.2.76、commons-lang 2.6、AspectJ 1.9.4、Apache POI 3.16（Excel 报表导出）、阿里云 OSS SDK 3.10.2、微信支付 SDK `wechatpay-apache-httpclient` 0.4.8 |
| 构建工具 | Maven（多模块 `pom` 聚合工程，`spring-boot-maven-plugin` 打包） |

## 目录结构

```text
sky-take-out/
├── pom.xml                 # 父 POM：模块聚合、依赖版本管理
├── .gitignore              # 忽略 target/、.idea、*.iml、*Test.java、**/test/
├── create_sql/
│   └── sky.sql             # 建库建表脚本（11 张表及初始数据）
├── sky-common/             # 公共模块
│   └── src/main/java/com/sky/
│       ├── constant/       # 常量（JWT、消息提示、营业状态、上传路径等）
│       ├── context/        # BaseContext（ThreadLocal 保存当前用户 ID）
│       ├── enumeration/    # OperationType（INSERT/UPDATE）
│       ├── exception/      # 业务异常
│       ├── json/           # JacksonObjectMapper（日期格式化）
│       ├── properties/     # JwtProperties / WeChatProperties / AliOssProperties
│       ├── result/         # Result / PageResult 统一返回
│       └── utils/          # JwtUtil、AliOssUtil、HttpClientUtil、WeChatPayUtil
├── sky-pojo/               # 数据模型模块
│   └── src/main/java/com/sky/
│       ├── dto/            # 入参对象（登录、分页查询、下单、支付等）
│       ├── entity/         # 实体（Employee、Dish、Setmeal、Orders、User 等）
│       └── vo/             # 出参对象（工作台/报表/订单视图等）
└── sky-server/             # 服务模块（可执行）
    ├── src/main/java/com/sky/
    │   ├── SkyApplication.java   # 启动类（@EnableCaching、@EnableTransactionManagement）
    │   ├── annotation/           # @Autofill 注解
    │   ├── aspect/               # AutoFillAspect 公共字段自动填充
    │   ├── config/               # WebMvc / Redis / WebSocket 配置
    │   ├── controller/           # admin（后台）、user（用户端）、nofity（支付回调）
    │   ├── handler/              # GlobalExceptionHandler 全局异常处理
    │   ├── interceptor/          # JWT 后台/用户端拦截器
    │   ├── mapper/               # MyBatis Mapper 接口
    │   ├── service/ + impl/      # 业务层
    │   ├── task/                 # OrderTask 订单状态定时任务
    │   └── websocket/            # WebSocketServer（/ws/{sid}）
    └── src/main/resources/
        ├── application.yml       # 端口 8080、数据源/Redis/JWT/微信配置
        ├── application-dev.yml   # dev 环境本地连接参数
        ├── mapper/*.xml          # MyBatis SQL 映射
        └── template/运营数据报表模板.xlsx  # 报表导出模板
```

## 主要功能 / 模块说明

### 后台管理端（`/admin/**`，JWT 拦截，登录接口除外）

| 模块 | 主要接口 | 说明 |
| --- | --- | --- |
| 员工管理 | `/admin/employee/login`、`/logout`、`/page`、`/status/{status}`、`/{id}` | 员工登录、新增、分页查询、启用/禁用、编辑 |
| 分类管理 | `/admin/category`（POST/PUT/DELETE）、`/page`、`/status/{status}`、`/list` | 菜品/套餐分类的增删改查、启停用 |
| 菜品管理 | `/admin/dish`（POST/PUT/DELETE）、`/page`、`/{id}`、`/status/{status}`、`/list` | 菜品维护、起售/停售，变更时清理 Redis 中 `dish_*` 缓存 |
| 套餐管理 | `/admin/setmeal`（POST/PUT/DELETE）、`/page`、`/{id}`、`/status/{status}` | 套餐维护、起停售；使用 `setmealCache` 缓存并在写操作后清除 |
| 订单管理 | `/admin/order/conditionSearch`、`/details/{id}`、`/statistics`、`/confirm`、`/rejection`、`/cancel`、`/delivery/{id}`、`/complete/{id}` | 订单检索、详情、各状态数量统计、接单/拒单/取消/派送/完成 |
| 营业状态 | `/admin/shop/{status}`、`/admin/shop/status` | 营业状态读写 Redis |
| 工作台 | `/admin/workspace/businessData`、`/overviewOrders`、`/overviewDishes`、`/overviewSetmeals` | 今日运营数据与订单/菜品/套餐概览 |
| 数据统计 | `/admin/report/turnoverStatistics`、`/userStatistics`、`/ordersStatistics`、`/top10`、`/export` | 营业额、用户、订单统计，销量 Top10，用 POI 按模板导出 Excel |
| 通用 | `/admin/common/upload` | 文件上传到本地目录并返回访问链接 |

### 用户端（`/user/**`，JWT 拦截，登录与店铺状态接口除外）

| 模块 | 主要接口 | 说明 |
| --- | --- | --- |
| 微信登录 | `/user/user/login` | 微信授权登录，签发用户端 JWT |
| 店铺状态 | `/user/shop/status` | 查询营业状态 |
| 浏览 | `/user/category/list`、`/user/dish/list`、`/user/setmeal/list`、`/user/setmeal/dish/{id}` | 分类、菜品、套餐及套餐内菜品查询；菜品按分类缓存到 Redis |
| 购物车 | `/user/shoppingCart/add`、`/list`、`/sub`、`/clean` | 加购、减购、查看、清空 |
| 地址簿 | `/user/addressBook`（增删改查）、`/list`、`/default` | 收货地址维护与默认地址 |
| 订单 | `/user/order/submit`、`/payment`、`/historyOrders`、`/orderDetail/{id}`、`/cancel/{id}`、`/repetition/{id}`、`/reminder/{id}` | 提交订单、微信支付、历史订单分页、详情、取消、再来一单、催单 |
| 支付回调 | `/notify/paySuccess` | 微信支付结果通知解密并更新订单状态 |

### 其他机制

- **JWT 鉴权**：`JwtTokenAdminInterceptor` 拦截 `/admin/**`（放行 `/admin/employee/login`）；`JwtTokenUserInterceptor` 拦截 `/user/**`（放行 `/user/user/login`、`/user/shop/status`）。
- **公共字段自动填充**：`@Autofill` + `AutoFillAspect` 在 Mapper 方法执行前填充创建/更新时间与操作人。
- **全局异常处理**：`GlobalExceptionHandler` 统一返回 `Result`。
- **WebSocket**：`/ws/{sid}` 端点，用于向客户端推送（如订单来单提醒）。
- **定时任务**：`OrderTask` 每分钟取消超时未支付订单、每天 1 点处理派送中订单。
- **数据库表**：`employee`、`category`、`dish`、`dish_flavor`、`setmeal`、`setmeal_dish`、`orders`、`order_detail`、`shopping_cart`、`address_book`、`user`（见 `create_sql/sky.sql`）。

## 运行 / 构建方式

前置条件：JDK 8+、Maven、MySQL、Redis 均已就绪。

1. 初始化数据库：执行 `create_sql/sky.sql`（脚本会创建并切换到 `sky_take_out` 库）。
2. 按本机环境修改 `sky-server/src/main/resources/application-dev.yml` 中的 MySQL、Redis 连接信息（当前默认 `localhost:3306` 库 `sky_take_out`、`localhost:6379`、database `0`）。
3. 在仓库根目录构建：

   ```bash
   mvn clean package
   ```

4. 运行（以下为按工程结构推断的常用方式，任选其一）：

   ```bash
   # 方式一：Maven 直接启动（需同时构建依赖模块）
   mvn -pl sky-server -am spring-boot:run

   # 方式二：运行打包产物
   java -jar sky-server/target/sky-server-1.0-SNAPSHOT.jar
   ```

5. 服务默认端口 `8080`；接口文档地址：`http://localhost:8080/doc.html`。
6. 前端（商家后台页面、微信小程序）不在本仓库中，需另行获取；本仓库只包含后端服务。

> 说明：仓库内没有 `README`、部署脚本或前端工程，以上命令依据父/子 `pom.xml` 的模块划分与 `spring-boot-maven-plugin` 推断，未实际执行验证。

## 备注

- 本项目为「苍穹外卖」教学/练习项目的实现，代码结构与课程资料（`com.sky` 包名、`itcast`/`itheima` 等 JWT 密钥）一致，属于个人学习与练手仓库，非生产项目。
- 仓库共 47 次提交，远程地址为 `https://github.com/A-lioth/sky-take-out.git`，当前分支 `master` 跟踪 `main/master`。
- **敏感配置已硬编码**：`application-dev.yml` 中明文存放数据库/Redis 密码与微信 `appid`/`secret`，`application.yml` 中 JWT 密钥为固定字符串。若要公开或部署，应先改为环境变量并轮换这些凭据。
- **本地绝对路径硬编码**：`FileUploadPathConstant.FILE_UPLOAD_PATH` 与 `WebMvcConfiguration` 的静态资源映射均写死为 `C:\GitHub\Java_Program\sky-take-out\upload\`，换机器运行需一并修改。
- `sky-server/src/test` 下仅有少量测试类（`RedisConfigurationTest`、`HttpClientTest`），且 `.gitignore` 忽略 `**/test/` 与 `*Test.java`，测试覆盖很有限。
- `OrderTask` 使用了 `@Scheduled`，但启动类上未见 `@EnableScheduling`；定时任务是否真正生效需自行确认。
- 源码中部分注释在编辑器里显示为乱码（文件本身可能为 GBK 或历史编码不一致），不影响编译，但阅读时需注意编码设置。
