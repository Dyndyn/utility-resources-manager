import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';
import { HouseholdService } from '../service/household.service';
import { IHousehold } from '../household.model';
import { HouseholdFormService, HouseholdFormGroup } from './household-form.service';

@Component({
  standalone: true,
  selector: 'jhi-household-update',
  templateUrl: './household-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HouseholdUpdateComponent implements OnInit {
  isSaving = false;
  household: IHousehold | null = null;

  citiesSharedCollection: ICity[] = [];

  editForm: HouseholdFormGroup = this.householdFormService.createHouseholdFormGroup();

  constructor(
    protected householdService: HouseholdService,
    protected householdFormService: HouseholdFormService,
    protected cityService: CityService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCity = (o1: ICity | null, o2: ICity | null): boolean => this.cityService.compareCity(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ household }) => {
      this.household = household;
      if (household) {
        this.updateForm(household);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const household = this.householdFormService.getHousehold(this.editForm);
    if (household.id !== null) {
      this.subscribeToSaveResponse(this.householdService.partialUpdate(household));
    } else {
      this.subscribeToSaveResponse(this.householdService.create(household));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHousehold>>): void {
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

  protected updateForm(household: IHousehold): void {
    this.household = household;
    this.householdFormService.resetForm(this.editForm, household);

    this.citiesSharedCollection = this.cityService.addCityToCollectionIfMissing<ICity>(this.citiesSharedCollection, household.city);
  }

  protected loadRelationshipsOptions(): void {
    this.cityService
      .query()
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing<ICity>(cities, this.household?.city)))
      .subscribe((cities: ICity[]) => (this.citiesSharedCollection = cities));
  }
}
