package org.wallet.dap.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.wallet.common.constants.BusinessConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author zengfucheng
 * @date 2018年5月7日
 */
@Slf4j
public class CommonUtil {
    private static boolean allowTest = true;
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.0#");
    private static final DecimalFormat COIN_FORMAT = new DecimalFormat("0.0#######");
    private static final DateFormat DATE_FORMAT_UTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        MONEY_FORMAT.setRoundingMode(RoundingMode.FLOOR);
        COIN_FORMAT.setRoundingMode(RoundingMode.FLOOR);
        DATE_FORMAT_UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * 相差毫秒数
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static long getIntervalTime(Date fDate, Date oDate) {
        if (fDate == null || oDate == null) {
            return -1;
        }
        return oDate.getTime() - fDate.getTime();
    }

    /**
     * 相差小时数
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static long getIntervalHours(Date fDate, Date oDate) {
        if (fDate == null || oDate == null) {
            return -1;
        }
        long intervalMilli = oDate.getTime() - fDate.getTime();
        return intervalMilli / (1000 * 60 * 60);
    }

    /**
     * 相差分钟数
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static long getIntervalMinutes(Date fDate, Date oDate) {
        if (fDate == null || oDate == null) {
            return -1;
        }
        long intervalMilli = oDate.getTime() - fDate.getTime();
        return intervalMilli / (1000 * 60);
    }

    /**
     * 相隔分钟数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static long getIntervalMinutes(String startDate, String endDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fDate = formatter.parse(startDate);
        Date oDate = formatter.parse(endDate);
        return CommonUtil.getIntervalMinutes(fDate, oDate);
    }

    /**
     * 相差小时数
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static long getIntervalHours(Long fDate, Long oDate) {
        if (fDate == null || oDate == null) {
            return -1;
        }
        long intervalMilli = oDate - fDate;
        return intervalMilli / (1000 * 60 * 60);
    }

    /**
     * 加减天数
     *
     * @param currentDate
     * @param days
     * @return
     */
    public static Date addDays(Date currentDate, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 返回毫秒数
     *
     * @param time
     * @param unit
     * @return
     */
    public static long getBlockTime(long time, TimeUnit unit) {
        long blockTime = 0;
        switch (unit) {
            case NANOSECONDS:
                blockTime = time;
                break;
            case MICROSECONDS:
                blockTime = time;
                break;
            case MILLISECONDS:
                blockTime = time;
                break;
            case SECONDS:
                blockTime = time * 1000;
                break;
            case MINUTES:
                blockTime = time * 60 * 1000;
                break;
            case HOURS:
                blockTime = time * 60 * 60 * 1000;
                break;
            case DAYS:
                blockTime = time * 24 * 60 * 60 * 1000;
                break;
            default:
                break;
        }
        return blockTime;
    }

    public static String toUTCTimeStr(long timestamp) {
        return toUTCTimeStr(new Date(timestamp));
    }

    public static String toUTCTimeStr(Date time) {
        return DATE_FORMAT_UTC.format(time);
    }

    public static String toLocalTimeStr(long timestamp) {
        return toLocalTimeStr(new Date(timestamp));
    }

    public static String toLocalTimeStr(Date time) {
        return DATE_FORMAT.format(time);
    }


    /**
     * 检查法币金额是否符合标准
     * 正数，小数小于2位
     *
     * @param number 法币金额
     * @return 是否符合标准
     */
    public static Boolean checkMoney(Number number) {
        String numberString = number.toString();
        if (number instanceof BigDecimal) {
            numberString = ((BigDecimal) number).toPlainString();
        }
        return numberString.matches("\\d{1,12}|\\d{1,12}(\\.\\d{1,2})");
    }

    /**
     * 检查数字货币是否符合标准
     * 正数，小数小于8位
     *
     * @param number 数字货币
     * @return 是否符合标准
     */
    public static Boolean checkCoin(Number number) {
        String numberString = number.toString();
        if (number instanceof BigDecimal) {
            numberString = ((BigDecimal) number).toPlainString();
        }
        return numberString.matches("\\d{1,12}|\\d{1,12}(\\.\\d{1,8})");
    }

    /**
     * 格式化法币金额，转换为保留2位小数，且不四舍五入
     *
     * @param number
     * @return
     */
    public static String formatMoney(Number number) {
        if (null != number) {
            return MONEY_FORMAT.format(number);
        }
        return null;
    }

    /**
     * 格式化数字货币，转换为保留8位小数，且不四舍五入
     *
     * @param number
     * @return
     */
    public static String formatCoin(Number number) {
        if (null != number) {
            return COIN_FORMAT.format(number);
        }
        return null;
    }

    public static boolean allowTest(){
        if(!allowTest){
            return false;
        }
        allowTest = BusinessConstant.ALLOW_TEST_PROFILES.contains(EnvironmentUtil.getProperty(ConfigFileApplicationListener.ACTIVE_PROFILES_PROPERTY));
        return allowTest;
    }
}
