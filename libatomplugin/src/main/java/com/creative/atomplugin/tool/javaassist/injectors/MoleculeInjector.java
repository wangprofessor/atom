package com.creative.atomplugin.tool.javaassist.injectors;

import com.creative.atom.data.Molecule;
import com.creative.atomplugin.tool.javaassist.ClassMethodBuilder;
import com.creative.atomplugin.tool.javaassist.IClassMethodBuilder;
import com.creative.atomplugin.tool.javaassist.IInjector;
import com.creative.atomplugin.tool.javaassist.builder.SelfCreateArrayBuilder;
import com.creative.atomplugin.tool.javaassist.builder.SelfCreateBuilder;
import com.creative.atomplugin.tool.javaassist.builder.SelfGetArraySubBuilder;
import com.creative.atomplugin.tool.javaassist.builder.SelfGetSubBuilder;
import com.creative.atomplugin.tool.javaassist.builder.SelfSetArraySubBuilder;
import com.creative.atomplugin.tool.javaassist.builder.SelfSplitBuilder;
import com.creative.atomplugin.tool.javaassist.builder.SelfSetSubBuilder;

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;

public class MoleculeInjector implements IInjector {
    @Override
    public boolean enable(Class<?> clazz) {
        return Molecule.class.isAssignableFrom(clazz);
    }

    @Override
    public void inject(Class<?> clazz, CtClass ctClass) {
        List<IClassMethodBuilder> builderList = new ArrayList<>();

        builderList.add(new ClassMethodBuilder(new SelfCreateBuilder()));
        builderList.add(new ClassMethodBuilder(new SelfCreateArrayBuilder()));
        builderList.add(new ClassMethodBuilder(new SelfSetSubBuilder()));
        builderList.add(new ClassMethodBuilder(new SelfGetSubBuilder()));
        builderList.add(new ClassMethodBuilder(new SelfSetArraySubBuilder()));
        builderList.add(new ClassMethodBuilder(new SelfGetArraySubBuilder()));
        builderList.add(new ClassMethodBuilder(new SelfSplitBuilder()));

        for (IClassMethodBuilder classMethodBuilder : builderList) {
            ClassMethodBuilder.injectMethod(classMethodBuilder, clazz, ctClass);
        }
    }

}
