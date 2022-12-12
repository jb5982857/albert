
### 本地插件发布
1. module 依赖 upload.gradle，并且要在 version = 之后
举例： 

version = "0.1.0"
//这个必须在 version 之后
apply from: "../upload.gradle"

或Android Model:


2. 发布插件
   1. ./gradlew clean assemble
   2. ./gradlew generatePomFileForPluginPublication
   3. ./gradlew publishPluginPublicationToRepoRepository
