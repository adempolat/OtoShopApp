package com.adempolat.otovinnapp.usecases

import android.content.Intent
import android.speech.RecognizerIntent
import javax.inject.Inject

class StartVoiceRecognitionUseCase @Inject constructor() {
    operator fun invoke(language: String, prompt: String): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
        }
    }
}
