package com.example.android.politicalpreparedness.representative.adapter

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.android.politicalpreparedness.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("profileImage")
fun ImageView.fetchImage(src: String?) {
    src?.let {
        val uri = it.toUri().buildUpon().scheme("https").build()
        //TODO: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        Glide.with(context).load(uri).apply(
                RequestOptions().transform(
                        RoundedCorners(context.resources.getDimensionPixelSize(R.dimen._100sdp)),
                        CenterInside()
                    )
            ).placeholder(R.drawable.ic_profile).into(this)
    } ?: kotlin.run {
        Glide.with(context).load(R.drawable.ic_profile).into(this)
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    this.adapter?.let {
        val adapter = toTypedAdapter<String>(it as ArrayAdapter<*>)
        val position = when (adapter.getItem(0)) {
            is String -> adapter.getPosition(value)
            else -> this.selectedItemPosition
        }
        if (position >= 0) {
            setSelection(position)
        }
    }
}

@BindingAdapter("textDate")
fun TextView.showText(date: Date?) {
    val text = date?.let {
        val simpleDateFormat = SimpleDateFormat("E MMM dd HH:mm:ss 'EDT' yyyy", Locale.US)
        try {
            simpleDateFormat.format(it)
        } catch (ex: ParseException) {
            ""
        }
    }
    setText(text)
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {
    return adapter as ArrayAdapter<T>
}
