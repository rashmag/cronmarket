package ooo.cron.delivery.screens.market_category_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import ooo.cron.delivery.App
import ooo.cron.delivery.data.network.models.Partner
import ooo.cron.delivery.data.network.models.TagsResult
import ooo.cron.delivery.databinding.FragmentMarketCategoryBinding
import ooo.cron.delivery.screens.BaseFragment
import ooo.cron.delivery.screens.partners_screen.PartnersActivity
import java.lang.Exception
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
class MarketCategoryFragment() : BaseFragment(),
    MarketCategoryContract.View {

    @Inject
    lateinit var presenter: MarketCategoryContract.Presenter

    @Inject
    lateinit var binding: FragmentMarketCategoryBinding

    @Inject
    lateinit var tagsAdapter: TagsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root.apply {
        configureTagsList()
        configurePartnersList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun getMarketCategoryId(): Int? = arguments
        ?.getInt(ARGUMENT_MARKET_CATEGORY_ID)
        ?: kotlin.run {
            throw Exception("$ARGUMENT_MARKET_CATEGORY_ID $ARGUMENT_NOT_PROVIDED_EXCEPTION_MESSAGE")
        }

    override fun getMarketCategoryName(): String? = arguments
        ?.getString(ARGUMENT_MARKET_CATEGORY_NAME)
        ?: kotlin.run {
            throw Exception("$ARGUMENT_MARKET_CATEGORY_NAME $ARGUMENT_NOT_PROVIDED_EXCEPTION_MESSAGE")
        }

    override fun getMarketCategoryImg(): String? = arguments
        ?.getString(ARGUMENT_MARKET_CATEGORY_IMAGE)
        ?: kotlin.run {
            throw Exception("$ARGUMENT_MARKET_CATEGORY_IMAGE $ARGUMENT_NOT_PROVIDED_EXCEPTION_MESSAGE")
        }

    override fun showTags(tags: TagsResult) {
        tagsAdapter.update(tags)
    }

    override fun showPartners(
        dataSource: PartnersDataSource,
        pageSize: Int
    ) {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()

        val pagedList: PagedList<Partner> = PagedList.Builder(dataSource, config)
            .setNotifyExecutor(ContextCompat.getMainExecutor(requireContext()))
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()

        (binding.rvMarketCategoryPartners.adapter as PartnersAdapter).submitList(pagedList)
    }

    override fun navigatePartnerScreen(partnerId: String, isOpen: Boolean, openHours: Int?, openMinutes: Int?) {
        startActivityForResult(Intent(requireContext(), PartnersActivity::class.java)
            .apply {
                putExtra(PartnersActivity.EXTRA_PARTNER_ID, partnerId)
                putExtra(PartnersActivity.EXTRA_IS_OPEN, isOpen)
                putExtra(PartnersActivity.EXTRA_OPEN_HOURS, openHours)
                putExtra(PartnersActivity.EXTRA_OPEN_MINUTES, openMinutes)
            },
            PartnersActivity.RESULT_CODE
        )
    }

    private fun injectDependencies() {
        App.appComponent.marketCategoryBuilder()
            .inflater(layoutInflater)
            .build()
            .inject(this)
    }

    private fun configurePartnersList() {
        binding.rvMarketCategoryPartners.setHasFixedSize(false)
        binding.rvMarketCategoryPartners.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMarketCategoryPartners.adapter = PartnersAdapter{
            presenter.onPartnerClicked(it.id, it.isOpen(), it.openTime()[HOURS], it.openTime()[MINUTES])
        }
    }

    private fun configureTagsList() {
        binding.rvMarketCategoryTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMarketCategoryTags.adapter = tagsAdapter
    }

    companion object {
        const val ARGUMENT_MARKET_CATEGORY_ID = "MARKET_CATEGORY_ID"
        const val ARGUMENT_MARKET_CATEGORY_NAME = "MARKET_CATEGORY_NAME"
        const val ARGUMENT_MARKET_CATEGORY_IMAGE = "MARKET_CATEGORY_IMAGE"
        val ARGUMENT_NOT_PROVIDED_EXCEPTION_MESSAGE = "is not provided to ${this::class.java.name}"

        const val HOURS = 0
        const val MINUTES = 1
    }
}