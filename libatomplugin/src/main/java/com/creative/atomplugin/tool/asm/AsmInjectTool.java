package com.creative.atomplugin.tool.asm;

import com.creative.atomplugin.InjectUtils;
import com.creative.atomplugin.IInjectTool;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AsmInjectTool implements IInjectTool {
    @Override
    public void injectDir(File dir) {

    }

    @Override
    public void injectFile(File dir, File file) {
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(file));
            ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS);
            ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5,classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    InjectUtils.println("visitMethod() called with: access = [" + access + "], name = [" + name + "], desc = [" + desc + "], signature = [" + signature + "], exceptions = [" + exceptions + "]");
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }

                @Override
                public void visitSource(String source, String debug) {
                    InjectUtils.println("visitSource() called with: source = [" + source + "], debug = [" + debug + "]");
                    super.visitSource(source, debug);
                }
            };
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            byte[] code = classWriter.toByteArray();
            FileOutputStream fos = new FileOutputStream(file.getParentFile().getAbsolutePath() + File.separator + file.getName());
            fos.write(code);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
