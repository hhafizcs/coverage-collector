package com.utd.cs6367;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Agent {
	public static void premain(String agentArgs, Instrumentation instrumentation) {
		instrumentation.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) throws IllegalClassFormatException {
                if (className.contains("org/apache/commons/dbutils")
                		|| className.contains("org/ahocorasick")
                		|| className.contains("ru/yandex/qatools")
                		|| className.contains("com/github/vbauer/caesar")
                		|| className.contains("org/trendafilov/confucius")
                		|| className.contains("org/ebaysf/web/cors")
                		|| className.contains("eu/danieldk/dictomaton")
                		|| className.contains("net/objecthunter/exp4j")
                		|| className.contains("ch/hsr/geohash")
                		|| className.contains("org/geojson")
                		|| className.contains("io/gsonfire")) {
                    ClassReader cr = new ClassReader(classFileBuffer);
                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                    ClassTransformVisitor classTransformVisitor = new ClassTransformVisitor(cw, className);
                    cr.accept(classTransformVisitor, 0);
                    return cw.toByteArray();
                }

                return classFileBuffer;
            }
        });
	}
}