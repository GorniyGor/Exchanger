package com.recan.exchanger.presentation.exchaner

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.recan.exchanger.databinding.ExchangerItemBinding
import com.recan.exchanger.presentation.Utils.exchangeRateFormat
import com.recan.exchanger.presentation.model.CurrencyModel

open class ExchangerAdapter(
    private val itemClickListener: (view: View, data: CurrencyModel) -> Unit,
    private val currencyChangedListener: (view: View, data: CurrencyModel, newValue: Double) -> Unit
): ListAdapter<CurrencyModel, ExchangerAdapter.ViewHolder>(CurrencyDiffCallback()) {

    var recyclerView: RecyclerView? = null
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onCurrentListChanged(
        previousList: MutableList<CurrencyModel>,
        currentList: MutableList<CurrencyModel>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        if(previousList.isNotEmpty() &&
            previousList[0] != currentList[0] &&
            recyclerView != null &&
            recyclerView!!.childCount > 0) {
            recyclerView?.scrollToPosition(0)
        }
    }
    //--------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ExchangerItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false).root
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ExchangerItemBinding.bind(itemView)
        private var data: CurrencyModel? = null

        init {

            itemView.setOnClickListener { v ->
                data?.let { itemClickListener(v, it) }
            }

            binding.etRate.addTextChangedListener(object : TextWatcher{
                var beforeText: String? = null

                override fun afterTextChanged(s: Editable?) {
                    if(binding.etRate.isFocused && s != null && data != null && beforeText != s.toString()) {
                        currencyChangedListener(itemView, data!!, s.convertToDouble())
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    beforeText = s?.toString()
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        fun bind(data: CurrencyModel){
            this.data = data
            binding.etRate.setTextKeepState(data.rate.convertToString())
            if(data.isBaseCurrency) binding.etRate.requestFocus()
            binding.tvCurrencyName.apply {
                if(text != data.name) text = data.name
            }
        }

        private fun Editable.convertToDouble(): Double =
            exchangeRateFormat.parse(toString().let { if(it.isEmpty()) "0,0" else it }).toDouble()

        private fun Double.convertToString(): String =
            if (this == 0.0) "" else exchangeRateFormat.format(this)
    }

    class CurrencyDiffCallback : DiffUtil.ItemCallback<CurrencyModel>() {
        override fun areItemsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean {
            return oldItem == newItem || (oldItem.isBaseCurrency && oldItem.name == newItem.name)
        }

    }
}