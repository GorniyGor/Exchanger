package com.recan.exchanger.presentation.exchaner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.recan.exchanger.R
import com.recan.exchanger.databinding.ExchangerActivityBinding
import com.recan.exchanger.presentation.base.BaseActivity
import com.recan.exchanger.presentation.model.CurrencyModel

class ExchangerActivity : BaseActivity<ExchangerPresenter.ExchangerView, ExchangerPresenter>(),
    ExchangerPresenter.ExchangerView {

    private lateinit var binding: ExchangerActivityBinding
    private lateinit var recyclerAdapter: ExchangerAdapter
    private val errorAnimator by lazy {
        ValueAnimator().apply {
            val errorView = binding.tvError
            duration = 700L
            addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                    if(!isReverse) errorView.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    if(isReverse) errorView.visibility = View.INVISIBLE
                }
            })
            addUpdateListener {
                (it.animatedValue as Int).toFloat().also {
                    binding.recyclerView.translationY = it
                    errorView.translationY = it
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExchangerActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        binding.toolbar.title = getString(R.string.exchanger_screen_title)
        setupRecycler()
        setupSwipeRefresh()
        presenter.loadData()
    }

    private fun setupSwipeRefresh(){
        binding.swipeRefresh.apply {
            isEnabled = false
            setOnRefreshListener { presenter.loadData() }
        }
    }
    private fun setupRecycler(){
        binding.recyclerView.apply {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setHasFixedSize(true)
            recyclerAdapter = ExchangerAdapter(
                    { _, data -> presenter.onCurrencySelected(data.name, data.rate) },
                    { _, data, value -> presenter.onCurrencyAmountChange(data.name, value) }
            )
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(this@ExchangerActivity)
        }
    }

    override fun updateData(list: List<CurrencyModel>) {
        recyclerAdapter.submitList(list)
    }

    override fun showError(throwable: Throwable) {
        binding.swipeRefresh.isEnabled = true
        binding.tvError.apply {
            if(visibility != View.VISIBLE || text != throwable.message) {
                text = throwable.message
                post { errorAnimator.apply { setIntValues(0, height) }.start() }
            }
        }
    }

    override fun hideError() {
        if(binding.tvError.visibility == View.VISIBLE && !errorAnimator.isRunning) {
            binding.swipeRefresh.isEnabled = false
            errorAnimator.reverse()
        }
    }

    override fun setLoading(isShowing: Boolean) {
        binding.swipeRefresh.isRefreshing = isShowing
    }
}
