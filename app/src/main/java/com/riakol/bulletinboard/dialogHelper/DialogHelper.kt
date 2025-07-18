package com.riakol.bulletinboard.dialogHelper

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.riakol.bulletinboard.MainActivity
import com.riakol.bulletinboard.R
import com.riakol.bulletinboard.accountHelper.AccountHelper
import com.riakol.bulletinboard.databinding.SignDialogBinding
import kotlinx.coroutines.launch

class DialogHelper(act: MainActivity) {
    private val activity = act
    private val accHelper = AccountHelper(activity)
    private val WEB_CLIENT_ID = "792839367465-i2da82aemf1tpgodfmees3rna1km98sd.apps.googleusercontent.com"

    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(activity)
        val rootDialogElement = SignDialogBinding.inflate(activity.layoutInflater)
        builder.setView(rootDialogElement.root)
        setDialogState(index, rootDialogElement)

        val dialog = builder.create()

        rootDialogElement.btnGoogleSignIn.setOnClickListener {
            launchSignIn()
            dialog.dismiss()
        }

        rootDialogElement.btnSignUpIn.setOnClickListener {
            setOnClickSignUpIn(index, rootDialogElement, dialog)
        }
        rootDialogElement.btnForgetPass.setOnClickListener {
            setOnClickResetPassword(rootDialogElement, dialog)
        }

        dialog.show()
    }

    private fun launchSignIn() {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        activity.lifecycleScope.launch {
            try {
                val credentialManager = CredentialManager.create(activity)
                val result = credentialManager.getCredential(
                    request = request,
                    context = activity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e("DialogHelper", "GetCredentialException: ", e)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        // Передаем учетные данные в AccountHelper для входа в Firebase
                        accHelper.signInWithGoogleCredential(googleIdTokenCredential)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("DialogHelper", "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e("DialogHelper", "Unexpected type of custom credential")
                }
            }
            else -> {
                Log.e("DialogHelper", "Unexpected type of credential")
            }
        }
    }

    private fun setOnClickResetPassword(
        rootDialogElement: SignDialogBinding,
        dialog: AlertDialog
    ) {
        if (rootDialogElement.edSignEmail.text.isNotEmpty()) {
            activity.mAuth.sendPasswordResetEmail(rootDialogElement.edSignEmail.text.toString()).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, R.string.email_reset_password_was_sent, Toast.LENGTH_LONG).show()
                }

            }
            dialog.dismiss()
        } else {
            rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
        }
    }

    private fun setOnClickSignUpIn(
        index: Int,
        rootDialogElement: SignDialogBinding,
        dialog: AlertDialog
    ) {
        dialog.dismiss()
        if (index == DialogConst.SIGN_UP_STATE) {
            accHelper.signUpWithEmail(
                rootDialogElement.edSignEmail.text.toString(),
                rootDialogElement.edSignPassword.text.toString()
            )
        } else {
            accHelper.signInWithEmail(
                rootDialogElement.edSignEmail.text.toString(),
                rootDialogElement.edSignPassword.text.toString()
            )
        }
    }

    private fun setDialogState(
        index: Int,
        rootDialogElement: SignDialogBinding
    ) {
        if (index == DialogConst.SIGN_UP_STATE) {
            rootDialogElement.tvSignTitle.text = activity.resources.getString(R.string.aс_sing_up)
            rootDialogElement.btnSignUpIn.text = activity.resources.getString(R.string.sign_up_action)
        } else {
            rootDialogElement.tvSignTitle.text = activity.resources.getString(R.string.aс_sing_in)
            rootDialogElement.btnSignUpIn.text = activity.resources.getString(R.string.sign_in_action)
            rootDialogElement.btnForgetPass.visibility = View.VISIBLE
            rootDialogElement.btnGoogleSignIn.visibility = View.VISIBLE
        }
    }
}