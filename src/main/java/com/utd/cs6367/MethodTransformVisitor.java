package com.utd.cs6367;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodTransformVisitor extends MethodVisitor implements Opcodes {
	String className;
	int line;
	
    public MethodTransformVisitor(final MethodVisitor mv, String className) {
        super(ASM7, mv);
        this.className = className;
    }

    // statement coverage collection
    @Override
    public void visitLineNumber(int line, Label start){
    	this.line = line;
    	if(line != 0) {
        	mv.visitLdcInsn(className + ":" + line);
        	mv.visitMethodInsn(INVOKESTATIC, "com/utd/cs6367/CoverageStorage", "addToCovLst", "(Ljava/lang/String;)V", false);
    	}
    	super.visitLineNumber(line, start);
    }
    
    // statement coverage collection
    @Override
    public void visitLabel(Label label){
    	if(line != 0) {
    		mv.visitLdcInsn(className + ":" + line);
        	mv.visitMethodInsn(INVOKESTATIC, "com/utd/cs6367/CoverageStorage", "addToCovLst", "(Ljava/lang/String;)V", false);
    	}
    	super.visitLabel(label);
    }
}