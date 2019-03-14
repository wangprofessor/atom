package com.creation.plugin.inject.tool.asm

import com.creation.plugin.inject.InjectUtils
import com.creation.plugin.inject.IInjectTool

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class AsmInjectTool : IInjectTool {
    override fun injectDir(dir: File) {

    }

    override fun injectFile(dir: File, file: File) {
        try {
            val classReader = ClassReader(FileInputStream(file))
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val classVisitor = object : ClassVisitor(Opcodes.ASM5, classWriter) {
                override fun visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array<String>): MethodVisitor {
                    InjectUtils.println("visitMethod() called with: access = [$access], name = [$name], desc = [$desc], signature = [$signature], exceptions = [$exceptions]")
                    return super.visitMethod(access, name, desc, signature, exceptions)
                }

                override fun visitSource(source: String, debug: String) {
                    InjectUtils.println("visitSource() called with: source = [$source], debug = [$debug]")
                    super.visitSource(source, debug)
                }
            }
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            val code = classWriter.toByteArray()
            val fos = FileOutputStream(file.parentFile.absolutePath + File.separator + file.name)
            fos.write(code)
            fos.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }
}
