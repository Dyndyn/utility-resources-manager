import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUtilityProvider } from '../utility-provider.model';
import { UtilityProviderService } from '../service/utility-provider.service';

@Component({
  standalone: true,
  templateUrl: './utility-provider-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UtilityProviderDeleteDialogComponent {
  utilityProvider?: IUtilityProvider;

  constructor(
    protected utilityProviderService: UtilityProviderService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilityProviderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
