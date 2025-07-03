package com.riakol.bulletinboard.dialogHelper

import android.app.AlertDialog
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

        if (index == DialogConst.SIGN_UP_STATE) {
            rootDialogElement.tvSignTitle.text = activity.resources.getString(R.string.aс_sing_up)
            rootDialogElement.btnSignUpIn.text = activity.resources.getString(R.string.sign_up_action)
        } else {
            rootDialogElement.tvSignTitle.text = activity.resources.getString(R.string.aс_sing_in)
            rootDialogElement.btnSignUpIn.text = activity.resources.getString(R.string.sign_in_action)
        }

        rootDialogElement.btnSignUpIn.setOnClickListener {
            if (index == DialogConst.SIGN_UP_STATE) {
                accHelper.signUpWithEmail(
                    rootDialogElement.edSignEmail.text.toString(),
                    rootDialogElement.edSignPassword.text.toString()
                )
            } else {

            }
        }

        builder.setView(rootDialogElement.root)
        builder.show()
    }
}