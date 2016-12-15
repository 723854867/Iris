package org.Iris.util.common;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.gson.Gson;

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
		@SuppressWarnings("unchecked")
		public static <T> T xmlToBean(String xml, Class<T> clazz) throws JAXBException {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			T instance = (T) unmarshaller.unmarshal(new StringReader(xml));
			return instance;
		}
	}
}
