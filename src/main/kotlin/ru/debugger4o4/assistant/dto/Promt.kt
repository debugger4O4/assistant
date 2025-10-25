package ru.debugger4o4.assistant.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Promt {
    private var choices: List<Choice>? = null
    private var created: Long = 0
    private var model: String? = null
    private var objectField: String? = null
    private var usage: Usage? = null

    constructor()

    constructor(choices: List<Choice>?, created: Long, model: String?, objectField: String?, usage: Usage?) {
        this.choices = choices
        this.created = created
        this.model = model
        this.objectField = objectField
        this.usage = usage
    }

    fun getChoices(): List<Choice>? = choices
    fun setChoices(choices: List<Choice>?) { this.choices = choices }

    fun getCreated(): Long = created
    fun setCreated(created: Long) { this.created = created }

    fun getModel(): String? = model
    fun setModel(model: String?) { this.model = model }

    fun getObjectField(): String? = objectField
    fun setObjectField(objectField: String?) { this.objectField = objectField }

    fun getUsage(): Usage? = usage
    fun setUsage(usage: Usage?) { this.usage = usage }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Choice {
    private var message: Message? = null
    private var index: Int = 0
    private var finishReason: String? = null

    constructor()

    constructor(message: Message?, index: Int, finishReason: String?) {
        this.message = message
        this.index = index
        this.finishReason = finishReason
    }

    fun getMessage(): Message? = message
    fun setMessage(message: Message?) { this.message = message }

    fun getIndex(): Int = index
    fun setIndex(index: Int) { this.index = index }

    fun getFinishReason(): String? = finishReason
    fun setFinishReason(finishReason: String?) { this.finishReason = finishReason }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Message {
    private var content: String? = null
    private var role: String? = null

    constructor()

    constructor(content: String?, role: String?) {
        this.content = content
        this.role = role
    }

    fun getContent(): String? = content
    fun setContent(content: String?) { this.content = content }

    fun getRole(): String? = role
    fun setRole(role: String?) { this.role = role }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Usage {
    private var promptTokens: Int = 0
    private var completionTokens: Int = 0
    private var totalTokens: Int = 0
    private var preCachedPromptTokens: Int = 0

    constructor()

    constructor(promptTokens: Int, completionTokens: Int, totalTokens: Int, preCachedPromptTokens: Int) {
        this.promptTokens = promptTokens
        this.completionTokens = completionTokens
        this.totalTokens = totalTokens
        this.preCachedPromptTokens = preCachedPromptTokens
    }

    fun getPromptTokens(): Int = promptTokens
    fun setPromptTokens(promptTokens: Int) { this.promptTokens = promptTokens }

    fun getCompletionTokens(): Int = completionTokens
    fun setCompletionTokens(completionTokens: Int) { this.completionTokens = completionTokens }

    fun getTotalTokens(): Int = totalTokens
    fun setTotalTokens(totalTokens: Int) { this.totalTokens = totalTokens }

    fun getPreCachedPromptTokens(): Int = preCachedPromptTokens
    fun setPreCachedPromptTokens(preCachedPromptTokens: Int) { this.preCachedPromptTokens = preCachedPromptTokens }
}