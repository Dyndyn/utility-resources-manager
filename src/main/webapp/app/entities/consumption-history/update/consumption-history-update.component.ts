import { Component, OnInit } from '@angular/core';
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

@Component({
  standalone: true,
  selector: 'jhi-consumption-history-update',
  templateUrl: './consumption-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
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
}
