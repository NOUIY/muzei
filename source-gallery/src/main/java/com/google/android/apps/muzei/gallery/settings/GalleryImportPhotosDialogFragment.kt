package com.google.android.apps.muzei.gallery.settings

import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.appcompat.R as AppCompatR
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.apps.muzei.gallery.R
import com.google.android.apps.muzei.util.collectIn
import com.google.android.material.R as MaterialR
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GalleryImportPhotosDialogFragment : DialogFragment() {

    companion object {
        private const val TAG = "GalleryImportPhotosDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            GalleryImportPhotosDialogFragment().show(fragmentManager, TAG)
        }
    }

    private val viewModel: GallerySettingsViewModel by activityViewModels()
    private var listener: OnRequestContentListener? = null
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().withStyledAttributes(
            attrs = AppCompatR.styleable.AlertDialog,
            defStyleAttr = AppCompatR.attr.alertDialogStyle
        ) {
            @LayoutRes val listItemLayout = getResourceId(AppCompatR.styleable.AlertDialog_listItemLayout, 0)
            adapter = ArrayAdapter(requireContext(), listItemLayout)
        }
        viewModel.getContentActivityInfoList.collectIn(this) { getContentActivities ->
            if (getContentActivities.isEmpty()) {
                dismiss()
            } else {
                updateAdapter(getContentActivities)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(),
                MaterialR.style.Theme_MaterialComponents_DayNight_Dialog_Alert)
                .setTitle(R.string.gallery_import_dialog_title)
                .setAdapter(adapter) { _, which ->
                    viewModel.getContentActivityInfoList.value.run {
                        listener?.requestGetContent(get(which))
                    }
                }.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnRequestContentListener ?: throw IllegalArgumentException(
                "${context.javaClass.simpleName} must implement OnRequestContentListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun updateAdapter(getContentActivities: List<ActivityInfo>) {
        val packageManager = requireContext().packageManager
        adapter.apply {
            clear()
            addAll(getContentActivities.map { it.loadLabel(packageManager) })
            notifyDataSetChanged()
        }
    }

    fun interface OnRequestContentListener {
        fun requestGetContent(info: ActivityInfo)
    }
}