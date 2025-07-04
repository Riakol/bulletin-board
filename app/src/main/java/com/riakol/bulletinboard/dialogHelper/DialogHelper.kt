package com.riakol.bulletinboard.dialogHelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.riakol.bulletinboard.MainActivity
import com.riakol.bulletinboard.R
import com.riakol.bulletinboard.accountHelper.AccountHelper
import com.riakol.bulletinboard.databinding.SignDialogBinding

class DialogHelper(act: MainActivity) {
    private val activity = act
    private val accHelper = AccountHelper(activity)

    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(activity)
        val rootDialogElement = SignDialogBinding.inflate(activity.layoutInflater)
        builder.setView(rootDialogElement.root)
        setDialogState(index, rootDialogElement)

        val dialog = builder.create()

        rootDialogElement.btnSignUpIn.setOnClickListener {
            setOnClickSignUpIn(index, rootDialogElement, dialog)
        }
        rootDialogElement.btnForgetPass.setOnClickListener {
            setOnClickResetPassword(rootDialogElement, dialog)
        }

        dialog.show()
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
        }
    }
}