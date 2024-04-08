
plugins {
    id("eu.centralpay.core_banking.java-conventions")
    id("org.openapi.generator")
}

val projectName = "test-openapi"
description = "OpenAPI Core Banking API"


repositories {
    mavenCentral()
}

dependencies {
    val openApiToolsVersion = "0.2.6"
    val swaggerCoreV3Version = "2.2.10"

    implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
    implementation("io.swagger.core.v3:swagger-annotations:$swaggerCoreV3Version")
}

tasks {
    val openApiGenerate by getting

    withType<Jar> {
        archiveBaseName.set(projectName)
        manifest.attributes["CentralPay-Name"] = projectName
        from(sourceSets.main.get().output)
        dependsOn(openApiGenerate)
    }

    compileJava {
        dependsOn(openApiGenerate)
    }
}

val generatedSourcesDir: String = layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath
openApiGenerate {
    val openApiSpec = project.file("resources/openapi.yml").absolutePath
    val generatedPackageName = "eu.centralpay.core_banking.api"

    generatorName.set("spring")

    inputSpec.set(openApiSpec)
    outputDir.set(generatedSourcesDir)

    groupId.set("${project.group}")
    id.set(project.name)
    version.set("${project.version}")

    apiPackage.set("$generatedPackageName.api")
    modelPackage.set("$generatedPackageName.dto")
    modelNameSuffix.set("Dto")

    configOptions.set(
        mapOf(
            "annotationLibrary" to "swagger2",
            "configPackage" to "$generatedPackageName.config",
            "dateLibrary" to "java8",
            "documentationProvider" to "source",
            "generateModels" to "true",
            "interfaceOnly" to "true",
            "java8" to "true",
            "library" to "spring-boot",
            "performBeanValidation" to "true",
            "serializationLibrary" to "jackson",
            "unhandledException" to "true",
            "useBeanValidation" to "true",
            "useResponseEntity" to "true",
            "useSpringBoot3" to "true",
            "useSpringController" to "true",
            "useSwaggerUI" to "true",
            "useTags" to "true",
        )
    )
}

