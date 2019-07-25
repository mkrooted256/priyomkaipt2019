package com.example.priyomkaipt2019

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.priyomkaipt2019.db.AppDatabase

import com.example.priyomkaipt2019.db.Record
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordsFragment : Fragment() {
    lateinit var adapter: RecordRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: RecyclerView = inflater.inflate(R.layout.fragment_record_list, container, false) as RecyclerView

        view.layoutManager = LinearLayoutManager(context)
        adapter = RecordRecyclerViewAdapter(mutableListOf(), object: OnListFragmentInteractionListener{
            override fun onListFragmentInteraction(item: Record?) {
                //TODO Open details fragment
                Toast.makeText(activity, "Клац на фтішника на ім'я ${item?.name_i} ${item?.name_p}", Toast.LENGTH_LONG).show()
            }
        })
        view.adapter = adapter

        (context as MainActivity).crScope.launch {
            val data = AppDatabase.getInstance(context)?.recordDao()?.getAll();
            adapter.addItems(data)
        }

        return view
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Record?)
    }
}
