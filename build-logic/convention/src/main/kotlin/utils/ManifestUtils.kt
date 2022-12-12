package utils

import org.dom4j.Document
import org.dom4j.DocumentException
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.*

object ManifestUtils {
    private const val APPLICATION = "application"
    private const val ACTIVITY = "activity"
    private const val META_DATA = "meta-data"
    private const val NAME = "name"
    private const val VALUE = "value"
    private const val MODULE_IS_MAIN = "module_is_main"
    private const val BASE_APPLICATION_PATH = "com.albert.common.compoents.BaseApplication"

    private const val LAUNCH_NODE_TEXT =
        "<intent-filter " +
                "xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                ">\n" +
                "<action android:name=\"android.intent.action.MAIN\" />" +
                "<category android:name=\"android.intent.category.LAUNCHER\" />" +
                "</intent-filter>\n"

    fun addLaunchActivityIfNeeded(manifestPath: String): Boolean {
        try {
            val rootDoc = SAXReader().read(File(manifestPath))
            val applicationNode =
                (rootDoc.content()[0] as Element).content().find { it.name == APPLICATION }
                    ?: return false

            (applicationNode as Element).apply {
                val applicationNameAttr = this.attribute(NAME)
                if (applicationNameAttr == null) {
                    this.addAttribute("android:$NAME", BASE_APPLICATION_PATH)
                }

                val activityNodes =
                    this.content().filter { it.name == ACTIVITY }
                        ?: return false

                activityNodes.forEach { activityNode ->
                    val metaData =
                        (activityNode as Element).content()?.filter { it.name == META_DATA }
                    metaData?.forEach {
                        val attrs = (it as Element).attributes()
                        val nameAttr = attrs.find { it.name == NAME && it.value == MODULE_IS_MAIN }
                        if (nameAttr != null) {
                            //找到了这个meta-data
                            val valueAttr = attrs.find { it.name == VALUE } ?: return@forEach
                            val value = valueAttr.value
                            if (value == "true") {
                                addLaunchNode(activityNode)
                            }
                        }

                    }
                }
            }
            update(rootDoc)
            return true
        } catch (e: Exception) {
            throw RuntimeException("添加 launcher activity 时报错", e)
        }
    }

    private fun update(document: Document) {
        try {
            val fileWriter: Writer =
                FileWriter(document.name.replaceFirst("file://".toRegex(), ""))
            val format = OutputFormat.createPrettyPrint() //缩减型格式
            //OutputFormat format = OutputFormat.createCompactFormat();//紧凑型格式
            format.encoding = "utf-8" //设置编码
            //format.setTrimText(false);//设置text中是否要删除其中多余的空格
            val xmlWriter = XMLWriter(fileWriter, format)

            /*如果想要对xml文件的输出格式进行设置，就必须用XMLWriter类，但是我们又需要保留其中的空格，此时我们就需要对format进行设置，也就是加上一句format
             .setTrimText(false);
                这样就可以既保持xml文件的输出格式，也可以保留其中的空格;element中attribute中的值如果有空格的话在任何情况下是都不会去除空格的；*/
            xmlWriter.write(document)
            xmlWriter.close()
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        }
    }


    private fun addLaunchNode(activityNode: Element): Boolean {
        val manifestNodeReader = SAXReader()
        try {
            val launchEle =
                manifestNodeReader.read(ByteArrayInputStream(LAUNCH_NODE_TEXT.toByteArray(charset("UTF-8"))))
            val nodes = launchEle.content()
            nodes.forEach {
                activityNode.content().add(it)
            }
            return true
        } catch (e: DocumentException) {
            throw RuntimeException(e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }
    }
}