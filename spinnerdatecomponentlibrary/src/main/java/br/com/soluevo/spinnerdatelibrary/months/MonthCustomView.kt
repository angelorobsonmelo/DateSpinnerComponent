package br.com.soluevo.spinnerdatelibrary.months

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.soluevo.spinnerdatelibrary.R
import br.com.soluevo.spinnerdatelibrary.commom.ViewModelFactory
import br.com.soluevo.spinnerdatelibrary.commom.di.ContextModule
import br.com.soluevo.spinnerdatelibrary.databinding.MonthComponentBinding
import br.com.soluevo.spinnerdatelibrary.domain.MonthResponse
import br.com.soluevo.spinnerdatelibrary.months.di.component.DaggerMonthComponent
import br.com.soluevo.spinnerdatelibrary.months.handler.MonthHandler
import com.jaredrummler.materialspinner.MaterialSpinner
import javax.inject.Inject


class MonthCustomView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var handler: MonthHandler? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: MonthComponentBinding
    private lateinit var mMaterialSpinner: MaterialSpinner
    private var viewModel: MonthViewModel? = null
    private val months = mutableListOf<MonthResponse>()

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.month_component, this, true
        )

        setUpElements()
    }

    private fun setUpElements() {
        injectDependencies()
    }

    private fun injectDependencies() {
        DaggerMonthComponent.builder()
            .contextModule(ContextModule(context))
            .build()
            .inject(this)
    }

    fun getMonthsFromActivity(
        cookieId: String,
        activity: AppCompatActivity
    ) {
        viewModel = ViewModelProviders.of(activity, viewModelFactory)[MonthViewModel::class.java]
        viewModel?.getMonths(cookieId)

        binding.lifecycleOwner = activity
        binding.viewModel = viewModel

        viewModel?.successObserver?.observe(activity, Observer {
            months.clear()
            months.addAll(it)
            setUpMonthSpinner()
            handler?.setMonsths(months)
        })

        viewModel?.errorObserver?.observe(activity, Observer {

        })

        viewModel?.position?.observe(activity, Observer<Int> {
            handler?.setMonth(months[it])
        })
    }

    private fun setUpMonthSpinner() {
        val lastPosition = months.size - 1

        mMaterialSpinner = binding.spinner
        mMaterialSpinner.gravity = Gravity.CENTER_HORIZONTAL
        mMaterialSpinner.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        mMaterialSpinner.setItems(months.map { month -> month.title })
        mMaterialSpinner.selectedIndex = lastPosition

        viewModel?.setIndex(lastPosition)

        mMaterialSpinner.setOnItemSelectedListener { _, position, _, _ ->
            viewModel?.setIndex(position)
        }
    }

    fun getMonthsFromFragment(
        cookieId: String,
        fragment: Fragment
    ) {
        viewModel = ViewModelProviders.of(fragment, viewModelFactory)[MonthViewModel::class.java]
        viewModel?.getMonths(cookieId)

        binding.lifecycleOwner = fragment
        binding.viewModel = viewModel

        viewModel?.successObserver?.observe(fragment, Observer {
            months.clear()
            months.addAll(it)
            handler?.setMonsths(months)
        })

        viewModel?.errorObserver?.observe(fragment, Observer {

        })

        viewModel?.position?.observe(fragment, Observer<Int> {
            handler?.setMonth(months[it])
        })
    }

    fun clearDisposable() {
        viewModel?.disposables?.clear()
    }
}