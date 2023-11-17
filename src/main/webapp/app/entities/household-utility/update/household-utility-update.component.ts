import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IHousehold } from 'app/entities/household/household.model';
import { HouseholdService } from 'app/entities/household/service/household.service';
import { IUtilityProvider } from 'app/entities/utility-provider/utility-provider.model';
import { UtilityProviderService } from 'app/entities/utility-provider/service/utility-provider.service';
import { HouseholdUtilityService } from '../service/household-utility.service';
import { IHouseholdUtility } from '../household-utility.model';
import { HouseholdUtilityFormService, HouseholdUtilityFormGroup } from './household-utility-form.service';

@Component({
  standalone: true,
  selector: 'jhi-household-utility-update',
  templateUrl: './household-utility-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HouseholdUtilityUpdateComponent implements OnInit {
  isSaving = false;
  householdUtility: IHouseholdUtility | null = null;

  householdsSharedCollection: IHousehold[] = [];
  utilityProvidersSharedCollection: IUtilityProvider[] = [];

  editForm: HouseholdUtilityFormGroup = this.householdUtilityFormService.createHouseholdUtilityFormGroup();

  constructor(
    protected householdUtilityService: HouseholdUtilityService,
    protected householdUtilityFormService: HouseholdUtilityFormService,
    protected householdService: HouseholdService,
    protected utilityProviderService: UtilityProviderService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareHousehold = (o1: IHousehold | null, o2: IHousehold | null): boolean => this.householdService.compareHousehold(o1, o2);

  compareUtilityProvider = (o1: IUtilityProvider | null, o2: IUtilityProvider | null): boolean =>
    this.utilityProviderService.compareUtilityProvider(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ householdUtility }) => {
      this.householdUtility = householdUtility;
      if (householdUtility) {
        this.updateForm(householdUtility);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const householdUtility = this.householdUtilityFormService.getHouseholdUtility(this.editForm);
    if (householdUtility.id !== null) {
      this.subscribeToSaveResponse(this.householdUtilityService.update(householdUtility));
    } else {
      this.subscribeToSaveResponse(this.householdUtilityService.create(householdUtility));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHouseholdUtility>>): void {
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

  protected updateForm(householdUtility: IHouseholdUtility): void {
    this.householdUtility = householdUtility;
    this.householdUtilityFormService.resetForm(this.editForm, householdUtility);

    this.householdsSharedCollection = this.householdService.addHouseholdToCollectionIfMissing<IHousehold>(
      this.householdsSharedCollection,
      householdUtility.household,
    );
    this.utilityProvidersSharedCollection = this.utilityProviderService.addUtilityProviderToCollectionIfMissing<IUtilityProvider>(
      this.utilityProvidersSharedCollection,
      householdUtility.utilityProvider,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.householdService
      .query()
      .pipe(map((res: HttpResponse<IHousehold[]>) => res.body ?? []))
      .pipe(
        map((households: IHousehold[]) =>
          this.householdService.addHouseholdToCollectionIfMissing<IHousehold>(households, this.householdUtility?.household),
        ),
      )
      .subscribe((households: IHousehold[]) => (this.householdsSharedCollection = households));

    this.utilityProviderService
      .query()
      .pipe(map((res: HttpResponse<IUtilityProvider[]>) => res.body ?? []))
      .pipe(
        map((utilityProviders: IUtilityProvider[]) =>
          this.utilityProviderService.addUtilityProviderToCollectionIfMissing<IUtilityProvider>(
            utilityProviders,
            this.householdUtility?.utilityProvider,
          ),
        ),
      )
      .subscribe((utilityProviders: IUtilityProvider[]) => (this.utilityProvidersSharedCollection = utilityProviders));
  }
}
