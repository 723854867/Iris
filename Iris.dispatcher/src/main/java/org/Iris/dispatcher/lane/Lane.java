package org.Iris.dispatcher.lane;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jetlang.core.BatchExecutor;
import org.jetlang.core.BatchExecutorImpl;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;

public class Lane {
	
	private static final BatchExecutor BATCH_EXECUTOR = new BatchExecutorImpl();

	private int idx;
	private String name;
	private Fiber fiber;
	private Executor exec;

	public Lane(int idx) {
		this.init();
	}
	
	private void init() { 
		this.exec = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory());
		this.exec.execute(new Runnable() {
			@Override
			public void run() {
				name = Thread.currentThread().getName();
			}
		});
		
		PoolFiberFactory factory = new PoolFiberFactory(exec, null);
		this.fiber = factory.create(BATCH_EXECUTOR);
		this.fiber.start();
	}
	
	public int getIdx() {
		return idx;
	}
	
	public Fiber getFiber() {
		return fiber;
	}
	
	public boolean isInLane() { 
		return Thread.currentThread().getName().equals(name);
	}
}
