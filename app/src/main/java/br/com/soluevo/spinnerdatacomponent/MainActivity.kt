package br.com.soluevo.spinnerdatacomponent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.soluevo.spinnerdatelibrary.domain.MonthResponse
import br.com.soluevo.spinnerdatelibrary.months.MonthCustomView
import br.com.soluevo.spinnerdatelibrary.months.handler.MonthHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MonthHandler {

    private lateinit var customView: MonthCustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpCustomTabDate()
    }

    private fun setUpCustomTabDate() {
        customView = month_custom_view_id
        customView.handler = this
        customView.getMonthsFromActivity(
            "_session_id=OqBRjNjX89fV4wjh-ecvgfCWNPE; path=/; HttpOnly",
            this
        )
    }

    override fun setMonth(monthResponse: MonthResponse) {

    }

    override fun setMonsths(months: MutableList<MonthResponse>) {

    }

    override fun setError(error: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        customView.clearDisposable()
    }
}