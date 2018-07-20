package com.creative.atomplugin;

import com.android.build.api.transform.*;
import com.android.build.gradle.TestedExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.collect.Sets;
import com.creative.atomplugin.tool.asm.AsmInjectTool;
import com.creative.atomplugin.tool.javaassist.JavaAssistInjectTool;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

class ClassTransform extends Transform {
    private Project mProject;
    private boolean mIsApp;
    private TestedExtension mExtension;

    private IInjectTool mInjectTool;

    ClassTransform(Project project, boolean isApp, TestedExtension extension) {
        mProject = project;
        mIsApp = isApp;
        mExtension = extension;

        mInjectTool = new JavaAssistInjectTool(mProject.getRootDir().getAbsolutePath());
//        mInjectTool = new AsmInjectTool();
    }

    @Override
    public String getName() {
        return "class editor";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        if(mIsApp) {
            return Sets.immutableEnumSet(
                    QualifiedContent.Scope.PROJECT,
                    QualifiedContent.Scope.SUB_PROJECTS,
                    QualifiedContent.Scope.EXTERNAL_LIBRARIES);
        } else {
            return Sets.immutableEnumSet(
                    QualifiedContent.Scope.PROJECT);
        }
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(Context context,
                   Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider,
                   boolean isIncremental) throws IOException {
        InjectUtils.println("transform start");
        mProject.getDependencies();
        for (TransformInput input : inputs) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(
                        directoryInput.getName(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY);

                try {
                    // 只有这一句是有用的
                    inject(directoryInput.getFile().getAbsolutePath(), dest.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FileUtils.copyDirectory(directoryInput.getFile(), dest);
            }

            for (JarInput jarInput : input.getJarInputs()) {
                String jarName = jarInput.getName();
                String md5Name = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath());
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4);
                }
                File dest = outputProvider.getContentLocation(
                        jarName + md5Name,
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                FileUtils.copyFile(jarInput.getFile(), dest);
            }
        }
        InjectUtils.println("transform finish");
    }

    private void inject(String path, String dest) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            InjectUtils.println("dest dir:" + dest);
            mInjectTool.injectDir(dir);

            List<File> files = new ArrayList<>();
            iteratorPath(dir, files, null);
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                if (filePath.endsWith(".class")
                        && !filePath.contains("R$")
                        && !filePath.contains("R.class")
                        && !filePath.contains("BuildConfig.class")) {
                    mInjectTool.injectFile(dir, file);
                }
            }
        }
    }

    private static void iteratorPath(File dir, List<File> pathName, FileFilter fileFilter) {
        if (!dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(fileFilter);
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    pathName.add(file);
                } else if (file.isDirectory()) {
                    iteratorPath(file, pathName, fileFilter);
                }
            }
        }
    }
}