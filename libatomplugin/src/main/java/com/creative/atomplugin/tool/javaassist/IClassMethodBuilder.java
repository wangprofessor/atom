package com.creative.atomplugin.tool.javaassist;

public interface IClassMethodBuilder {
    String buildMethod(Class<?> clazz);
    String buildBody(Class<?> clazz);
}
