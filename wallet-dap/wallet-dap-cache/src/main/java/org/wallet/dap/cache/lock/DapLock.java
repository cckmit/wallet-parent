package org.wallet.dap.cache.lock;

import lombok.extern.slf4j.Slf4j;
import org.wallet.dap.cache.RedisScriptExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
public class DapLock implements Lock{
	public static final String KEY_PREFIX  = "LOCK";
	private List<String> keys = new ArrayList<>(1);
	private List<Object> args = new ArrayList<>(1);
	private String lockName;
	private boolean locked = false;

	public DapLock(String lockName, Integer timeout) {
		this.lockName = lockName;
		keys.add(KEY_PREFIX + ":" + lockName);
		args.add(timeout);
	}

	@Override
	public void lock() {
		while(!locked) {
			if(locked = (RedisScriptExecutor.execute("lock.lua", keys, args, Long.class) == 1)) break;
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		unlock();
	}

	@Override
	public boolean tryLock() {
		if(locked) return locked;
		return locked = (RedisScriptExecutor.execute("lock.lua", keys, args, Long.class) == 1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		long start = System.currentTimeMillis();
		long deadline = getDeadline(time, unit);
		while(!locked && System.currentTimeMillis()-start <= deadline) {
			Long res = RedisScriptExecutor.execute("lock.lua", keys, args, Long.class);
			if(res == 1) {
				locked = true;
				break;
			}
		}
		return locked;
	}

	@Override
	public void unlock() {
		try {
			if(locked) {
				RedisScriptExecutor.execute("unlock.lua", keys, args);
				locked = false;
			}
		} catch (Exception e) {
			log.error("Release lock[lockName={}] exception", lockName, e);
		}

	}

	@Override
	public Condition newCondition() {
		return null;
	}

	protected static long getDeadline(long time, TimeUnit unit) {
		long blockTime = 0;
		switch (unit) {
			case NANOSECONDS : throw new IllegalArgumentException("Unsupported time units : NANOSECONDS");
			case MICROSECONDS : throw new IllegalArgumentException("Unsupported time units : MICROSECONDS");
			case MILLISECONDS : blockTime = time;break;
			case SECONDS : blockTime = time * 1000; break;
			case MINUTES : blockTime = time * 60000; break;
			case HOURS : blockTime = time * 3600000; break;
			case DAYS : blockTime = time * 86400000; break;
			default: break;
		}
		return blockTime;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public boolean isLocked() {
		return locked;
	}

}
