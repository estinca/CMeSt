import { Component } from '@angular/core';
import {BaseComponent} from './base.component';
import {TranslateService} from '@ngx-translate/core';
import {EmitterService} from './shared/service/emitterService';
import {takeUntil} from 'rxjs/operators';
import {Router} from '@angular/router';

@Component({
  selector: 'cms-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent extends BaseComponent {

  public isLoading: boolean = false;


  constructor(private translateService: TranslateService, private router: Router) {
    super();

    this.translateService.setDefaultLang('en');
    this.translateService.use('en');

  //   EmitterService.of('loading')
  //     .pipe(takeUntil(this.ngUnsubscribe))
  //     .subscribe((loading: boolean) => this.isLoading = loading);
  // }

    EmitterService.of("userLoggedIn")
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe( (isLoggedIn: boolean) => {
        if(!isLoggedIn) {
          this.router.navigateByUrl('/login');
        }
      });
  }

}
