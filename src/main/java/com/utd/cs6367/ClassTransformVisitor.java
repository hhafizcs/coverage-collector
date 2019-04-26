package com.utd.cs6367;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class ClassTransformVisitor extends ClassVisitor implements Opcodes {
	private String className;
	
    public ClassTransformVisitor(final ClassVisitor cv, String className) {
    	super(ASM7, cv);
    	this.className = className.replace("/", ".");
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        
        if(mv == null) {
        	return null;
        } else {
        	return new MethodTransformVisitor(mv, className, access, desc, name);
        }
    }
}