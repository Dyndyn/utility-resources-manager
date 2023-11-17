import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtility } from '../utility.model';
import { UtilityService } from '../service/utility.service';
import { UtilityFormService, UtilityFormGroup } from './utility-form.service';

@Component({
  standalone: true,
  selector: 'jhi-utility-update',
  templateUrl: './utility-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UtilityUpdateComponent implements OnInit {
  isSaving = false;
  utility: IUtility | null = null;

  editForm: UtilityFormGroup = this.utilityFormService.createUtilityFormGroup();

  constructor(
    protected utilityService: UtilityService,
    protected utilityFormService: UtilityFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utility }) => {
      this.utility = utility;
      if (utility) {
        this.updateForm(utility);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utility = this.utilityFormService.getUtility(this.editForm);
    if (utility.id !== null) {
      this.subscribeToSaveResponse(this.utilityService.update(utility));
    } else {
      this.subscribeToSaveResponse(this.utilityService.create(utility));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtility>>): void {
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

  protected updateForm(utility: IUtility): void {
    this.utility = utility;
    this.utilityFormService.resetForm(this.editForm, utility);
  }
}
