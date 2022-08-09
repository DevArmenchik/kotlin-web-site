package builds.apiReferences.buildTypes

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

const val dokkaSubmodulePatchGroovy = """
  pluginsMapConfiguration.set([\"org.jetbrains.dokka.base.DokkaBase\": \"\"\"{ \"templatesDir\": \"${"\\$"}{
    rootProject.projectDir.toString().replace('\\\', '/')
  }/dokka-templates\" }\"\"\"])
"""
object KotlinxSerialization : BuildType({
  name = "Kotlinx.Serialization"

  templates(DokkaReferenceTemplate)

  params {
    param("release.tag", BuildParams.KOTLINX_SERIALIZATION_RELEASE_TAG)
  }

  vcs {
    root(builds.apiReferences.vcsRoots.KotlinxSerialization)
  }

  triggers {
    vcs {
      branchFilter = "+:<default>"
    }
  }

  steps {
    script {
      name = "Patch the gradle submodules"
      scriptContent = """
        sed -i '/^outputDirectory = file("build/dokka")/a $dokkaSubmodulePatchGroovy' gradle/dokka.gradle.kts
      """.trimIndent()
    }
  }
})
