package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Agent {

  public static void premain(String agentArgs, Instrumentation inst) {
    inst.addTransformer(new ClassFileTransformer() {
      @Override
      public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {

        if ("other/Stuff".equals(s)) {
          // ASM Code
          // ClassReader reader = new ClassReader(bytes);
          // ClassWriter writer = new ClassWriter(reader, 0);
          // ClassPrinter visitor = new ClassPrinter(writer);
          // reader.accept(visitor, 0);
          // return writer.toByteArray();

          // Javassist
          try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.get("other.Stuff");
            CtMethod m = cc.getDeclaredMethod("runWithParams");
            m.insertBefore("{ System.out.println($1 + ',' + $2); }");
            byte[] byteCode = cc.toBytecode();
            cc.detach();
            return byteCode;
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }

        return null;
      }
    });
  }

}

