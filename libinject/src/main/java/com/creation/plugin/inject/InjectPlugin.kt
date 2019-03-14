package com.creation.plugin.inject

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension

import org.gradle.api.Plugin
import org.gradle.api.Project

class InjectPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        InjectUtils.println("apply plugin:" + project.name)
        val appExtension = project.extensions.findByType(AppExtension::class.java)
        val libExtension = project.extensions.findByType(LibraryExtension::class.java)
        if (libExtension != null) {
            InjectUtils.println("type:lib")
            libExtension.registerTransform(ClassTransform(project, false, libExtension))
        }
        if (appExtension != null) {
            InjectUtils.println("type:app")
            appExtension.registerTransform(ClassTransform(project, true, appExtension))
        }
    }
}