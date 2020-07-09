package org.dhis2.uicomponents.map.carousel

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.dhis2.R
import org.dhis2.databinding.ItemCarouselProgramEventBinding
import org.dhis2.usescases.programEventDetail.ProgramEventViewModel

class CarouselProgramEventHolder(
    val binding: ItemCarouselProgramEventBinding,
    val onClick: (eventUid: String?, orgUnitUid: String?) -> Boolean
) :
    RecyclerView.ViewHolder(binding.root),
    CarouselBinder<ProgramEventViewModel> {

    override fun bind(data: ProgramEventViewModel) {
        binding.event = data
        itemView.setOnClickListener {
            onClick(data.uid(), data.orgUnitUid())
        }

        val attributesString = SpannableStringBuilder("")
        data.eventDisplayData().forEach {
            attributesString.append(
                SpannableStringBuilder("${it.val0()} ${it.val1()}  ").apply {
                    setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(itemView.context, R.color.text_black_8A3)
                        ),
                        0, it.val0().length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            )
        }
        binding.dataValue.text = when {
            attributesString.isNotEmpty() -> attributesString
            else -> itemView.context.getString(R.string.no_data)
        }
    }
}