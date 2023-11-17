import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHouseholdUtility } from '../household-utility.model';
import { HouseholdUtilityService } from '../service/household-utility.service';

@Component({
  standalone: true,
  templateUrl: './household-utility-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HouseholdUtilityDeleteDialogComponent {
  householdUtility?: IHouseholdUtility;

  constructor(
    protected householdUtilityService: HouseholdUtilityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.householdUtilityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
