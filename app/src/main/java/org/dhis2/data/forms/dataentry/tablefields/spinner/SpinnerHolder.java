package org.dhis2.data.forms.dataentry.tablefields.spinner;

import android.view.View;

import org.dhis2.data.forms.dataentry.tablefields.FormViewHolder;
import org.dhis2.data.forms.dataentry.tablefields.RowAction;
import org.dhis2.data.tuples.Trio;
import org.dhis2.databinding.FormOptionSetBinding;
import org.dhis2.utils.custom_views.OptionSetCellDialog;
import org.dhis2.utils.custom_views.OptionSetCellPopUp;
import org.dhis2.utils.custom_views.OptionSetDialog;


import androidx.fragment.app.FragmentActivity;
import io.reactivex.processors.FlowableProcessor;

/**
 * QUADRAM. Created by ppajuelo on 07/11/2017.
 */

public class SpinnerHolder extends FormViewHolder implements View.OnClickListener {

    FormOptionSetBinding binding;
    private final FlowableProcessor<RowAction> processor;
    private final FlowableProcessor<Trio<String, String, Integer>> processorOptionSet;

    private SpinnerViewModel viewModel;

    SpinnerHolder(FormOptionSetBinding mBinding, FlowableProcessor<RowAction> processor, FlowableProcessor<Trio<String, String, Integer>> processorOptionSet, boolean isSearchMode) {
        super(mBinding);
        this.binding = mBinding;
        this.processor = processor;
        this.processorOptionSet = processorOptionSet;

        binding.optionSetView.setOnSelectedOptionListener((optionName, optionCode) -> {

            processor.onNext(
                    RowAction.create(viewModel.uid(), optionCode , viewModel.dataElement(),
                            viewModel.categoryOptionCombo(), viewModel.catCombo(), viewModel.row(), viewModel.column())
            );
        });
    }

    public void update(SpinnerViewModel viewModel, boolean accessDataWrite) {
        this.viewModel = viewModel;
        binding.optionSetView.updateEditable(viewModel.editable() && accessDataWrite);
        binding.optionSetView.setValue(viewModel.value());
        binding.optionSetView.setOnClickListener(this);
    }

    public void dispose() {
    }

    @Override
    public void onClick(View v) {
        closeKeyboard(v);
        if (binding.optionSetView.openOptionDialog()) {
            OptionSetCellDialog dialog = new OptionSetCellDialog(viewModel,
                    binding.optionSetView,
                    (view) -> binding.optionSetView.deleteSelectedOption()
            );
            dialog.show(((FragmentActivity) binding.getRoot().getContext()).getSupportFragmentManager(), OptionSetDialog.TAG);
        } else
            new OptionSetCellPopUp(itemView.getContext(), v, viewModel,
                    binding.optionSetView);
    }
}