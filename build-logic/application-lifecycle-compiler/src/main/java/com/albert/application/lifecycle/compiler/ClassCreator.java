package com.albert.application.lifecycle.compiler;

import com.albert.application.lifecycle.annotation.AApplicationLifecycle;
import com.albert.application.lifecycle.api.IAApplicationLifecycleTemp;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class ClassCreator {
    //key是view在xml中的id，value是作用在类上面的element对象
    private final Map<AApplicationLifecycle, String> mVariableElements = new HashMap<>();

    private String mPackageName;
    private static final String CLASS_NAME = "AApplication" + System.currentTimeMillis();
    private static final String CONTAINER_CLASS = "com.albert.application.lifecycle.api.IAApplicationLifecycle";

    public ClassCreator(String PackageName) {
        mPackageName = PackageName;
    }

    public void putElement(AApplicationLifecycle lifecycle, String className) {
        mVariableElements.put(lifecycle, className);
    }

    public TypeSpec generateJavaCode() throws ClassNotFoundException {
        return TypeSpec.classBuilder(CLASS_NAME)
                //public 修饰类
                .addModifiers(Modifier.PUBLIC)
                //添加类的方法
                .addMethod(generateMethod())
                //实现
                .addSuperinterface(ParameterizedTypeName.get(IAApplicationLifecycleTemp.class))
                //构建Java类
                .build();
    }

    private MethodSpec generateMethod() {
        //获取所有注解的类的类名
        //public Map<String, Class<*>> getPlugins()
        //构建方法--方法名
        return MethodSpec.methodBuilder("getPlugins")
                //public方法
                .addModifiers(Modifier.PUBLIC)
                //返回void
                .returns(ParameterizedTypeName.get(List.class))
                //方法传参（参数全类名，参数名）
                //方法代码
                .addCode(generateMethodCode())
                .build();
    }

    /**
     * java.util.ArrayList<com.dh.usdk.support.router.api.IAApplicationLifecycle> result = new java.util.ArrayList<>();
     * try {
     * result.add(new $className());
     * } catch (ClassNotFoundException e) {
     * e.printStackTrace();
     * }
     * return result;
     */
    private String generateMethodCode() {
        StringBuilder code = new StringBuilder();
        code.append("java.util.ArrayList<" + CONTAINER_CLASS + "> result = new java.util.ArrayList<>();");
        for (AApplicationLifecycle plugin : mVariableElements.keySet()) {
            String className = mVariableElements.get(plugin);
            String putCode = "result.add(new " + className + "());";
            code.append(putCode);
        }
        code.append("return result;");
        return code.toString();
    }

    public String getPackageName() {
        return mPackageName;
    }
}
