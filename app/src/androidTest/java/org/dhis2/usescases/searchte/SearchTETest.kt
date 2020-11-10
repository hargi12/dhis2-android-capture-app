package org.dhis2.usescases.searchte

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResourceTimeoutException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import org.dhis2.R
import org.dhis2.common.idlingresources.MapIdlingResource
import org.dhis2.usescases.BaseTest
import org.dhis2.usescases.flow.teiFlow.entity.DateRegistrationUIModel
import org.dhis2.usescases.searchTrackEntity.SearchTEActivity
import org.dhis2.usescases.searchte.entity.DisplayListFieldsUIModel
import org.junit.After
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchTETest : BaseTest() {

    @get:Rule
    val rule = ActivityTestRule(SearchTEActivity::class.java, false, false)

    private var mapIdlingResource: MapIdlingResource? = null
    private var map: MapboxMap? = null

    @Test
    fun shouldSuccessfullySearchByName() {
        val firstName = "Tim"
        val firstNamePosition = 0
        val filterCount = "1"

        prepareChildProgrammeIntentAndLaunchActivity()
        
        searchTeiRobot {
            typeAttributeAtPosition(firstName, firstNamePosition)
            clickOnFab()
            checkFilterCount(filterCount)
            closeSearchForm()
            checkListOfSearchTEI(firstName, "")
        }
    }

    @Test
    fun shouldShowErrorWhenCanNotFindSearchResult() {
        val firstName = "asdssds"
        val firstNamePosition = 1
        val filterCount = "1"
        val noResultMessage = context.getString(R.string.search_criteria_not_met).replace("%s","Person")

        prepareTestProgramRulesProgrammeIntentAndLaunchActivity()

        searchTeiRobot {
            typeAttributeAtPosition(firstName, firstNamePosition)
            clickOnFab()
            checkFilterCount(filterCount)
            closeSearchForm()
            checkNoSearchResult(firstName, noResultMessage)
        }
    }

    @Test
    fun shouldSuccessfullySearchUsingMoreThanOneField() {
        val firstName = "Anna"
        val firstNamePosition = 0
        val lastName = "Jones"
        val lastNamePosition = 1
        val filterCount = "2"

        prepareChildProgrammeIntentAndLaunchActivity()

        searchTeiRobot {
            typeAttributeAtPosition(firstName, firstNamePosition)
            typeAttributeAtPosition(lastName, lastNamePosition)
            clickOnFab()
            checkFilterCount(filterCount)
            closeSearchForm()
            checkListOfSearchTEI(firstName, lastName)
        }
    }

    @Test
    fun shouldSuccessfullyChangeBetweenPrograms() {
        val tbProgram = "TB program"

        prepareChildProgrammeIntentAndLaunchActivity()

        searchTeiRobot {
            clickOnProgramSpinner()
            selectAProgram(tbProgram)
            checkProgramHasChanged(tbProgram)
        }
    }

    @Test
    fun shouldCheckDisplayInList() {
        val birthdaySearch = createDateOfBirthSearch()
        val displayInListData = createDisplayListFields()
        val namePosition = 0
        val lastNamePosition = 1
        val filterCount = "3"

        prepareTestAdultWomanProgrammeIntentAndLaunchActivity()

        searchTeiRobot {
            typeAttributeAtPosition(displayInListData.name, namePosition)
            typeAttributeAtPosition(displayInListData.lastName, lastNamePosition)
            clickOnDateField()
            selectSpecificDate(birthdaySearch.year, birthdaySearch.month, birthdaySearch.day)
            acceptDate()
            clickOnFab()
            checkFilterCount(filterCount)
            closeSearchForm()
            checkFieldsFromDisplayList(displayInListData)
        }
    }


    @Ignore("WIP")
    @Test
    fun shouldSuccessfullyFilterBySync() {
        prepareChildProgrammeIntentAndLaunchActivity()

        searchTeiRobot {
            /*clickOnSearchFilter()
            clickOnFilterByName("SYNC")*/
            //clickOnFilterByName("Synced")
            closeSearchForm()
        }
    }

    @Test
    fun shouldSuccessfullyShowMapAndTeiCard() {
        val firstName = "Jimmy"

        prepareTestTBProgrammeIntentAndLaunchActivity()

        searchTeiRobot {
            clickOnOptionMenu()
            clickOnShowMap()
            try {
                mapIdlingResource = MapIdlingResource(rule)
                IdlingRegistry.getInstance().register(mapIdlingResource)
                map = mapIdlingResource!!.map
            } catch (ex: IdlingResourceTimeoutException) {
                throw RuntimeException("Could not start test")
            }
            checkCarouselTEICardInfo(firstName)
        }
    }

    @After
    fun unregisterIdlingResource() {
        if (mapIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mapIdlingResource)
        }
    }

    private fun createDateOfBirthSearch() = DateRegistrationUIModel(
        2001,
        1,
        1
    )

    private fun createDisplayListFields() = DisplayListFieldsUIModel(
        "Sarah",
        "Thompson",
        "2001-01-01",
        "sarah@gmail.com",
        "Main street 1",
        "56",
        "167"
    )

    private fun prepareChildProgrammeIntentAndLaunchActivity() {
        Intent().apply {
            putExtra(PROGRAM_UID, CHILD_PROGRAM_UID_VALUE)
            putExtra(CHILD_TE_TYPE, CHILD_TE_TYPE_VALUE)
        }.also { rule.launchActivity(it) }
    }

    private fun prepareTestProgramRulesProgrammeIntentAndLaunchActivity() {
        Intent().apply {
            putExtra(PROGRAM_UID, XX_TEST_PROGRAM_RULES_UID_VALUE)
            putExtra(CHILD_TE_TYPE, PROGRAM_RULES_TE_TYPE_VALUE)
        }.also { rule.launchActivity(it) }
    }

    private fun prepareTestAdultWomanProgrammeIntentAndLaunchActivity() {
        Intent().apply {
            putExtra(PROGRAM_UID, ADULT_WOMAN_PROGRAM_UID_VALUE)
            putExtra(CHILD_TE_TYPE, ADULT_WOMAN_TE_TYPE_VALUE)
        }.also { rule.launchActivity(it) }
    }

    private fun prepareTestTBProgrammeIntentAndLaunchActivity() {
        Intent().apply {
            putExtra(PROGRAM_UID, TB_PROGRAM_UID)
            putExtra(CHILD_TE_TYPE, TB_TE_TYPE_VALUE)
        }.also { rule.launchActivity(it) }
    }

    companion object {
        const val PROGRAM_UID = "PROGRAM_UID"
        const val CHILD_PROGRAM_UID_VALUE = "IpHINAT79UW"
        const val XX_TEST_PROGRAM_RULES_UID_VALUE = "jIT6KcSZiAN"
        const val ADULT_WOMAN_PROGRAM_UID_VALUE = "uy2gU8kT1jF"
        const val TB_PROGRAM_UID = "ur1Edk5Oe2n"


        const val CHILD_TE_TYPE_VALUE = "nEenWmSyUEp"
        const val PROGRAM_RULES_TE_TYPE_VALUE = "nEenWmSyUEp"
        const val ADULT_WOMAN_TE_TYPE_VALUE = "nEenWmSyUEp"
        const val TB_TE_TYPE_VALUE = "nEenWmSyUEp"
        const val CHILD_TE_TYPE = "TRACKED_ENTITY_UID"
    }
}
