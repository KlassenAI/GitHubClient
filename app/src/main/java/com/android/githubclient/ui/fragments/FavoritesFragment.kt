package com.android.githubclient.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.githubclient.R
import com.android.githubclient.ui.activities.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoritesFragment : Fragment() {

    private lateinit var emptyRecyclerText: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Скачанные репозитории"
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onStart() {
        super.onStart()
        initFields()
    }

    private fun initFields() {
        emptyRecyclerText = requireActivity().findViewById(R.id.favorites_recycler_text)
        initFab()
        initRecycler()
        initViewModel()
    }

    private fun initFab() {
        fab = requireActivity().findViewById(R.id.fab)
        fab.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setCancelable(false)
            alertDialog.setTitle("Удаление")
            alertDialog.setMessage("Вы действительно хотите удалить информацию о всех репозиториях?")
            alertDialog.setPositiveButton("Да") { _, _ ->
                requireMainActivity().viewModel.deleteAll()
                Toast.makeText(
                    context,
                    "Информация о репозиториях была удалена",
                    Toast.LENGTH_SHORT
                ).show()
            }
            alertDialog.setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
    }

    private fun initRecycler() {
        recycler = requireActivity().findViewById(R.id.favorites_recycler)
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = requireMainActivity().favoritesAdapter
    }

    private fun initViewModel() {
        requireMainActivity().viewModel.favoritesRepositories.observe(this) {
            requireMainActivity().favoritesAdapter.setList(it)
            emptyRecyclerText.visibility =
                if (requireMainActivity().favoritesAdapter.itemCount == 0) VISIBLE else GONE
        }
    }

    private fun requireMainActivity() = (requireActivity() as MainActivity)
}

