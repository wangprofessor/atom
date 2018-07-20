package com.creative.atomplugin.tool.javaassist;

import com.creative.atomplugin.InjectUtils;
import com.creative.atomplugin.IInjectTool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class JavaAssistInjectTool implements IInjectTool {
    private ClassPool mClassPool;
    private final HashSet<String> mModuleSet = new HashSet<>();

    private URLClassLoader mClassLoader;
    private final Method mURLMethod;

    private final String mRootPath;
    private final Injector mInjector = new Injector();

    private String mLastFlavor = "";

    public JavaAssistInjectTool(String rootPath) {
        mRootPath = rootPath;

        mModuleSet.add("libjava");
        mModuleSet.add("libandroid");

        try {
            mURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            mURLMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void injectDir(File dir) {
        initAssistFlavor(dir);

        initAssistDir(dir);
        initClassLoaderDir(dir);

    }

    private void initAssistFlavor(File dir) {
        String dirPath = dir.getAbsolutePath();
        int index = dirPath.lastIndexOf('/');
        String flavor = dirPath.substring(index + 1, dirPath.length());

        if (flavor.equals(mLastFlavor)) {
            return;
        }

        mClassPool = new ClassPool(null);
        mClassPool.appendSystemPath();

        mClassLoader = new URLClassLoader(new URL[0], getClass().getClassLoader());
        mInjector.setClassLoader(mClassLoader);

        for (String module : mModuleSet) {
            String path = mRootPath + "/" + module + "/build/intermediates/classes/" + flavor;
            InjectUtils.println("lib path:" + path);
            try {
                mClassPool.appendClassPath(path);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        mLastFlavor = flavor;
    }

    private void initClassLoaderDir(File dir) {
        try {
            mURLMethod.invoke(mClassLoader, dir.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAssistDir(File dir) {
        try {
            mClassPool.appendClassPath(dir.getAbsolutePath());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void injectFile(File dir, File file) {
        String dirPath = dir.getAbsolutePath();
        String filePath = file.getAbsolutePath();
        String className = getClassName(dirPath, filePath);

        try {
            CtClass clazz = mClassPool.getCtClass(className);
            if (clazz.isFrozen()) {
                clazz.defrost();
            }
            boolean inject = mInjector.inject(filePath, clazz);
            if (inject) {
                clazz.writeFile(dirPath);
            }
        } catch (NotFoundException | CannotCompileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getClassName(String dirPath, String filePath) {
        filePath = filePath.substring(dirPath.length() + 1);
        filePath = filePath.replace('/', '.').replace('\\', '.');
        filePath = filePath.substring(0, filePath.length() - ".class".length());
        return filePath;
    }
}
