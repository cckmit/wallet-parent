package org.wallet.dap.cache.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallet.dap.cache.RedisScriptExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 联合锁
 * @author zengfucheng
 * @date 2018年7月3日
 */
public class MultiDapLock implements Lock {
	private static final Logger logger = LoggerFactory.getLogger(MultiDapLock.class);
	private final DapLock[] locks;
	private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

	public MultiDapLock(DapLock...locks) {
		if(locks.length == 0) throw new IllegalArgumentException("Constructor parameter can not be null");
		this.locks = locks;
	}

	@Override
	public void lock() {
		for(DapLock lock : locks) {
			lock.lock();
		}
	}

	@Override
	public void lockInterruptibly() {
		unlock();
	}

	@Override
	public Condition newCondition() {
		return null;
	}

	@Override
	public boolean tryLock() {
		for(DapLock lock : locks) {
			if(!lock.tryLock()) return false;
		}
		return true;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		for(DapLock lock : locks) {
			if(!lock.tryLock(time, unit)) return false;
		}
		return true;
	}

	@Override
	public void unlock() {
		for(DapLock lock : locks) {
			THREAD_POOL.execute(new UnlockRunnable(lock));
		}
	}
	
	class UnlockRunnable implements Runnable {
		DapLock lock;
		UnlockRunnable(DapLock lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			try {
				RedisScriptExecutor.execute("unlock.lua", lock.getKeys(), lock.getArgs());
			} catch (Exception e) {
				logger.error("redis exception", e);
			}
			
		}
	}
}
