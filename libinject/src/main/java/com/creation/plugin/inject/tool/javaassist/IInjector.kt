package com.creation.plugin.inject.tool.javaassist

import javassist.CtClass

interface IInjector {
    fun enable(clazz: Class<*>): Boolean
    fun inject(clazz: Class<*>, ctClass: CtClass)
}
