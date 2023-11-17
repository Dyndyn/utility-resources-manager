import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtility } from 'app/entities/utility/utility.model';
import { UtilityService } from 'app/entities/utility/service/utility.service';
import { IUtilityProvider } from '../utility-provider.model';
import { UtilityProviderService } from '../service/utility-provider.service';
import { UtilityProviderFormService, UtilityProviderFormGroup } from './utility-provider-form.service';

@Component({
  standalone: true,
  selector: 'jhi-utility-provider-update',
  templateUrl: './utility-provider-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UtilityProviderUpdateComponent implements OnInit {
  isSaving = false;
  utilityProvider: IUtilityProvider | null = null;

  utilitiesSharedCollection: IUtility[] = [];

  editForm: UtilityProviderFormGroup = this.utilityProviderFormService.createUtilityProviderFormGroup();

  constructor(
    protected utilityProviderService: UtilityProviderService,
    protected utilityProviderFormService: UtilityProviderFormService,
    protected utilityService: UtilityService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUtility = (o1: IUtility | null, o2: IUtility | null): boolean => this.utilityService.compareUtility(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilityProvider }) => {
      this.utilityProvider = utilityProvider;
      if (utilityProvider) {
        this.updateForm(utilityProvider);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilityProvider = this.utilityProviderFormService.getUtilityProvider(this.editForm);
    if (utilityProvider.id !== null) {
      this.subscribeToSaveResponse(this.utilityProviderService.update(utilityProvider));
    } else {
      this.subscribeToSaveResponse(this.utilityProviderService.create(utilityProvider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilityProvider>>): void {
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

  protected updateForm(utilityProvider: IUtilityProvider): void {
    this.utilityProvider = utilityProvider;
    this.utilityProviderFormService.resetForm(this.editForm, utilityProvider);

    this.utilitiesSharedCollection = this.utilityService.addUtilityToCollectionIfMissing<IUtility>(
      this.utilitiesSharedCollection,
      utilityProvider.utility,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilityService
      .query()
      .pipe(map((res: HttpResponse<IUtility[]>) => res.body ?? []))
      .pipe(
        map((utilities: IUtility[]) =>
          this.utilityService.addUtilityToCollectionIfMissing<IUtility>(utilities, this.utilityProvider?.utility),
        ),
      )
      .subscribe((utilities: IUtility[]) => (this.utilitiesSharedCollection = utilities));
  }
}
