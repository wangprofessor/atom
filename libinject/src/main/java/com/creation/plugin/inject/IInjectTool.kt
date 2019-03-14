package com.creation.plugin.inject

import java.io.File

interface IInjectTool {
    fun injectDir(dir: File)
    fun injectFile(dir: File, file: File)
}
