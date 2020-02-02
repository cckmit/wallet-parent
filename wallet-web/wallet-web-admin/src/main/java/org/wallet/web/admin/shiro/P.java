package org.wallet.web.admin.shiro;

/**
 * Shiro Permissions
 * @author zengfucheng
 */
public interface P {
    /** 权限分隔符 */
    String SEP = ":";

    /** 基础权限 - 查询 */
    String INFO = SEP + "info";
    /** 基础权限 - 添加 */
    String SAVE = SEP + "save";
    /** 基础权限 - 修改 */
    String UPDATE = SEP + "update";
    /** 基础权限 - 删除 */
    String DELETE = SEP + "delete";

    /** 业务模块 - 系统 */
    String SYS = "sys";

    /** 功能模块 - 系统 - 部门 */
    String SYS_DEPT = SYS + SEP + "dept";
    /** 功能模块 - 系统 - 字典 */
    String SYS_DICT = SYS + SEP + "dict";
    /** 功能模块 - 系统 - 日志 */
    String SYS_LOG = SYS + SEP + "log";
    /** 功能模块 - 系统 - 菜单 */
    String SYS_MENU = SYS + SEP + "menu";
    /** 功能模块 - 系统 - 角色 */
    String SYS_ROLE = SYS + SEP + "role";
    /** 功能模块 - 系统 - 用户 */
    String SYS_USER = SYS + SEP + "user";

    /** 业务模块 - DApp */
    String APP = "app";

    /** 功能模块 - DApp - 广告位 */
    String APP_ADVERT = APP + SEP + "advert";
    /** 功能模块 - DApp - 主链 */
    String APP_CHAIN = APP + SEP + "chain";
    /** 功能模块 - DApp - 主链节点 */
    String APP_CHAIN_NODE = APP + SEP + "chain-node";
    /** 功能模块 - DApp - 基础信息 */
    String APP_INFO = APP + SEP + "info";
    /** 功能模块 - App - 超链 */
    String APP_LINK = APP + SEP + "link";
    /** 功能模块 - App - 消息 */
    String APP_MESSAGE = APP + SEP + "message";
    /** 功能模块 - DApp - 标签 */
    String APP_TAG = APP + SEP + "tag";
    /** 功能模块 - DApp - 类型 */
    String APP_TYPE = APP + SEP + "type";
    /** 功能模块 - App - 版本 */
    String APP_VERSION = APP + SEP + "version";

    /** 业务模块 - 钱包 */
    String WALLET = "wallet";
    /** 功能模块 - 钱包 - 币种信息 */
    String WALLET_COIN = WALLET + SEP + "coin";
    /** 功能模块 - 钱包 - 币种配置 */
    String WALLET_COIN_CONFIG = WALLET + SEP + "coin-config";
    /** 功能模块 - 钱包 - 交易所信息 */
    String WALLET_EXCHANGE = WALLET + SEP + "exchange";
    /** 功能模块 - 钱包 - 基础信息 */
    String WALLET_INFO = WALLET + SEP + "info";
    /** 功能模块 - 钱包 - EOS激活码支付方式 */
    String WALLET_PAY_CONFIG = WALLET + SEP + "pay-config";

    /** 业务模块 - 表扩展 */
    String TABLE = "table";
    /** 功能模块 - 表扩展 - 扩展属性 */
    String TABLE_EXT_ATTR = TABLE + SEP + "ext" + SEP + "attr";
}
