package org.wallet.common.dto;

import lombok.Data;
import org.wallet.common.enums.Device;
import org.wallet.common.enums.InternetType;

/**
 * @author zengfucheng
 **/
@Data
public class ClientData {
    /** 唯一标识 */
    private String clientId;
    /** 设备类型 */
    private Device device;
    /** IMSI */
    private String imsi;
    /** IMeI */
    private String imei;
    /** 手机号 */
    private String phone;
    /** App 版本 */
    private String appVersion;
    /** 系统版本 */
    private String systemVersion;
    /** 品牌 */
    private String brand;
    /** 型号 */
    private String model;
    /** 网络类型 */
    private InternetType internet;
    /** 屏幕分辨率 */
    private String screen;
}
