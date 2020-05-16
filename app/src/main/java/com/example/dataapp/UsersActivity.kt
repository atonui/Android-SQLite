package com.example.dataapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {
    var dbHelper = SQLiteConfig(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

//        add new user
        imgAddUser.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        imgRefresh.setOnClickListener {
            refresh()
        }

//        pulling data from db and displaying it to user
        refresh()

    }

    private fun showMessage(title:String, message:String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)

        alertDialog.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
        alertDialog.create().show()
    }

    private fun refresh() {
        val users: ArrayList<DataItem> = ArrayList()
        val cursor = dbHelper.allData
        if (cursor.count == 0) {
            showMessage("No Data","You have not registered anyone.")
        } else {
            while (cursor.moveToNext()) {
                users.add(
                    DataItem(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                    )
                )
            }
            userList.adapter = CustomAdapter(this, users)
        }
    }
}


