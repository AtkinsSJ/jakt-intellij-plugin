package org.serenityos.jakt.plugin.highlighting

import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.serenityos.jakt.plugin.JaktLanguage

class JaktColorSettingsPage : ColorSettingsPage {
    private val DESCRIPTORS = mapOf(
        "Delimiters//Braces" to JaktSyntaxHighlighter.BRACES_KEY,
        "Delimiters//Brackets" to JaktSyntaxHighlighter.BRACKETS_KEY,
        "Delimiters//Parentheses" to JaktSyntaxHighlighter.PARENS_KEY,
        "Delimiters//Semicolon" to JaktSyntaxHighlighter.SEMICOLON_KEY,

        "Operators//Dot" to JaktSyntaxHighlighter.DOT_KEY,
        "Operators//Comma" to JaktSyntaxHighlighter.COMMA_KEY,
        "Operators//Other" to JaktSyntaxHighlighter.OPERATOR_KEY,

        "Numbers" to JaktSyntaxHighlighter.NUMBERS_KEY,
        "Text Literals//Strings" to JaktSyntaxHighlighter.STRINGS_KEY,
        "Text Literals//Characters" to JaktSyntaxHighlighter.CHAR_KEY,

        "Keywords//Literals" to JaktSyntaxHighlighter.LITERAL_KEY,
        "Keywords//Control Flow" to JaktSyntaxHighlighter.CONTROL_FLOW_KEY,
        "Keywords//Modifiers" to JaktSyntaxHighlighter.MODIFIER_KEY,
        "Keywords//Misc" to JaktSyntaxHighlighter.MISC_KEY,
        "Keywords//Unsafe" to JaktSyntaxHighlighter.UNSAFE_KEY,

        "Syntax Error" to JaktSyntaxHighlighter.ERROR_KEY,
    ).map { AttributesDescriptor(it.key, it.value) }.toTypedArray()

    override fun getIcon() = JaktLanguage.ICON

    override fun getHighlighter() = JaktSyntaxHighlighter()

    // TODO: Eventually put every feature here
    override fun getDemoText() = """
        function main() {
            let i = 5
            if i == 5 and 7 > 6 {
                println("OK")
            }
            let a = true
            let b = true
            if a and b {
                println("OK")
            }
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap() = null

    override fun getAttributeDescriptors() = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = JaktLanguage.displayName
}