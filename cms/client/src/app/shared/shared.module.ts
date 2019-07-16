import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HttpLoaderFactory} from './utils/functions';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {AlertModule, ModalModule} from 'ngx-bootstrap';
import { PaginationComponent } from './component/pagination/pagination.component';
import { FlashMessageComponent } from './component/flash-message/flash-message.component';
import { ConfirmModalComponent } from './modal/confirm-modal/confirm-modal.component';
import {NgxSelectModule} from 'ngx-select-ex';
import { SiteService } from './service/site.service';
import { RestService } from './service/rest.service';
import { AuthenticationService } from './service/authentication.service';
import { AuthGuardService } from './service/auth-guard.service';
import { FlashMessageService } from './service/flash-message.service';


export function jwtTokenGetter() {
  return 'no-token';
}

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,

    JwtModule.forRoot({
      config: {
        tokenGetter: jwtTokenGetter
      }
    }),

    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),

    // Common bootstrap modules
    AlertModule.forRoot(),
    ModalModule.forRoot(),
    NgxSelectModule,

  ],
  // providers: [
  //   SiteService,
  //   RestService,
  //   AuthenticationService,
  //   AuthGuardService,
  //   FlashMessageService
  // ],

  declarations: [
    PaginationComponent,
    FlashMessageComponent,
    ConfirmModalComponent
  ],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    TranslateModule,

    // Common bootstrap modules
    AlertModule,
    ModalModule,
    NgxSelectModule,

    PaginationComponent,
    FlashMessageComponent,

    ConfirmModalComponent
  ],
  entryComponents: [
    ConfirmModalComponent
  ]
})
export class SharedModule { }
