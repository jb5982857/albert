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
    private const val INTENT_FILTER = "intent-filter"
    private const val NAME = "name"
    private const val VALUE = "value"
    private const val MODULE_IS_MAIN = "module_is_main"
    private const val BASE_APPLICATION_PATH = "com.albert.common.compoents.BaseApplication"
    private const val LAUNCHER = "android.intent.category.LAUNCHER"
    private const val CATEGORY = "category"

    private const val LAUNCH_NODE_TEXT =
        "<intent-filter " +
                "xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                ">\n" +
                "<action android:name=\"android.intent.action.MAIN\" />" +
                "<category android:name=\"android.intent.category.LAUNCHER\" />" +
                "</intent-filter>\n"

    fun addApplicationIfNeeded(manifestPath: String): Boolean {
        val rootDoc = SAXReader().read(File(manifestPath))
        val applicationNode =
            (rootDoc.content()[0] as Element).content().find { it.name == APPLICATION }
                ?: return false

        (applicationNode as Element).apply {
            val applicationNameAttr = this.attribute(NAME)
            if (applicationNameAttr == null) {
                this.addAttribute("android:$NAME", BASE_APPLICATION_PATH)
            }
        }
        update(rootDoc)
        return true
    }

    fun delLaunchActivityIfNeeded(manifestPath: String): Boolean {
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
                    val intentFilter =
                        (activityNode as Element).content()?.filter { it.name == INTENT_FILTER }
                    intentFilter?.forEach { intentNode ->
                        val intentContent = (intentNode as Element).content() ?: return@forEach
                        intentContent.forEach {
                            if (it is Element) {
                                val attrs = it.attributes()
                                if (it.name == CATEGORY) {
                                    val nameAttr =
                                        attrs.find { it.name == NAME && it.value == LAUNCHER }
                                    if (nameAttr != null) {
                                        //????????????activity
                                        //?????? LAUNCHER
                                        intentNode.remove(it)
                                    }
                                }
                            }
                        }

                    }
                }
            }
            update(rootDoc)
            return true
        } catch (e: Exception) {
            throw RuntimeException("?????? launcher activity ?????????", e)
        }
    }

    private fun update(document: Document) {
        try {
            val fileWriter: Writer =
                FileWriter(document.name.replaceFirst("file://".toRegex(), ""))
            val format = OutputFormat.createPrettyPrint() //???????????????
            //OutputFormat format = OutputFormat.createCompactFormat();//???????????????
            format.encoding = "utf-8" //????????????
            //format.setTrimText(false);//??????text???????????????????????????????????????
            val xmlWriter = XMLWriter(fileWriter, format)

            /*???????????????xml????????????????????????????????????????????????XMLWriter???????????????????????????????????????????????????????????????????????????format????????????????????????????????????format
             .setTrimText(false);
                ????????????????????????xml??????????????????????????????????????????????????????;element???attribute??????????????????????????????????????????????????????????????????????????????*/
            xmlWriter.write(document)
            xmlWriter.close()
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        }
    }


    private fun addNode(activityNode: Element): Boolean {
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