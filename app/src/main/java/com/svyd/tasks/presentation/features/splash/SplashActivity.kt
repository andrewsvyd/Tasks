package com.svyd.tasks.presentation.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.features.authorization.AuthorizationActivity
import com.svyd.tasks.presentation.features.tasks.TasksActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = TasksApplication.instance.tokenManager.provideToken()
        if (token != null && !token.isEmpty()) {
            navigateToServers()
        } else {
            navigateToAuthorization()
        }
        finish()
    }

    private fun navigateToServers() {
        startActivity(Intent(this, TasksActivity::class.java))
    }

    private fun navigateToAuthorization() {
        startActivity(Intent(this, AuthorizationActivity::class.java))
    }
}
