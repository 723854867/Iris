package org.Iris.util.common.uuid;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 速度最快的方式,但是生成的ID比较简单
 * 
 * @author Ahab
 */
public class SimpleIdGenerator implements IdGenerator {

	private final AtomicLong mostSigBits = new AtomicLong(0);

	private final AtomicLong leastSigBits = new AtomicLong(0);

	@Override
	public UUID generateId() {
		long leastSigBits = this.leastSigBits.incrementAndGet();
		if (leastSigBits == 0) {
			this.mostSigBits.incrementAndGet();
		}
		return new UUID(this.mostSigBits.get(), leastSigBits);
	}
}
