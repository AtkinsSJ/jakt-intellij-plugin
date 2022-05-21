package org.serenityos.jakt.plugin

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.FileViewProvider

class JaktFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, JaktLanguage) {
    override fun getFileType() = FileType

    override fun toString() = "Jakt File"

    object FileType : LanguageFileType(JaktLanguage) {
        override fun getName() = "Jakt File"

        override fun getDescription() = "Jakt language file"

        override fun getDefaultExtension() = "jakt"

        override fun getIcon() = JaktLanguage.ICON
    }
}