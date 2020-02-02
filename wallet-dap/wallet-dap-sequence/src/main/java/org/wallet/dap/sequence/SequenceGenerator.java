package org.wallet.dap.sequence;

import java.util.Random;

/**
 * 0|00000000000000000000000000000000000000000|0000|0000000000000000000
 * 符号位|时间戳（毫秒）|节点数|随机数
 * 符号位：1位
 * 时间戳：41位
 * 节点数：4位
 * 随机数：18位
 * @author zengfucheng
 * @date 2018年4月11日
 */
public class SequenceGenerator {
	
	private final long nodeId;
	/**
	 * 基准时间
	 */
	private final static long TWEPOCH = 0L;
	private long sequence = 0L;
	/**
	 * 机器标识位数
	 */
	private final static long NODE_ID_BITS = 4L;
	/**
	 * 机器ID最大值
	 */
	public final static long MAX_NODE_ID = -1L ^ -1L << NODE_ID_BITS;
	/**
	 * 序列号识位数
	 */
	private final static long SEQUENCE_BITS = 18L;
	/**
	 * 机器ID偏左移18位
	 */
	private final static long NODE_ID_SHIFT = SEQUENCE_BITS;
	/**
	 * 时间毫秒左移22位
	 */
	private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + NODE_ID_BITS;
	/**
	 * 序列号ID最大值
	 */
	public final static long SEQUENCE_MASK = -1L ^ -1L << SEQUENCE_BITS;
	private long lastTimestamp = -1L;
	
	public SequenceGenerator(final long nodeId) {
		super();
		if (nodeId > MAX_NODE_ID || nodeId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_NODE_ID));
		}
		this.nodeId = nodeId;
	}
	
	public synchronized long nextId() {
		long timestamp = this.timeGen();
		//如果服务器时间有问题, 随机生成sequence。
		if (timestamp < this.lastTimestamp) {
			return ((timestamp - TWEPOCH << TIMESTAMP_LEFT_SHIFT)) | (this.nodeId << NODE_ID_SHIFT) | new Random(SEQUENCE_MASK).nextLong();
		}
		//如果上次生成时间和当前时间相同,在同一毫秒内
		if (this.lastTimestamp == timestamp) {
			 //sequence自增，因为sequence只有18bit，所以和sequenceMask相与一下，循环0-262143
			this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
			if (this.sequence == 0) {
				//自旋等待到下一毫秒
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}

		//如果大于上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
		} else {
			this.sequence = 0;
		}
		this.lastTimestamp = timestamp;
		// 最后按照规则拼出ID。
        // 0|00000000000000000000000000000000000000000|0000|000000000000000000
		return ((timestamp - TWEPOCH << TIMESTAMP_LEFT_SHIFT)) | (this.nodeId << NODE_ID_SHIFT) | this.sequence;
	}
	
	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}
	 
	private long timeGen() {
		return System.currentTimeMillis();
	}
}
