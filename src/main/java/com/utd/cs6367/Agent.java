package com.utd.cs6367;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Agent {
	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className.contains("org/apache/commons/dbutils")) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                    ClassTransformVisitor ctv = new ClassTransformVisitor(cw, className);
                    cr.accept(ctv, 0);
                    return cw.toByteArray();
                }

                return classfileBuffer;
            }
        });
	}
}