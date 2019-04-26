package com.utd.cs6367;

import java.lang.reflect.Modifier;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class MethodTransformVisitor extends MethodVisitor implements Opcodes {
	String className;
	int lineNumber;
	int access;
	String desc;
	String methodName;
	
    public MethodTransformVisitor(final MethodVisitor mv, String className, int access, String desc, String methodName) {
        super(ASM7, mv);
        this.className = className;
        this.access = access;
        this.desc = desc;
        this.methodName = methodName;
    }
    
    @Override
    public void visitCode() {
    	Type[] argTypes = Type.getArgumentTypes(desc);
    	int numOfArgs = argTypes.length;
    	boolean isStatic = Modifier.isStatic(access);
    	int localVarIndex = isStatic ? 0 : 1;
    	int arrayIndex = getNextEmptyLocalVarIndex(argTypes);
    	
    	if(numOfArgs > 0) {
    		mv.visitIntInsn(Opcodes.BIPUSH, numOfArgs + 1);
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
            mv.visitVarInsn(Opcodes.ASTORE, arrayIndex);
            
            for (int i = 0; i < numOfArgs; i++) {
        		Type tp = argTypes[i];
        		
        		if(!(tp.equals(Type.BOOLEAN_TYPE) || tp.equals(Type.BYTE_TYPE)
        				|| tp.equals(Type.CHAR_TYPE) || tp.equals(Type.SHORT_TYPE)
        				|| tp.equals(Type.INT_TYPE) || tp.equals(Type.LONG_TYPE)
        				|| tp.equals(Type.FLOAT_TYPE) || tp.equals(Type.DOUBLE_TYPE)
        				|| tp.getSort() == Type.ARRAY || tp.getSort() == Type.OBJECT)) {
        			continue;
        		}
        		
        		mv.visitVarInsn(Opcodes.ALOAD, arrayIndex);
                mv.visitIntInsn(Opcodes.BIPUSH, i);
            	
            	if (tp.equals(Type.BOOLEAN_TYPE)) {
                    mv.visitVarInsn(Opcodes.ILOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                }
                else if (tp.equals(Type.BYTE_TYPE)) {
                    mv.visitVarInsn(Opcodes.ILOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                }
                else if (tp.equals(Type.CHAR_TYPE)) {
                    mv.visitVarInsn(Opcodes.ILOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
                }
                else if (tp.equals(Type.SHORT_TYPE)) {
                    mv.visitVarInsn(Opcodes.ILOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                }
                else if (tp.equals(Type.INT_TYPE)) {
                    mv.visitVarInsn(Opcodes.ILOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                }
                else if (tp.equals(Type.LONG_TYPE)) {
                    mv.visitVarInsn(Opcodes.LLOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                    localVarIndex++;
                }
                else if (tp.equals(Type.FLOAT_TYPE)) {
                    mv.visitVarInsn(Opcodes.FLOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                }
                else if (tp.equals(Type.DOUBLE_TYPE)) {
                    mv.visitVarInsn(Opcodes.DLOAD, localVarIndex);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                    localVarIndex++;
                }
                else if (tp.getSort() == Type.ARRAY || tp.getSort() == Type.OBJECT) {
                    mv.visitVarInsn(Opcodes.ALOAD, localVarIndex);
                }
            	
            	mv.visitInsn(Opcodes.AASTORE);
            	localVarIndex++;
        	}
            
            /*if(!isStatic) {
            	mv.visitVarInsn(Opcodes.ALOAD, arrayIndex);
                mv.visitIntInsn(Opcodes.BIPUSH, numOfArgs);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitInsn(Opcodes.AASTORE);
            }*/
        	
        	mv.visitVarInsn(Opcodes.ALOAD, arrayIndex);
        	mv.visitLdcInsn(className);
        	mv.visitLdcInsn(methodName);
        	
        	mv.visitMethodInsn(INVOKESTATIC, "com/utd/cs6367/CoverageStorage", "addVarsToVarCovList", "([Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false);
    	}
    	
    	super.visitCode();
    }

    @Override
    public void visitLineNumber(int line, Label start){
    	lineNumber = line;
    	
    	if(lineNumber != 0) {
        	mv.visitLdcInsn(className);
        	mv.visitLdcInsn(lineNumber);
        	mv.visitMethodInsn(INVOKESTATIC, "com/utd/cs6367/CoverageStorage", "addLineToStmtCovList", "(Ljava/lang/String;I)V", false);
    	}
    	
    	super.visitLineNumber(line, start);
    }
    
    @Override
    public void visitLabel(Label label){
    	if(lineNumber != 0) {
    		mv.visitLdcInsn(className);
        	mv.visitLdcInsn(lineNumber);
        	mv.visitMethodInsn(INVOKESTATIC, "com/utd/cs6367/CoverageStorage", "addLineToStmtCovList", "(Ljava/lang/String;I)V", false);
    	}
    	
    	super.visitLabel(label);
    }
    
    private int getNextEmptyLocalVarIndex(Type[] types) {
    	int localVarIndex = (access | Opcodes.ACC_STATIC) == 0 ? 0 : 1;
    	
    	for (Type type : types) {
    		if(type.equals(Type.BOOLEAN_TYPE) || type.equals(Type.BYTE_TYPE)
    				|| type.equals(Type.CHAR_TYPE) || type.equals(Type.SHORT_TYPE)
    				|| type.equals(Type.INT_TYPE) || type.equals(Type.FLOAT_TYPE)
    				|| type.getSort() == Type.ARRAY || type.getSort() == Type.OBJECT) {
    			localVarIndex += 1;
    		} else if(type.equals(Type.LONG_TYPE) || type.equals(Type.DOUBLE_TYPE)) {
    			localVarIndex += 2;
    		}
    	}
    	
    	return localVarIndex;
    }
}