package br.com.soluevo.spinnerdatelibrary.months

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import br.com.soluevo.spinnerdatelibrary.commom.BaseViewModel
import br.com.soluevo.spinnerdatelibrary.domain.MonthResponse
import br.com.soluevo.spinnerdatelibrary.service.months.MonthsApiDataSource
import br.com.soluevo.spinnerdatelibrary.utils.DateUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class MonthViewModel @Inject constructor(
    private val monthsApiDataSource: MonthsApiDataSource
) : BaseViewModel<MutableList<MonthResponse>>() {

    val disposables = CompositeDisposable()
    private val _index = MutableLiveData<Int>()

    val position: LiveData<Int> = Transformations.map(_index) {
        it
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun getMonths(cookieId: String) {
        val disposable = monthsApiDataSource.getMonths(cookieId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.reverse()
                DateUtil.fillList(it)
            }
            .map {
                val newMonthsResponse = mutableListOf<MonthResponse>()
                it.forEach { monthResponse ->
                    val title = getTitle(monthResponse)

                    if (isToday(monthResponse)) {
                        newMonthsResponse.add(
                            MonthResponse(
                                monthResponse.id,
                                monthResponse.month,
                                monthResponse.year,
                                title.toUpperCase()
                            )
                        )
                    } else {
                        newMonthsResponse.add(
                            MonthResponse(
                                monthResponse.id,
                                monthResponse.month,
                                monthResponse.year,
                                title.toUpperCase()
                            )
                        )
                    }
                }

                newMonthsResponse
            }
            .doOnSubscribe { isLoadingObserver.value = true }
            .doAfterTerminate { isLoadingObserver.value = false }
            .subscribe(
                {
                    successObserver.value = it
                },
                { throwable ->
                    errorObserver.value = throwable.localizedMessage
                }
            )

        disposables.add(disposable)
    }

    private fun getTitle(
        monthResponse: MonthResponse
    ): String {
        val shortMonths = DateFormatSymbols().shortMonths
        return if (isToday(monthResponse)) {
            "Hoje"
        } else {
            "${shortMonths[monthResponse.month - 1]} / ${monthResponse.year}"
        }
    }

    private fun isToday(
        monthResponse: MonthResponse
    ): Boolean {
        val mCalendar = Calendar.getInstance()
        val mTodayMonth = mCalendar.get(Calendar.MONTH)
        val mTodayYear = mCalendar.get(Calendar.YEAR)

        return monthResponse.month == mTodayMonth + 1 && monthResponse.year == mTodayYear
    }
}