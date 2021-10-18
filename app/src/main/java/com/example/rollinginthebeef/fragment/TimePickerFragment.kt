package com.example.rollinginthebeef.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.rollinginthebeef.R
import java.util.*

class TimePickerFragment:DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        var minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(activity, 2, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val tranferTime: Button = activity?.findViewById(R.id.tranferTime) as Button
        val minuteNew: String = if(minute<10) "0${minute.toString()}" else minute.toString()
        tranferTime.text = "$hourOfDay:$minuteNew"

    }

    override fun onCancel(dialog: DialogInterface) {
        Toast.makeText(activity, "Please select a time", Toast.LENGTH_SHORT).show()
        super.onCancel(dialog)
    }

}