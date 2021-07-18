package com.android.githubclient.ui.fragments

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.githubclient.R
import com.android.githubclient.ui.activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var emptyRecyclerText: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var swipe: SwipeRefreshLayout
    private var title: String? = null
    private var isMessageShown: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireMainActivity().title = if (title != null) title else "Поиск"
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onStart() {
        super.onStart()
        initFields()
    }

    private fun initFields() {
        emptyRecyclerText = requireMainActivity().findViewById(R.id.search_recycler_text)
        initSwipe()
        initRecycler()
        initViewModel()
    }

    private fun initSwipe() {
        swipe = requireMainActivity().findViewById(R.id.search_swipe)
        swipe.setOnRefreshListener {
            if (title != null) {
                requireMainActivity().viewModel.searchRepositories(title!!)
            }
            swipe.isRefreshing = false
        }
    }

    private fun initRecycler() {
        recycler = requireMainActivity().findViewById(R.id.search_recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        recycler.adapter = requireMainActivity().searchAdapter
    }

    private fun initViewModel() {
        requireMainActivity().viewModel.searchRepositories.observe(this) {
            if (it != null) {
                requireMainActivity().searchAdapter.setList(it)
                if (it.isEmpty()) showToast("Репозитории с таким названием не были найдены")
            } else showToast("Проверьте подключение к интернету")
            emptyRecyclerText.visibility = if (it == null || it.isEmpty()) VISIBLE else GONE
            swipe.isRefreshing = false
        }
    }

    private fun showToast(text: String) {
        if (!isMessageShown) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            isMessageShown = true
        }
    }

    private fun requireMainActivity() = (requireActivity() as MainActivity)

    private fun setTitle(query: String) {
        requireMainActivity().title = query
        title = query
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val item = menu.findItem(R.id.search_menu_item)
        val view = item.actionView as SearchView

        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                view.clearFocus()
                item.collapseActionView()
                recycler.scrollToPosition(0)
                swipe.isRefreshing = true
                GlobalScope.launch {
                    requireMainActivity().viewModel.searchRepositories(query)
                    isMessageShown = false
                }
                setTitle(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
    }
}