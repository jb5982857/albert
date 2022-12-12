package com.albert.application.lifecycle.common.utils

import android.content.Context
import com.albert.application.lifecycle.api.IAApplicationLifecycleTemp
import dalvik.system.DexFile
import java.io.IOException
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

object AApplicationClassLoader {
    fun getClassName(context: Context, packageName: String?): List<String> {
        val classNameList: MutableList<String> = ArrayList()
        try {
            val df = DexFile(context.packageCodePath) //通过DexFile查找当前的APK中可执行文件
            val enumeration = df.entries() //获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) { //遍历
                val className = enumeration.nextElement() as String
                if (className.contains(packageName!!)) { //在当前所有可执行的类里面查找包含有该包名的所有类
                    classNameList.add(className)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return classNameList
    }

    const val CLASS_NAME = "com.albert.application.lifecycle.apt.AApplicationApts"
    const val FUNC_NAME = "getPlugins"

    fun invokeLifecyclePluginsFunc(): List<IAApplicationLifecycleTemp> {
        val obj = getObjectByClassName<Any>(CLASS_NAME) ?: return emptyList()
        return callFunc<List<IAApplicationLifecycleTemp>>(obj, FUNC_NAME) ?: return emptyList()
    }

    fun <T> getObjectByClassName(className: String, vararg args: Any): T? {
        try {
            val clazz = Class.forName(className)

            var constructor: Constructor<*>? = null
            val constructors = clazz.constructors
            constructors.forEach {
                if (it.parameterTypes.size == args.size) {
                    constructor = if (args.isEmpty()) {
                        clazz.getConstructor()
                    } else {
                        clazz.getConstructor(*it.parameterTypes)
                    }
                }
            }

            constructor?.isAccessible = true
            val obj = if (args.isEmpty()) {
                constructor?.newInstance()
            } else {
                constructor?.newInstance(*args)
            }
            return obj as T
        } catch (e: ClassNotFoundException) {
            LogUtils.e("找不到类:$className", e)
        } catch (e: InstantiationException) {
            LogUtils.e("调用构造方法错误:$className", e)
        } catch (e: IllegalAccessException) {
            LogUtils.e("调用构造方法错误:$className", e)
        } catch (e: NoSuchMethodException) {
            LogUtils.e("找不到类 $className 的构造方法", e)
        } catch (e: SecurityException) {
            LogUtils.e("类 $className 的构造方法 不配访问", e)
        } catch (e: IllegalAccessException) {
            LogUtils.e("调用构造方法错误:$className", e)
        } catch (e: IllegalArgumentException) {
            LogUtils.e("调用构造方法错误:$className", e)
        } catch (e: InvocationTargetException) {
            LogUtils.e("调用构造方法错误:$className", e)
        }
        return null
    }

    fun <T> callFunc(obj: Any, methodName: String): T? {
        try {
            val clazz = obj.javaClass
            val result = clazz.getMethod(methodName).invoke(obj)
            return result as T
        } catch (e: NoSuchMethodException) {
            LogUtils.e("找不到方法:$methodName", e)
        } catch (e: SecurityException) {
            LogUtils.e("方法：$methodName 不配访问", e)
        }
        return null
    }

}