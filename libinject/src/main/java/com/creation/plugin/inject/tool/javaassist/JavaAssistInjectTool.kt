package com.creation.plugin.inject.tool.javaassist

import com.creation.plugin.inject.InjectUtils
import com.creation.plugin.inject.IInjectTool

import java.io.File
import java.io.IOException
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.util.HashSet

import javassist.CannotCompileException
import javassist.ClassPool
import javassist.NotFoundException

class JavaAssistInjectTool(private val mRootPath: String) : IInjectTool {
    private lateinit var mClassPool: ClassPool
    private val mModuleSet = HashSet<String>()

    private lateinit var mClassLoader: URLClassLoader
    private val mURLMethod: Method
    private val mInjector = Injector()

    private var mLastFlavor = ""

    init {

        // add
//        mModuleSet.add(null)

        try {
            mURLMethod = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
            mURLMethod.isAccessible = true
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        }

    }

    override fun injectDir(dir: File) {
        initAssistFlavor(dir)

        initAssistDir(dir)
        initClassLoaderDir(dir)

    }

    private fun initAssistFlavor(dir: File) {
        val dirPath = dir.absolutePath
        val index = dirPath.lastIndexOf('/')
        val flavor = dirPath.substring(index + 1, dirPath.length)

        if (flavor == mLastFlavor) {
            return
        }

        mClassPool = ClassPool(null)
        mClassPool.appendSystemPath()

        mClassLoader = URLClassLoader(arrayOfNulls(0), javaClass.classLoader)
        mInjector.setClassLoader(mClassLoader)

        for (module in mModuleSet) {
            val path = "$mRootPath/$module/build/intermediates/classes/$flavor"
            InjectUtils.println("lib path:$path")
            try {
                mClassPool.appendClassPath(path)
            } catch (e: NotFoundException) {
                throw RuntimeException(e)
            }

        }

        mLastFlavor = flavor
    }

    private fun initClassLoaderDir(dir: File) {
        try {
            mURLMethod.invoke(mClassLoader, dir.toURI().toURL())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initAssistDir(dir: File) {
        try {
            mClassPool.appendClassPath(dir.absolutePath)
        } catch (e: NotFoundException) {
            throw RuntimeException(e)
        }

    }

    override fun injectFile(dir: File, file: File) {
        val dirPath = dir.absolutePath
        val filePath = file.absolutePath
        val className = getClassName(dirPath, filePath)

        try {
            val clazz = mClassPool.getCtClass(className)
            if (clazz.isFrozen) {
                clazz.defrost()
            }
            val inject = mInjector.inject(filePath, clazz)
            if (inject) {
                clazz.writeFile(dirPath)
            }
        } catch (e: NotFoundException) {
            throw RuntimeException(e)
        } catch (e: CannotCompileException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    private fun getClassName(dirPath: String, path: String): String {
        var filePath = path.substring(dirPath.length + 1)
        filePath = filePath.replace('/', '.').replace('\\', '.')
        filePath = filePath.substring(0, filePath.length - ".class".length)
        return filePath
    }
}
