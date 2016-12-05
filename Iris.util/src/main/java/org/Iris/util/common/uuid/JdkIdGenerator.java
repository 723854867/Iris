package org.Iris.util.common.uuid;

import java.util.UUID;

/**
 * 直接使用 UUID.randonUUID()
 * 
 * @author Ahab
 */
public class JdkIdGenerator implements IdGenerator {
	
	@Override
	public UUID generateId() {
		return UUID.randomUUID();
	}
}
