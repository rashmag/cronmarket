package ooo.cron.delivery.screens.order_history_screen.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import ooo.cron.delivery.App
import ooo.cron.delivery.data.network.models.OrderHistoryNetModel
import ooo.cron.delivery.databinding.FragmentOrderHistoryBinding
import ooo.cron.delivery.screens.BaseFragment
import ooo.cron.delivery.utils.extensions.uiLazy

class OrderHistoryFragment : BaseFragment() {

    private var _binding: FragmentOrderHistoryBinding ?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<OrderHistoryViewModel> { viewModelFactory }

    private val orderHistoryAdapter by uiLazy {
        OrderHistoryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .orderHistoryComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setupViewModel()
    }

    private fun initAdapter(){
        with(binding.recyclerOrdersHistory){
            adapter = orderHistoryAdapter
        }
    }

    private fun setupViewModel(){
        with(viewModel){
            orderHistoryList.observe(viewLifecycleOwner) {
                showData(it.body() ?: listOf())
            }
        }
    }

    private fun showData(list: List<OrderHistoryNetModel>){
        orderHistoryAdapter.submitList(list)
    }
}