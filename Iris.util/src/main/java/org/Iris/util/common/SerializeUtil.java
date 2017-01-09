package org.Iris.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.gson.Gson;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

@SuppressWarnings("unchecked")
public interface SerializeUtil {

	class JsonUtil {
		public static final Gson GSON = new Gson();
	}

	class XmlUtil {

		/**
		 * Please be attention, the clazz must annotated with @XmlRootElement(name="rootElementName")
		 * 
		 * @param xml
		 * @param clazz
		 * @return
		 * @throws JAXBException
		 */
		public static <T> T xmlToBean(String xml, Class<T> clazz)  {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				T instance = (T) unmarshaller.unmarshal(new StringReader(xml));
				return instance;
			} catch (JAXBException e) {
				throw new RuntimeException("Serial failure!", e);
			}
		}
	}
	
	class ProtostuffUtil {
		public static final <T> byte[] serial(T object) {
			Class<T> clazz = (Class<T>)object.getClass();
			Schema<T> schema = RuntimeSchema.getSchema(clazz);
			LinkedBuffer buffer = LinkedBuffer.allocate();
			try {
				return ProtostuffIOUtil.toByteArray(object, schema, buffer);
			} finally {
				buffer.clear();
			}
		}
		
		public static final <T> int serial(T object, OutputStream out) throws IOException {
			Class<T> clazz = (Class<T>)object.getClass();
			Schema<T> schema = RuntimeSchema.getSchema(clazz);
			LinkedBuffer buffer = LinkedBuffer.allocate();
			try {
				return ProtostuffIOUtil.writeTo(out, object, schema, buffer);
			} finally {
				buffer.clear();
			}
		}
		
		public static final <T> T deserial(byte[] data, Class<T> clazz) {
			Schema<T> schema = RuntimeSchema.getSchema(clazz);
			T t = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(data, t, schema);
			return t;
		}
		
		public static final <T> T deserial(InputStream input, Class<T> clazz) throws IOException {
			Schema<T> schema = RuntimeSchema.getSchema(clazz);
			T t = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(input, t, schema);
			return t;
		}
	}
}
