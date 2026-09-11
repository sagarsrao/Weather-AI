plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.myapplication"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

tasks.register("verifyCoverage") {
    dependsOn("createDebugAndroidTestCoverageReport")

    doLast {
        val reportFile = file(
            "build/reports/coverage/androidTest/debug/connected/report.xml"
        )

        if (!reportFile.exists()) {
            throw GradleException(
                "JaCoCo coverage report not found: ${reportFile.absolutePath}"
            )
        }

        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()

        factory.setFeature(
            "http://apache.org/xml/features/nonvalidating/load-external-dtd",
            false
        )

        factory.setFeature(
            "http://xml.org/sax/features/validation",
            false
        )

        factory.setFeature(
            "http://apache.org/xml/features/disallow-doctype-decl",
            false
        )

        factory.isXIncludeAware = false
        factory.isExpandEntityReferences = false

        val document = factory.newDocumentBuilder().parse(reportFile)

        val counters = document.getElementsByTagName("counter")

        var instructionMissed = 0
        var instructionCovered = 0
        var branchMissed = 0
        var branchCovered = 0

        for (i in 0 until counters.length) {
            val counter = counters.item(i)

            val type = counter.attributes
                .getNamedItem("type")
                ?.nodeValue

            when (type) {
                "INSTRUCTION" -> {
                    instructionMissed = counter.attributes
                        .getNamedItem("missed")
                        .nodeValue
                        .toInt()

                    instructionCovered = counter.attributes
                        .getNamedItem("covered")
                        .nodeValue
                        .toInt()
                }

                "BRANCH" -> {
                    branchMissed = counter.attributes
                        .getNamedItem("missed")
                        .nodeValue
                        .toInt()

                    branchCovered = counter.attributes
                        .getNamedItem("covered")
                        .nodeValue
                        .toInt()
                }
            }
        }

        val instructionTotal =
            instructionMissed + instructionCovered

        val branchTotal =
            branchMissed + branchCovered

        val instructionCoverage =
            if (instructionTotal > 0) {
                instructionCovered.toDouble() /
                    instructionTotal * 100
            } else {
                0.0
            }

        val branchCoverage =
            if (branchTotal > 0) {
                branchCovered.toDouble() /
                    branchTotal * 100
            } else {
                0.0
            }

        val minimumInstructionCoverage = 80.0
        val minimumBranchCoverage = 50.0

        println()
        println("========================================")
        println("      JaCoCo Coverage Quality Gate")
        println("========================================")
        println(
            "Instruction Coverage: %.2f%% (minimum %.2f%%)"
                .format(
                    instructionCoverage,
                    minimumInstructionCoverage
                )
        )
        println(
            "Branch Coverage:      %.2f%% (minimum %.2f%%)"
                .format(
                    branchCoverage,
                    minimumBranchCoverage
                )
        )
        println("========================================")
        println()

        if (instructionCoverage < minimumInstructionCoverage) {
            throw GradleException(
                "Instruction coverage %.2f%% is below required %.2f%%"
                    .format(
                        instructionCoverage,
                        minimumInstructionCoverage
                    )
            )
        }

        if (branchCoverage < minimumBranchCoverage) {
            throw GradleException(
                "Branch coverage %.2f%% is below required %.2f%%"
                    .format(
                        branchCoverage,
                        minimumBranchCoverage
                    )
            )

            println("JaCoCo coverage quality gate PASSED.")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.turbine:turbine:1.0.0")

    // UI/Instrumented Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.6")
    androidTestImplementation("io.mockk:mockk-android:1.13.8")

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
