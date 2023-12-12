import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IHouseholdUtility } from 'app/entities/household-utility/household-utility.model';
import { HouseholdUtilityService } from 'app/entities/household-utility/service/household-utility.service';
import { IConsumptionHistory } from '../consumption-history.model';
import { ConsumptionHistoryService } from '../service/consumption-history.service';
import { ConsumptionHistoryFormService, ConsumptionHistoryFormGroup } from './consumption-history-form.service';
import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from '@angular/material-moment-adapter';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepicker, MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Moment } from 'moment';
import moment from 'moment';

export const MY_FORMATS = {
  parse: {
    dateInput: 'MM/YYYY',
  },
  display: {
    dateInput: 'MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  standalone: true,
  selector: 'jhi-consumption-history-update',
  templateUrl: './consumption-history-update.component.html',
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
    },
    { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
  ],
  encapsulation: ViewEncapsulation.None,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, MatDatepickerModule, MatInputModule, MatFormFieldModule],
})
export class ConsumptionHistoryUpdateComponent implements OnInit {
  isSaving = false;
  consumptionHistory: IConsumptionHistory | null = null;

  householdUtility: IHouseholdUtility | null = null;

  editForm: ConsumptionHistoryFormGroup = this.consumptionHistoryFormService.createConsumptionHistoryFormGroup();

  constructor(
    protected consumptionHistoryService: ConsumptionHistoryService,
    protected consumptionHistoryFormService: ConsumptionHistoryFormService,
    protected householdUtilityService: HouseholdUtilityService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareHouseholdUtility = (o1: IHouseholdUtility | null, o2: IHouseholdUtility | null): boolean =>
    this.householdUtilityService.compareHouseholdUtility(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumptionHistory }) => {
      this.consumptionHistory = consumptionHistory;
      if (consumptionHistory) {
        this.updateForm(consumptionHistory);
        this.loadRelationshipsOptions(consumptionHistory.householdUtility.id);
      } else {
        this.activatedRoute.queryParams.subscribe(params => {
          this.loadRelationshipsOptions(params['householdUtilityId']);
        });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consumptionHistory = this.consumptionHistoryFormService.getConsumptionHistory(this.editForm);
    if (consumptionHistory.id !== null) {
      this.subscribeToSaveResponse(this.consumptionHistoryService.update(consumptionHistory));
    } else {
      this.subscribeToSaveResponse(this.consumptionHistoryService.create(consumptionHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsumptionHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(consumptionHistory: IConsumptionHistory): void {
    this.consumptionHistory = consumptionHistory;
    this.consumptionHistoryFormService.resetForm(this.editForm, consumptionHistory);
  }

  protected loadRelationshipsOptions(id: number): void {
    this.householdUtilityService.find(id).subscribe((householdUtility: HttpResponse<IHouseholdUtility>) => {
      this.householdUtility = householdUtility.body;
      this.householdUtility = { id: this.householdUtility!.id, name: this.householdUtility!.name };
      this.editForm.get(['householdUtility'])?.setValue(this.householdUtility);
    });
  }
  setMonthAndYear(normalizedMonthAndYear: Moment, datepicker: MatDatepicker<Moment>) {
    const ctrlValue = this.editForm.get(['date'])?.value ?? moment();
    ctrlValue.month(normalizedMonthAndYear.month());
    ctrlValue.year(normalizedMonthAndYear.year());
    this.editForm.get(['date'])?.setValue(ctrlValue);
    datepicker.close();
  }
}
