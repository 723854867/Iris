package org.Iris.redis.operate.lua;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.Iris.util.io.FileReader;
import org.Iris.util.io.ResourceUtil;
import org.Iris.util.lang.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuaCache {
	
	private static final Logger logger = LoggerFactory.getLogger(LuaCache.class);

	private static final String DEFAULT_LUA_FILE				= "/lua/";
	private static final String LUA_SCRIPT_SUFFIX				= ".lua";

	private Map<String, LuaScript> scripts = new ConcurrentHashMap<String, LuaScript>();
	
	public LuaCache() {
		_loadPredefinedScript();
	}
	
	public LuaCache(String luaScriptLocation) throws IOException {
		_loadPredefinedScript();
		scan(luaScriptLocation);
	}

	private void _loadPredefinedScript() {
		for (LuaCommand command : LuaCommand.values()) {
			try {
				byte[] buffer = FileReader.bufferReadFromClassOrJar(LuaOperate.class, DEFAULT_LUA_FILE + command.name().toLowerCase() + LUA_SCRIPT_SUFFIX);
				String content = new String(buffer);
				String sha1Key = DigestUtils.sha1Hex(content);
				scripts.put(command.name(), new LuaScript(sha1Key, content));
				logger.info("Lua script {} loaded!", command.name());
			} catch (IOException e) {
				logger.info("Lua script {} load failure!", command.name());
			}
		}
	}
	
	public void scan(String luaScriptLocation) throws IOException { 
		File file = ResourceUtil.getFile(luaScriptLocation);
		if (!file.exists()) {
			logger.error("File {} not exist, lua cache load failure!", luaScriptLocation);
			return;
		}
		if (!file.isDirectory()) {
			logger.error("File {} is not a directory, lua cache load failure!", luaScriptLocation);
			return;
		}
		_scanning(file);
	}
	
	private void _scanning(File file) throws IOException {
		File[] files = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(LUA_SCRIPT_SUFFIX);
			}
		});
		for (File f : files) 
			addLuaScript(f.getName().replaceAll(LUA_SCRIPT_SUFFIX, StringUtil.EMPTY), f);
		File[] directories = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return file.isDirectory();
			}
		});
		for (File directory : directories)
			_scanning(directory);
		logger.info("Load {} lua script from {}!", files.length, file.getAbsoluteFile());
	}
	
	public boolean addLuaScript(String cacheName, File file) throws IOException {
		String content = new String(FileReader.bufferRead(file));
		String shalKey = DigestUtils.sha1Hex(content);
		if (null != scripts.putIfAbsent(cacheName.toUpperCase(), new LuaScript(shalKey, content))) {
			logger.warn("Lua script cache - {}:{} already exist!", cacheName, file.getCanonicalPath());
			return false;
		}
		return true;
	}
	
	public boolean addLuaScript(String cacheName, String scriptFile) throws IOException { 
		String content = new String(FileReader.bufferRead(scriptFile));
		String shalKey = DigestUtils.sha1Hex(content);
		if (null != scripts.putIfAbsent(cacheName.toUpperCase(), new LuaScript(shalKey, content))) {
			logger.warn("Lua script cache - {}:{} already exist!", cacheName, scriptFile);
			return false;
		}
		return true;
	}
	
	public LuaScript get(String key) {
		return this.scripts.get(key);
	}
}
