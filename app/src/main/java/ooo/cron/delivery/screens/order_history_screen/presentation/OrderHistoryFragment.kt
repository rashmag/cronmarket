package ooo.cron.delivery.screens.order_history_screen.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Named
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.OrderHistoryNetModel
import ooo.cron.delivery.databinding.FragmentOrderHistoryBinding
import ooo.cron.delivery.screens.BaseFragment
import ooo.cron.delivery.screens.order_history_detail_screen.presentation.OrderHistoryDetailFragment
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.uiLazy
import ooo.cron.delivery.utils.itemdecoration.SpaceItemDecoration

class OrderHistoryFragment : BaseFragment() {

    private var _binding: FragmentOrderHistoryBinding ?= null
    private val binding get() = _binding!!

    @Inject
    @Named("List")
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<OrderHistoryViewModel> { viewModelFactory }

    private val orderHistoryAdapter by uiLazy {
        OrderHistoryAdapter(
            onOrderClick = {
                navigateOrderDetail(it)
            }
        )
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
        onBackPressed()
    }

    private fun initAdapter() {
        with(binding.recyclerOrdersHistory) {
            adapter = orderHistoryAdapter
            addItemDecoration(
                SpaceItemDecoration(
                    MARGIN_SPACING_VALUE_34,
                    MARGIN_SPACING_VALUE_48,
                    MARGIN_SPACING_VALUE_48
                )
            )
        }
    }

    private fun navigateOrderDetail(orderId: String) {

        childFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
            .replace(R.id.order_history_container, OrderHistoryDetailFragment.newInstance(orderId))
            .addToBackStack(null)
            .commitAllowingStateLoss()

        binding.recyclerOrdersHistory.makeGone()
    }

    private fun setupViewModel() {
        with(viewModel) {
            orderHistoryList.observe(viewLifecycleOwner) {
                showData(it.body() ?: listOf())
            }
        }
    }

    private fun showData(list: List<OrderHistoryNetModel>){
        orderHistoryAdapter.submitList(list)
    }

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        childFragmentManager.beginTransaction().remove(OrderHistoryFragment())
        _binding = null
    }

    companion object{
        const val MARGIN_SPACING_VALUE_34 = 34
        const val MARGIN_SPACING_VALUE_48 = 48
    }
}