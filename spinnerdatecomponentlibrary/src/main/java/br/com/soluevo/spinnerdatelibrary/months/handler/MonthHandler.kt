package br.com.soluevo.spinnerdatelibrary.months.handler

import br.com.soluevo.spinnerdatelibrary.domain.MonthResponse

interface MonthHandler {

    fun setMonth(monthResponse: MonthResponse, index: Int)
    fun setError(error: String)
    fun setMonsths(months: MutableList<MonthResponse>)
}