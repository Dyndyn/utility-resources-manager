import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUtility } from '../utility.model';
import { UtilityService } from '../service/utility.service';

@Component({
  standalone: true,
  templateUrl: './utility-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UtilityDeleteDialogComponent {
  utility?: IUtility;

  constructor(
    protected utilityService: UtilityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
