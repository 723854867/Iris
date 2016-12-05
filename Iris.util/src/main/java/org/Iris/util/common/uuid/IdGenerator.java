package org.Iris.util.common.uuid;

import java.util.UUID;

/**
 * 用来产生唯一的 UUID 
 * 
 * @author Ahab
 */
public interface IdGenerator {
	
	/**
	 * 返回一个新的 UUID
	 * @return 
	 */
	UUID generateId();
}
