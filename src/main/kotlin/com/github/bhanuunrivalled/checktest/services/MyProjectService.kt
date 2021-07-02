package com.github.bhanuunrivalled.checktest.services

import com.github.bhanuunrivalled.checktest.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
