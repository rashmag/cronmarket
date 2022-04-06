package ooo.cron.delivery.screens.favorite_screen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ooo.cron.delivery.App
import ooo.cron.delivery.data.network.models.Partner
import ooo.cron.delivery.databinding.FragmentFavoritePartnersBinding
import ooo.cron.delivery.di.screens.favorite_partners.FavoritePartnersViewModelFactory
import ooo.cron.delivery.screens.base.BaseMVVMFragment
import ooo.cron.delivery.screens.partners_screen.PartnersActivity
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible
import ooo.cron.delivery.utils.extensions.uiLazy
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 02.04.2022
 * */

class FavoritePartnersFragment() : BaseMVVMFragment() {

    @Inject
    lateinit var binding: FragmentFavoritePartnersBinding

    @Inject
    lateinit var factory: FavoritePartnersViewModelFactory.Factory

    override val baseViewModel: FavoritePartnersViewModel by viewModels {
        factory.create()
    }

    private val favoritePartnersAdapter by uiLazy {
        FavoritePartnersAdapter(
            onPartnerClick = {
                navigatePartnerScreen(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onStart() {
        super.onStart()
        baseViewModel.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavoritePartners.adapter = favoritePartnersAdapter

        //обзерв избранных партнеров
        baseViewModel.favPartners.observe(viewLifecycleOwner) {
            if (it.partners.isEmpty()) {
                showNoFavoriteInfo()
            } else {
                showFavoritePartners(it.partners ?: listOf())
            }
        }
    }

    private fun showNoFavoriteInfo() {
        binding.rvFavoritePartners.makeGone()
        binding.favListEmpty.makeVisible()
    }

    fun navigatePartnerScreen(partner:Partner) {
        startActivityForResult(
            Intent(requireContext(), PartnersActivity::class.java)
                .apply {
                    putExtra(EXTRA_PARTNER_ID, partner.id)
                    putExtra(EXTRA_IS_OPEN, partner.isOpen())
                    putExtra(EXTRA_OPEN_HOURS, partner.openTime()[HOURS])
                    putExtra(EXTRA_OPEN_MINUTES, partner.openTime()[MINUTES])
                },
            PartnersActivity.RESULT_CODE
        )
    }

    private fun showFavoritePartners(partners: List<Partner>) {
        binding.rvFavoritePartners.makeVisible()
        binding.favListEmpty.makeGone()
        favoritePartnersAdapter.submitList(partners)
    }

    private fun injectDependencies() {
        App.appComponent.favoritePartnersComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)
    }

    companion object {
        const val HOURS = 0
        const val MINUTES = 1
        const val EXTRA_PARTNER_ID = "partnerId"
        const val EXTRA_IS_OPEN = "is_open"
        const val EXTRA_OPEN_HOURS = "open_hours"
        const val EXTRA_OPEN_MINUTES = "open_minutes"
    }
}