import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { NgChartsModule } from 'ng2-charts';

import FindLanguageFromKeyPipe from './language/find-language-from-key.pipe';
import TranslateDirective from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { MatDatepickerModule } from '@angular/material/datepicker';

/**
 * Application wide Module
 */
@NgModule({
  imports: [AlertComponent, AlertErrorComponent, FindLanguageFromKeyPipe, TranslateDirective],
  exports: [
    CommonModule,
    NgChartsModule,
    MatDatepickerModule,
    NgbModule,
    FontAwesomeModule,
    AlertComponent,
    AlertErrorComponent,
    TranslateModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
  ],
})
export default class SharedModule {}
