package org.Iris.util.lang;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testHasLength() { 
		CharSequence sequence = "　你 好  ";
		Assert.assertTrue(StringUtil.hasLength(sequence));
		sequence = " ";				
		Assert.assertTrue(StringUtil.hasLength(sequence));
	}
	
	@Test
	public void testTrimWhitespace() { 
		String string = "　你好  ";		// 全角空格
		Assert.assertEquals(StringUtil.trimWhitespace(string), "你好");
	}
	
	@Test
	public void testSha1() { 
		System.out.println(DigestUtils.sha1Hex("if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end"));
	}
}
