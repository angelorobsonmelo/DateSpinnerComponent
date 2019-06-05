package br.com.soluevo.spinnerdatelibrary.months.di.component

import br.com.soluevo.spinnerdatelibrary.commom.di.ContextModule
import br.com.soluevo.spinnerdatelibrary.months.MonthCustomView
import br.com.soluevo.spinnerdatelibrary.months.di.module.MonthModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MonthModule::class, ContextModule::class])
interface MonthComponent {

    fun inject(monthCustomView: MonthCustomView)
}