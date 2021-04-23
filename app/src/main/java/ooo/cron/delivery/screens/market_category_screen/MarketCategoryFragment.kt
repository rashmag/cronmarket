package ooo.cron.delivery.screens.market_category_screen

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
import java.lang.Exception
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
class MarketCategoryFragment : BaseFragment(),
    MarketCategoryContract.View {

    @Inject
    lateinit var presenter: MarketCategoryContract.Presenter

    @Inject
    lateinit var binding: FragmentMarketCategoryBinding

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

    override fun onStart() {
        super.onStart()
        presenter.onStartView()
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

    override fun showTags(tags: TagsResult) {
        (binding.rvMarketCategoryTags.adapter as TagsAdapter).update(tags)
        (binding.rvMarketCategoryTags.adapter as TagsAdapter).notifyDataSetChanged()
    }

    override fun showPartners(
        dataSource: PartnersDataSource,
        prefetchDistance: Int,
        pageSize: Int
    ) {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(prefetchDistance)
            .setPageSize(pageSize)
            .build()

        val pagedList: PagedList<Partner> = PagedList.Builder(dataSource, config)
            .setNotifyExecutor(ContextCompat.getMainExecutor(context))
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()

        (binding.rvMarketCategoryPartners.adapter as PartnersAdapter).submitList(pagedList)
    }

    private fun injectDependencies() {
        App.appComponent.marketCategoryBuilder()
            .inflater(layoutInflater)
            .build()
            .inject(this)
    }

    private fun configureTagsList() {
        binding.rvMarketCategoryTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMarketCategoryTags.adapter = TagsAdapter(
            presenter::onTagClick,
            presenter::onAllTagsClick,
            presenter::onOtherTagsClick
        )
    }

    private fun configurePartnersList() {
        binding.rvMarketCategoryPartners.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        binding.rvMarketCategoryPartners.adapter = PartnersAdapter()
    }

    companion object {
        const val ARGUMENT_MARKET_CATEGORY_ID = "MARKET_CATEGORY_ID"
        const val ARGUMENT_MARKET_CATEGORY_NAME = "MARKET_CATEGORY_NAME"
        val ARGUMENT_NOT_PROVIDED_EXCEPTION_MESSAGE = "is not provided to ${this::class.java.name}"
    }

}