package com.android.githubclient.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.githubclient.R
import com.android.githubclient.adapters.RepositoryListItemAdapter.RepositoryViewHolder
import com.android.githubclient.databinding.ListItemBinding
import com.android.githubclient.model.Repository
import com.android.githubclient.ui.viewmodel.MainViewModel

class RepositoryListItemAdapter(
    private var items: List<Repository>,
    private var activity: Activity,
    private var viewModel: MainViewModel
) : RecyclerView.Adapter<RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val itemBinding: ListItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item,
                parent,
                false
            )
        return RepositoryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = items[position]
        val favorite = viewModel.isExist(repository.id)
        holder.binding.repository = repository
        holder.binding.listItemImg.setImageResource(
            if (favorite) R.drawable.ic_baseline_file_download_done_24
            else R.drawable.ic_baseline_download_24
        )
        holder.binding.listItemImg.setOnClickListener {
            if (favorite) {
                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setCancelable(false)
                alertDialog.setTitle("Удаление")
                alertDialog.setMessage("Вы действительно хотите удалить информацию о репозитории?")
                alertDialog.setPositiveButton("Да") { _, _ ->
                    viewModel.delete(repository)
                    showToast("Информация о репозитории была удалена")
                }
                alertDialog.setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            } else {
                viewModel.insert(repository)
                showToast("Репозиторий скачивается по ссылке")
                openSite("https://api.github.com/repos/${repository.owner.login}/${repository.name}/zipball/${repository.defaultBranch}")
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class RepositoryViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setList(list: List<Repository>) {
        items = list
        notifyDataSetChanged()
    }

    private fun showToast(text: String) {
        Toast.makeText(activity.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    private fun openSite(html: String) {
        val address: Uri = Uri.parse(html)
        val openLinkIntent = Intent(Intent.ACTION_VIEW, address)
        activity.startActivity(openLinkIntent)
    }
}
