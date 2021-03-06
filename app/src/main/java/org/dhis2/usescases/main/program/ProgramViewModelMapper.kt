package org.dhis2.usescases.main.program

import org.hisp.dhis.android.core.common.State
import org.hisp.dhis.android.core.dataset.DataSet
import org.hisp.dhis.android.core.dataset.DataSetInstanceSummary
import org.hisp.dhis.android.core.program.Program

class ProgramViewModelMapper {
    fun map(
        program: Program,
        recordCount: Int,
        recordLabel: String,
        state: State,
        hasOverdue: Boolean,
        filtersAreActive: Boolean
    ): ProgramViewModel {
        return ProgramViewModel.create(
            program.uid(),
            program.displayName()!!,
            if (program.style() != null) program.style()!!.color() else null,
            if (program.style() != null) program.style()!!.icon() else null,
            recordCount,
            if (program.trackedEntityType() != null) {
                program.trackedEntityType()!!.uid()
            } else {
                null
            },
            recordLabel,
            program.programType()!!.name,
            program.displayDescription(),
            onlyEnrollOnce = program.onlyEnrollOnce() == true,
            accessDataWrite = program.access().data().write(),
            state = state.name,
            hasOverdueEvent = hasOverdue,
            filtersAreActive = filtersAreActive
        )
    }

    fun map(
        dataSet: DataSet,
        dataSetInstanceSummary: DataSetInstanceSummary,
        recordCount: Int,
        dataSetLabel: String,
        filtersAreActive: Boolean
    ): ProgramViewModel {
        return ProgramViewModel.create(
            dataSetInstanceSummary.dataSetUid(),
            dataSetInstanceSummary.dataSetDisplayName(),
            dataSet.style().color(),
            dataSet.style().icon(),
            recordCount,
            null,
            typeName = dataSetLabel,
            programType = "",
            description = dataSet.description(),
            onlyEnrollOnce = false,
            accessDataWrite = dataSet.access().data().write(),
            state = dataSetInstanceSummary.state().name,
            hasOverdueEvent = false,
            filtersAreActive = filtersAreActive
        )
    }
}
