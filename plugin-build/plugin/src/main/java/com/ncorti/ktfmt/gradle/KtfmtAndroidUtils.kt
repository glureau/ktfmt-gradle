package com.ncorti.ktfmt.gradle

import com.android.build.gradle.BaseExtension
import com.ncorti.ktfmt.gradle.KtfmtPluginUtils.createTasksForSourceSet
import java.util.concurrent.Callable
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

internal object KtfmtAndroidUtils {

    internal fun applyKtfmtToAndroidProject(
        project: Project,
        ktfmtExtension: KtfmtExtension,
        topLevelFormat: TaskProvider<Task>,
        topLevelCheck: TaskProvider<Task>
    ) {
        fun applyKtfmtForAndroid() {
            project.extensions.configure(BaseExtension::class.java) {
                it.sourceSets.all { sourceSet ->
                    // Passing Callable, so returned FileCollection, will lazy evaluate it
                    // only when task will need it.
                    // Solves the problem of having additional source dirs in
                    // current AndroidSourceSet, that are not available on eager
                    // evaluation.
                    createTasksForSourceSet(
                        project,
                        sourceSet.name,
                        project.files(Callable { sourceSet.java.srcDirs }),
                        ktfmtExtension,
                        topLevelFormat,
                        topLevelCheck
                    )
                }
            }
        }

        project.plugins.withId("com.android.application") { applyKtfmtForAndroid() }
        project.plugins.withId("com.android.library") { applyKtfmtForAndroid() }
        project.plugins.withId("com.android.test") { applyKtfmtForAndroid() }
        project.plugins.withId("com.android.dynamic-feature") { applyKtfmtForAndroid() }
    }
}
