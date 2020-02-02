package org.wallet.common.constants.platform;

/**
 * ZG.TOP 接口参数常量
 */
public interface ZGTopConstants {
    /**
     * 响应成功的返回码
     */
    String STATUS_SUCCESS = "200";
    /**
     * 异常响应码
     * 101	必填参数不能为空
     * 102	API key不存在
     * 103	API已禁止使用
     * 104	权限已关闭
     * 105	权限不足
     * 106	签名不匹配
     * 201	对应资产不存在
     * 202	对应资产不能充值和提款
     * 203	资产还没分配到钱包地址
     * 204	取消挂单失败（部分成交或全部已成交）
     * 205	交易数量不能小于0.0001
     * 206	交易价格不能小于0.0001
     * 207	币种未开放交易
     * 208	交易对币种余额不足
     * 209	交易密码错误
     * 210	交易价格不在限价区间内
     * 211	币种余额不足
     * 212	最大交易总金额受限
     * 213	最小交易总金额受限
     * 401	非法参数
     * 402	系统异常
     */
    /**
     * 平台支持交易对
     * 36	ETH/USDT
     * 42	EOS/USDT
     * 39	THETA/USDT
     * 37	SNT/USDT
     * 35	BTC/USDT
     * 43	WTC/USDT
     * 44	EAI/USDT
     * 47	FTL/USDT
     * 48	OCN/USDT
     * 49	OMG/USDT
     * 50	ELF/USDT
     * 51	IOST/USDT
     * 52	CDT/USDT
     * 53	GAT/USDT
     * 54	CVT/USDT
     * 56	ZXC/USDT
     * 62	GRAM/USDT
     * 63	WICC/USDT
     * 64	BQT/USDT
     * 65	HUR/USDT
     * 66	FREC/USDT
     * 68	DACC/USDT
     * 69	STM/USDT
     * 72	ZRX/USDT
     * 74	SWFTC/USDT
     * 76	AE/USDT
     * 77	BAT/USDT
     * 78	CCMC/USDT
     * 79	PAY/USDT
     * 82	LRC/USDT
     * 83	REP/USDT
     * 84	TYT/USDT
     * 86	OTWP/USDT
     * 87	QOS/USDT
     * 88	WOO/USDT
     * 89	DOGE/USDT
     * 90	BTS/USDT
     * 91	LTC/USDT
     * 92	ETC/USDT
     * 93	ISC/USDT
     * 101	TUSD/BTC
     * 103	BDC/USDT
     * 106	XCN/USDT
     * 108	SPND/ETH
     * 109	AERGO/ETH
     * 110	TASK/ETH
     * 112	TASK/USDT
     * 116	COC/USDT
     * 118	TCH/USDT
     * 120	KBC/ETH
     * 123	Ankr/ETH
     * 124	CAS/USDT
     * 125	CAS/ETH
     * 126	PSP/USDT
     * 127	GOD/USDT
     * 129	HT/USDT
     * 130	BNB/USDT
     * 131	DUSD/USDT
     * 132	CIF/USDT
     * 133	ZGT/USDT
     * 134	BRDT/USDT
     * 136	LBT/USDT
     * 137	YT/USDT
     */
}
