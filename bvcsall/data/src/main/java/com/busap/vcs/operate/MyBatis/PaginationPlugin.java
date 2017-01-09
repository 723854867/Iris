package com.busap.vcs.operate.MyBatis;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.io.Serializable;
import java.util.List;

public class PaginationPlugin extends PluginAdapter {

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
                                              IntrospectedTable introspectedTable) {
        addLimit(topLevelClass, introspectedTable, "limitStart");
        addLimit(topLevelClass, introspectedTable, "limitEnd");
        return super.modelExampleClassGenerated(topLevelClass,
                introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType(Serializable.class.getName()));
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType(Serializable.class.getName()));

        //topLevelClass.addImportedType(new FullyQualifiedJavaType(BasicModel.class.getName()));
        //topLevelClass.setSuperClass(new FullyQualifiedJavaType(BasicModel.class.getName()));

        Field f = new Field("serialVersionUID = 1L", new FullyQualifiedJavaType(long.class.getName()));
        f.setStatic(true);
        f.setVisibility(JavaVisibility.PRIVATE);
        f.setFinal(true);

        topLevelClass.addField(f);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement choose = new XmlElement("choose");
        XmlElement rangeLimitWhen = new XmlElement("when");
        rangeLimitWhen.addAttribute(new Attribute("test", "limitStart != -1 and limitEnd != -1"));
        rangeLimitWhen.addElement(new TextElement("limit ${limitStart} , ${limitEnd}"));

        XmlElement limitStartWhen = new XmlElement("when");
        limitStartWhen.addAttribute(new Attribute("test", "limitStart != -1"));
        limitStartWhen.addElement(new TextElement("limit ${limitStart}"));
        choose.addElement(rangeLimitWhen);
        choose.addElement(limitStartWhen);
        element.addElement(choose);

        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
                introspectedTable);
    }

    private void addLimit(TopLevelClass topLevelClass,
                          IntrospectedTable introspectedTable, String name) {
        CommentGenerator commentGenerator = context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(new FullyQualifiedJavaType(Integer.class.getName()));
        field.setName(name);
        field.setInitializationString("-1");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("set" + camel);
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getIntInstance(), name));
        method.addBodyLine("this." + name + "=" + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("get" + camel);
        method.addBodyLine("return " + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    /**
     * This plugin is always valid - no properties are required
     */
    public boolean validate(List<String> warnings) {
        return true;
    }


}