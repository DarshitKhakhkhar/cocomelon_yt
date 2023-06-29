package aculix.channelify.app.viewmodel

object PausedModel {
    private var isPaused: Boolean = false

    fun setYourVariable(value: Boolean) {
        isPaused = value
    }

    fun getYourVariable(): Boolean {
        return isPaused
    }
}