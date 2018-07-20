package com.creative.atom.data.newnode.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangshouchao on 2017/10/12.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Nodes {
    String name() default "";
    String soleName() default "";
    Class<?>[] exceptParents();
}
