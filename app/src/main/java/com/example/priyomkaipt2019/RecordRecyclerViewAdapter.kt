package com.example.priyomkaipt2019

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.example.priyomkaipt2019.RecordsFragment.OnListFragmentInteractionListener
import com.example.priyomkaipt2019.db.Record

import kotlinx.android.synthetic.main.fragment_record.view.*

class RecordRecyclerViewAdapter(
    private val mData: MutableList<Record>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = mData[position]
        holder.uidView.text = record.uid.toString()
        holder.namePView.text = record.name_p
        holder.nameIBView.text = record.name_i + " " + record.name_b

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(record)
        }
    }

    override fun getItemCount(): Int = mData.size

    fun addItems(records: List<Record>?) {
        if (records != null) {
            mData.addAll(records)
            notifyItemInserted(mData.size-1)
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val uidView: TextView = mView.record_uid
        val namePView: TextView = mView.record_pname
        val nameIBView: TextView = mView.record_ibname
    }
}
