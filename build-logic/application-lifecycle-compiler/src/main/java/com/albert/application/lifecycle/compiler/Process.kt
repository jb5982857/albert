package com.albert.application.lifecycle.compiler

import com.albert.application.lifecycle.annotation.AApplicationLifecycle
import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

/**
 * 生成原则：
 * 生成在 $APT_PATH 路径在，并且类名为 AApplication${timestamp}
 */
@AutoService(Processor::class)
class Process : AbstractProcessor() {
    private var mElementUtils: Elements? = null
    private var mCreator: ClassCreator? = null

    companion object {
        const val APT_PATH = "com.albert.application.lifecycle.apt"
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(AApplicationLifecycle::class.java.canonicalName)
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        println(" ${this::class.java.simpleName} init")
        mElementUtils = processingEnv.elementUtils
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements =
            roundEnv.getElementsAnnotatedWith(AApplicationLifecycle::class.java)
        if (elements.isEmpty()) {
            return false
        }
        println("执行到 process了 $elements")
        if (mCreator == null) {
            mCreator =
                ClassCreator(APT_PATH)
        }
        //遍历元素
        for (element in elements) {
            //获取元素注解
            val bindAnnotation = element.getAnnotation(AApplicationLifecycle::class.java)
            //注解值
            mCreator?.putElement(bindAnnotation, element.toString())
        }
        val javaFile = JavaFile.builder(
            mCreator?.packageName,
            mCreator?.generateJavaCode()
        ).build()
        try {
            javaFile.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }
}