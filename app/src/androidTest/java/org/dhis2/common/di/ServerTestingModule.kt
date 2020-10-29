package org.dhis2.common.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import org.dhis2.App
import org.dhis2.BuildConfig
import org.dhis2.data.dagger.PerServer
import org.dhis2.data.prefs.PreferenceProviderImpl
import org.dhis2.data.server.DataBaseExporter
import org.dhis2.data.server.DataBaseExporterImpl
import org.dhis2.data.server.ServerModule
import org.dhis2.data.server.UserManager
import org.dhis2.data.server.UserManagerImpl
import org.dhis2.utils.RulesUtilsProvider
import org.dhis2.utils.RulesUtilsProviderImpl
import org.dhis2.utils.analytics.AnalyticsHelper
import org.dhis2.utils.analytics.AnalyticsInterceptor
import org.dhis2.utils.analytics.matomo.MatomoAnalyticsControllerImpl
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.D2Configuration
import org.hisp.dhis.android.core.D2Manager
import org.hisp.dhis.android.core.TestingD2Manager
import java.util.ArrayList

@Module
class ServerTestingModule : ServerModule(){

    @Provides
    @PerServer
    override fun sdk(): D2? {
        return TestingD2Manager.getD2()
    }

    @Provides
    @PerServer
    override fun configurationRepository(d2: D2?): UserManager? {
        return UserManagerImpl(d2!!)
    }

    @Provides
    @PerServer
    override fun dataBaseExporter(d2: D2?): DataBaseExporter? {
        return DataBaseExporterImpl(d2)
    }

    companion object {
        fun getD2Configuration(context: Context): D2Configuration? {
            val interceptors: MutableList<Interceptor> =
                ArrayList()
            val matomoTracker = (context as App).tracker
            interceptors.add(StethoInterceptor())
            interceptors.add(
                AnalyticsInterceptor(
                    AnalyticsHelper(
                        FirebaseAnalytics.getInstance(context),
                        PreferenceProviderImpl(context),
                        MatomoAnalyticsControllerImpl(matomoTracker),
                        TestingD2Manager.getD2()
                    )
                )
            )
            return D2Configuration.builder()
                .appName(BuildConfig.APPLICATION_ID)
                .appVersion(BuildConfig.VERSION_NAME)
                .connectTimeoutInSeconds(10 * 60)
                .readTimeoutInSeconds(10 * 60)
                .networkInterceptors(interceptors)
                .writeTimeoutInSeconds(10 * 60)
                .context(context)
                .build()
        }
    }

    @Provides
    @PerServer
    override fun rulesUtilsProvider(d2: D2?): RulesUtilsProvider? {
        return RulesUtilsProviderImpl(d2!!)
    }
}