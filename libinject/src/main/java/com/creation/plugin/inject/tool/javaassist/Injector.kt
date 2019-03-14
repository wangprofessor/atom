package com.creation.plugin.inject.tool.javaassist

import com.creation.plugin.inject.InjectUtils

import java.util.HashSet

import javassist.CtClass

internal class Injector {

    private var mClassLoader: ClassLoader? = null

    fun setClassLoader(classLoader: ClassLoader) {
        mClassLoader = classLoader
    }

    fun inject(classPath: String, ctClass: CtClass): Boolean {
        val className = ctClass.name
        val clazz: Class<*>
        try {
            clazz = Class.forName(className, true, mClassLoader)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException()
        }

        InjectUtils.println("inject class:" + clazz.name + ", path:" + classPath)

        return injectInner(clazz, ctClass)
    }

    companion object {
        private val sInjectorMap = HashSet<com.creation.plugin.inject.tool.javaassist.IInjector>()
        private fun injectInner(clazz: Class<*>, ctClass: CtClass): Boolean {
            var inject = false
            for (injector in sInjectorMap) {
                if (!injector.enable(clazz)) {
                    continue
                }
                injector.inject(clazz, ctClass)
                inject = true
            }
            return inject
        }

        init {
            // add
//            sInjectorMap.add(null)
        }
    }
}
