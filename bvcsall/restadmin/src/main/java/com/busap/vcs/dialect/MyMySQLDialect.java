package com.busap.vcs.dialect;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MyMySQLDialect extends MySQLDialect {
	 public MyMySQLDialect(){
		  super();
		  registerFunction("convert_utf8_unicode_ci", new SQLFunctionTemplate(StandardBasicTypes.STRING, "convert(?1 using utf8)  COLLATE utf8_unicode_ci  ") ); 
	 }
}