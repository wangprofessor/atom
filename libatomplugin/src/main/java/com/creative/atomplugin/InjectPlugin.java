package com.creative.atomplugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.TestedExtension;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import groovy.lang.Closure;
import javassist.ClassPool;

public class InjectPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        InjectUtils.println("apply plugin:" + project.getName());
        TestedExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        TestedExtension libExtension = project.getExtensions().findByType(LibraryExtension.class);
        if(libExtension != null) {
            InjectUtils.println("type:lib");
            libExtension.registerTransform(new ClassTransform(project, false, libExtension));
        }
        if(appExtension != null) {
            InjectUtils.println("type:app");
            appExtension.registerTransform(new ClassTransform(project, true, appExtension));
        }
    }
}