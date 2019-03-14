package com.creation.plugin.inject

import com.android.build.api.transform.*
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.creation.plugin.inject.tool.asm.AsmInjectTool
import com.google.common.collect.Sets
import com.creation.plugin.inject.tool.javaassist.JavaAssistInjectTool

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.util.ArrayList

internal class ClassTransform(private val mProject: Project, private val mIsApp: Boolean, private val mExtension: TestedExtension) : Transform() {

    private val mInjectTool: IInjectTool

    init {

        mInjectTool = JavaAssistInjectTool(mProject.rootDir.absolutePath)
//        mInjectTool = AsmInjectTool()
    }

    override fun getName(): String {
        return "class editor"
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return if (mIsApp) {
            Sets.immutableEnumSet<QualifiedContent.Scope>(
                    QualifiedContent.Scope.PROJECT,
                    QualifiedContent.Scope.SUB_PROJECTS,
                    QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        } else {
            Sets.immutableEnumSet<QualifiedContent.Scope>(
                    QualifiedContent.Scope.PROJECT)
        }
    }

    override fun isIncremental(): Boolean {
        return false
    }

    @Throws(IOException::class)
    override fun transform(context: Context?,
                           inputs: Collection<TransformInput>?,
                           referencedInputs: Collection<TransformInput>?,
                           outputProvider: TransformOutputProvider?,
                           isIncremental: Boolean) {
        InjectUtils.println("transform start")
        mProject.dependencies
        for (input in inputs!!) {
            for (directoryInput in input.directoryInputs) {
                val dest = outputProvider!!.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY)

                try {
                    // 只有这一句是有用的
                    inject(directoryInput.file.absolutePath, dest.absolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            for (jarInput in input.jarInputs) {
                var jarName = jarInput.name
                val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length - 4)
                }
                val dest = outputProvider!!.getContentLocation(
                        jarName + md5Name,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
        InjectUtils.println("transform finish")
    }

    private fun inject(path: String, dest: String) {
        val dir = File(path)
        if (dir.isDirectory) {
            InjectUtils.println("dest dir:$dest")
            mInjectTool.injectDir(dir)

            val files = ArrayList<File>()
            iteratorPath(dir, files, null)
            for (file in files) {
                val filePath = file.absolutePath
                if (filePath.endsWith(".class")
                        && !filePath.contains("R$")
                        && !filePath.contains("R.class")
                        && !filePath.contains("BuildConfig.class")) {
                    mInjectTool.injectFile(dir, file)
                }
            }
        }
    }

    private fun iteratorPath(dir: File, pathName: MutableList<File>, fileFilter: FileFilter?) {
        if (!dir.isDirectory) {
            return
        }
        val files = dir.listFiles(fileFilter)
        if (files != null) {
            for (file in files) {
                if (file.isFile) {
                    pathName.add(file)
                } else if (file.isDirectory) {
                    iteratorPath(file, pathName, fileFilter)
                }
            }
        }
    }
}