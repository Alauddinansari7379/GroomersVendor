pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
        maven {
            url =uri("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases")
        }
        maven { url =uri( "https://maven.aliyun.com/repository/public") }
        maven { url =uri( "https://maven.aliyun.com/repository/jcenter")}
    }
}

rootProject.name = "Groomers Vendor"
include(":app")
 