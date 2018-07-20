package com.creative.atomplugin.tool.javaassist.builder;

import com.creative.atom.data.Molecule;

public class BuilderUtils {
    public static final String MoleculeC = Molecule.class.getName();

    public static final String ENTER = "\r\n";
    private static final String EMPTY = "    ";

    public static String getEmpty(int depth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            builder.append(BuilderUtils.EMPTY);
        }
        return builder.toString();
    }

    public static void println(StringBuilder builder) {
        builder.append(BuilderUtils.ENTER);
    }

    public static void println(StringBuilder builder, int depth, String code) {
        builder.append(BuilderUtils.getEmpty(depth)).append(code).append(BuilderUtils.ENTER);
    }

    public static String println() {
        return BuilderUtils.ENTER;
    }

    public static String println(int depth, String code) {
        return BuilderUtils.getEmpty(depth) + code + BuilderUtils.ENTER;
    }

    public static String printLogo(Class<?> clazz, String methodName, int depth) {
        return println(depth, String.format("System.out.println(\"INJECT METHOD:%s:%s\");", clazz.getName(), methodName));
    }

    public static String primitiveToObject(Class<?> clazz, String value) {
        String returnValue;
        if (clazz.isPrimitive()) {
            if (clazz == int.class) {
                returnValue = String.format("new Integer(%s)", value);
            } else if (clazz == boolean.class) {
                returnValue = String.format("new Boolean(%s)", value);
            } else if (clazz == long.class) {
                returnValue = String.format("new Long(%s)", value);
            } else if (clazz == byte.class) {
                returnValue = String.format("new Byte(%s)", value);
            } else if (clazz == short.class) {
                returnValue = String.format("new Short(%s)", value);
            } else if (clazz == float.class) {
                returnValue = String.format("new Float(%s)", value);
            } else if (clazz == double.class) {
                returnValue = String.format("new Double(%s)", value);
            } else if (clazz == char.class) {
                returnValue = String.format("new Char(%s)", value);
            } else {
                throw new RuntimeException();
            }
        } else {
            returnValue = value;
        }
        return returnValue;
    }

    public static String objectToPrimitive(Class<?> clazz, String value) {
        String returnValue;
        if (clazz.isPrimitive()) {
            if (clazz == int.class) {
                returnValue = String.format("((Integer) %s).intValue()", value);
            } else if (clazz == boolean.class) {
                returnValue = String.format("((Boolean) %s).booleanValue()", value);
            } else if (clazz == long.class) {
                returnValue = String.format("((Long) %s).longValue()", value);
            } else if (clazz == byte.class) {
                returnValue = String.format("((Byte) %s).byteValue()", value);
            } else if (clazz == short.class) {
                returnValue = String.format("((Short) %s).shortValue()", value);
            } else if (clazz == float.class) {
                returnValue = String.format("((Float) %s).floatValue()", value);
            } else if (clazz == double.class) {
                returnValue = String.format("((Double) %s).doubleValue()", value);
            } else if (clazz == char.class) {
                returnValue = String.format("((Character) %s).charValue()", value);
            } else {
                throw new RuntimeException();
            }
        } else {
            returnValue = value;
        }
        return returnValue;
    }
}
