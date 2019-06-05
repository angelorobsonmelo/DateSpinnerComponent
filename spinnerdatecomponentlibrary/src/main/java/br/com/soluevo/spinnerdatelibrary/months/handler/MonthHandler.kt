package br.com.soluevo.spinnerdatelibrary.months.handler

import br.com.soluevo.spinnerdatelibrary.domain.MonthResponse

interface MonthHandler {

    fun setMonth(monthResponse: MonthResponse)
    fun setError(error: String)
    fun setMonsths(months: MutableList<MonthResponse>)
}