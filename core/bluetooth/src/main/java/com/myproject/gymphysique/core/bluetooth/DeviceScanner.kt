package com.myproject.gymphysique.core.bluetooth

import com.benasher44.uuid.Uuid
import com.juul.kable.Advertisement
import com.juul.kable.Filter
import com.juul.kable.Scanner
import com.juul.kable.logs.Logging
import com.juul.kable.logs.SystemLogEngine
import com.myproject.gymphysique.core.common.throttleLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber

class DeviceScanner(private val filters: List<Uuid>): AdvertisementProvider {
    private val scanFilters = filters.map {
        Filter.Service(it)
    }
    private val scanner = Scanner {
        filters = scanFilters
        logging {
            engine = SystemLogEngine
            level = Logging.Level.Warnings
            format = Logging.Format.Multiline
        }
    }

    override fun provideAdvertisements(): Flow<Advertisement> {
        return scanner
            .advertisements
            .throttleLatest(100)
            .catch { exception ->
                Timber.d("stopped exception: " + exception.message)
            }
    }
}