package com.example.dataapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.alert_dialog_box.view.*

class CustomAdapter(var context: Context, var data:ArrayList<DataItem>): BaseAdapter() {
    internal var dbHelper = SQLiteConfig(context)

    private class ViewHolder(row: View?) {
        var name:TextView
        var profession:TextView
        var residence:TextView
        var password:TextView
        var imgDelete:ImageView
        var imgEdit:ImageView

        init {
            this.name = row?.findViewById(R.id.tvName) as TextView
            this.profession = row?.findViewById(R.id.tvProfession) as TextView
            this.residence = row?.findViewById(R.id.tvResidence) as TextView
            this.password = row?.findViewById(R.id.tvPassword) as TextView
            this.imgDelete = row?.findViewById(R.id.imgDelete) as ImageView
            this.imgEdit = row?.findViewById(R.id.imgEdit) as ImageView
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.card_item_row, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val dataItem:DataItem = getItem(position) as DataItem
        val name = dataItem.name
        val profession = dataItem.profession
        val residence = dataItem.residence
        val password = dataItem.password

        viewHolder.name.text = name
        viewHolder.profession.text = profession
        viewHolder.residence.text = residence
        viewHolder.password.text = password

//        update functionality
        viewHolder.imgEdit.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.alert_dialog_box, parent, false)
            dialogBuilder.setView(dialogView)

//            populate the edit texts with data items
            dialogView.detName.setText(name)
            dialogView.detProfession.setText(profession)
            dialogView.detResidence.setText(residence)
            dialogView.detPassword.setText(password)

//            create the builder on the front face
            dialogBuilder.setTitle("Edit ${name}")
            dialogBuilder.setMessage("Do you want to edit ${name}")

//            when a user selects edit
            dialogBuilder.setPositiveButton("Edit", {dialog, which ->
//                get data entered by user
                val updateName = dialogView.detName.text.toString()
                val updateProfession = dialogView.detProfession.text.toString()
                val updateResidence = dialogView.detResidence.text.toString()
                val updatePassword = dialogView.detPassword.text.toString()

                val cursor = dbHelper.allData
                while (cursor.moveToNext()) {
                    if (name == cursor.getString(1)) {
                        val id = cursor.getString(0)
                        dbHelper.updateData(id, updateName, updateProfession, updateResidence, updatePassword)
                    }
                }
                this.notifyDataSetChanged()
                Toast.makeText(context, "$name record has been updated", Toast.LENGTH_SHORT).show()

            })

//            when user selects cancel
            dialogBuilder.setNegativeButton("Cancel", {dialog, which -> dialog.dismiss()})
            dialogBuilder.create().show()

        }

        viewHolder.imgDelete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle("Delete")
            dialogBuilder.setMessage("Confirm delete $name")

//            to delete data we need to delete the actual record then update our list view

            dialogBuilder.setPositiveButton("Delete", {dialog, which ->
//                delete data
                dbHelper.deleteData(name)

//                refresh our list view
                val users: ArrayList<DataItem> = ArrayList()
                val cursor = dbHelper.allData
                if (cursor.count == 0) {
                    Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show()
                }else {
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
                    this.notifyDataSetChanged()
                }
            })
            dialogBuilder.setNegativeButton("Cancel", {dialog, which -> dialog.dismiss() })
            dialogBuilder.create().show()
        }


        return view as View
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }
}