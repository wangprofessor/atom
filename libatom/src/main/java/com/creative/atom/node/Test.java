package com.creative.atom.node;

public class Test {
    public static void main(String[] args) {
        String[] strings = new String[] {"1", "2", "3"};
        INode node = NodeManager.create(strings);
        System.out.println(node);
    }
}
