package com.example.dataapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var dbHelper = SQLiteConfig(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        listen to a click event on the add data image
        imgAdd.setOnClickListener{
//            grab data from form
            val name: String = etName.text.trim().toString()
            val profession: String = etProfession.text.trim().toString()
            val residence: String = etResidence.text.trim().toString()
            val password: String = etPassword.text.trim().toString()

            if (name.isEmpty() || profession.isEmpty() || residence.isEmpty() || password.isEmpty()) {
//                show message to user
                showMessage("Missing Data", "Please fill in the empty fields")
            } else {
//                store data in database
                dbHelper.insertData(name, profession, residence, password)
                showMessage("Success", "Data added successfully")
//                cleat the input fields
                clearText()
            }
        }

        imgNext.setOnClickListener {
            startActivity(Intent(this, UsersActivity::class.java))
        }
    }

    fun showMessage(title:String, message:String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)

        alertDialog.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
        alertDialog.create().show()
    }

    private fun clearText() {
        etName.setText("")
        etProfession.setText("")
        etResidence.setText("")
        etPassword.setText("")
    }
}
