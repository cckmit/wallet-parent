# 引言
这是一个包含所有基础功能的钱包App后台Java项目。采用分布式微服务架构，拥有高性能，高可用，易扩展，可伸缩等特点。

## 技术选型

* 核心框架：Spring Boot 2.1.7.RELEASE、Spring Framework 5.1.9.RELEASE
* RPC框架：Dubbo 2.6.5
* 消息中间件：RocketMQ 4.2.0
* 云框架：AWS SDK 2.7.10、Aliyun 3.5.0
* 安全框架：Apache Shiro 1.4.0
* 服务端验证：Hibernate Validator 6.0.17、Jwt 3.8.1
* 持久层框架：JPA 2.1.10 RELEASE
* 数据库连接池：Alibaba Druid 1.1.18
* 缓存框架：Redis 3.x、Jedis 2.9.3
* 日志管理：SLF4J 1.7、Log4j、logback
* 工具类：Apache Commons、FastJson 1.2.58、HuTool 4.5.16、Guava 16.0.1

## 技术特点

* 架构分层为公用实体、公用组件、业务网关、业务层及接口层
* 统一管理Jar包版本、封装简化pom.xml，使其服务扩展方便，降低依赖复杂度
* 集成Maven环境变量，避免环境切换导致的研发及生产问题
* 使用自定义Shell脚本并利用assembly插件打包服务为Zip包，降低服务发布难度
* 优化服务Jar包启动参数（打开JMX监控、强制使用G1 GC、针对性优化GC参数、记录GC及内存溢出DUMP日志）
* 分布式全局唯一ID（org.wallet.dap.sequence.SequenceGenerator）
* 基于Redis实现的分布式锁（org.wallet.dap.cache.lock.DapLock）
* 封装公用缓存Bean（org.wallet.dap.cache.Cache）
* 二次封装JPA CRUD，简化JPA查询、排序及分页难度（org.wallet.service.common.dao.\*）
* 定义事务切面，简化事务管理（org.wallet.service.common.config.TransactionAdviceConfig）
* 封装数据源连接池配置，简化服务集成数据库连接池难度（org.wallet.service.common.config.DruidConfig）
* 封装Service CRUD降低单表业务研发难度（org.wallet.service.common.service.CrudService）
* 封装Dubbo业务服务，使其抽取、使用、调用Dubbo服务更简单，方便<br/>
 org.wallet.dap.common.dubbo.IService<br/>
 org.wallet.dap.common.dubbo.ServiceRequest<br/>
 org.wallet.dap.common.dubbo.ServiceResponse<br/>
 org.wallet.service.common.service.dubbo.BaseDubboService
* 封装RocketMQ，降低使用难度（org.wallet.dap.mq.RocketMQListener）
* 分离DTO、Entity、常量及枚举，规范业务分层及代码分区<br/>
org.wallet.common.constants.\*<br/>
org.wallet.common.dto.\*<br/>
org.wallet.common.entity.\*<br/>
org.wallet.common.enums.\*
* Shiro集成JWT使其后台管理网站实现前后端分离、降低耦合（org.wallet.web.admin.shiro.ShiroJwtFilter）
* App接口实现接口版本注解、可根据接口版本及App版本自定义研发App接口<br/>
org.wallet.web.common.mvc.version.ApiVersion<br/>
org.wallet.web.common.mvc.version.AppVersion
* 根据Http协议封装接口异常响应（org.wallet.web.common.mvc.controller.GlobalExceptionHandler）
* 封装XSSFilter，有效防止脚本攻击（org.wallet.web.common.mvc.XssFilter）
* 严格按照RESTFul方式定义接口，有效降低前后端沟通成本
* 使用Redis实现自定义Token机制（org.wallet.web.common.mvc.token.TokenInterceptor）
* 使用自定义接口加密、签名方式。即便未使用Https也能有效增加接口安全（org.wallet.web.common.crypto.*）


