package builds.apiReferences.buildTypes

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

const val dokkaSubmodulePatchKotlin = """
  pluginsMapConfiguration.set(mapOf(\"org.jetbrains.dokka.base.DokkaBase\" to \"\"\"{ \"templatesDir\" : \"${"\\$"}{
    rootProject.projectDir.toString().replace('\\\', '/')
  }/dokka-templates\" }\"\"\"))
"""

object KotlinxCoroutines: BuildType({
  name = "Kotlinx.Coroutines"

  templates(DokkaReferenceTemplate)

  params {
    param("release.tag", BuildParams.KOTLINX_COROUTINES_RELEASE_TAG)
  }

  vcs {
    root(builds.apiReferences.vcsRoots.KotlinxCoroutines)
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
        sed -i '/^suppressInheritedMembers.set(true)/a $dokkaSubmodulePatchKotlin' gradle/dokka.gradle.kts
      """.trimIndent()
    }
  }
})
