package asm

import org.apache.commons.io.FileUtils
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import java.io.File


class AApplicationWriter(val aptClassPackages: List<String>) {
    companion object {
        const val CLASS_NAME = "AApplicationApts.class"
    }

    fun start(javaPath: String) {
        val classWriter = ClassWriter(0)
        var methodVisitor: MethodVisitor

        classWriter.visit(
            Opcodes.V1_8,
            Opcodes.ACC_PUBLIC or Opcodes.ACC_SUPER,
            "com/albert/application/lifecycle/apt/AApplicationApts",
            null,
            "java/lang/Object",
            null
        )

        "class".apply {
            methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
            methodVisitor.visitCode()
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false
            )
            methodVisitor.visitInsn(Opcodes.RETURN)
            methodVisitor.visitMaxs(1, 1)
            methodVisitor.visitEnd()
        }
        "fun".apply {
            methodVisitor = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "getPlugins",
                "()Ljava/util/List;",
                "()Ljava/util/List<Lcom/albert/application/lifecycle/api/IAApplicationLifecycleTemp;>;",
                null
            )
            methodVisitor.visitCode()
            methodVisitor.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList")
            methodVisitor.visitInsn(Opcodes.DUP)
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false
            )
            methodVisitor.visitVarInsn(Opcodes.ASTORE, 1)
            aptClassPackages.forEach {
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                methodVisitor.visitTypeInsn(Opcodes.NEW, it)
                methodVisitor.visitInsn(Opcodes.DUP)
                methodVisitor.visitMethodInsn(
                    Opcodes.INVOKESPECIAL, it, "<init>", "()V", false
                )
                methodVisitor.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true
                )
                methodVisitor.visitInsn(Opcodes.POP)
            }

            methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
            methodVisitor.visitInsn(Opcodes.ARETURN)
            methodVisitor.visitMaxs(3, 2)
            methodVisitor.visitEnd()
        }
        classWriter.visitEnd()

        val bytes = classWriter.toByteArray()
        FileUtils.writeByteArrayToFile(File(javaPath), bytes)
    }
}