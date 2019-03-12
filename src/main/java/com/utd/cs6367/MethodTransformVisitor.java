package com.utd.cs6367;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodTransformVisitor extends MethodVisitor implements Opcodes {
	String className;
	int lineNumber;
	
    public MethodTransformVisitor(final MethodVisitor mv, String className) {
        super(ASM7, mv);
        this.className = className;
    }

    @Override
    public void visitLineNumber(int line, Label start){
    	lineNumber = line;
    	
    	if(lineNumber != 0) {
        	mv.visitLdcInsn(className + ":" + lineNumber);
        	mv.visitMethodInsn(INVOKESTATIC, "com/utd/cs6367/CoverageStorage", "addToCoverageList", "(Ljava/lang/String;)V", false);
    	}
    	
    	super.visitLineNumber(line, start);
    }
    
    @Override
    public void visitLabel(Label label){
    	if(lineNumber != 0) {
    		mv.visitLdcInsn(className + ":" + lineNumber);
        	mv.visitMethodInsn(INVOKESTATIC, "com/utd/cs6367/CoverageStorage", "addToCoverageList", "(Ljava/lang/String;)V", false);
    	}
    	
    	super.visitLabel(label);
    }
}