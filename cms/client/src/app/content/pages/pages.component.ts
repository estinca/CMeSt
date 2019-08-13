import {Component, OnInit} from '@angular/core';
import {Site} from '../../shared/models/entity/site';
import {TreeViewItem} from '../../shared/models/tree.view.item';
import {BaseComponent} from '../../base.component';
import {PageService} from '../../shared/service/page.service';
import {takeUntil, take} from 'rxjs/operators';
import {PaginatedCollection} from '../../shared/models/paginated.collection';
import {Page} from '../../shared/models/entity/page';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmModalComponent } from 'src/app/shared/modal/confirm-modal/confirm-modal.component';
import { EmitterService } from 'src/app/shared/service/emitterService';

@Component({
  selector: 'cms-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.scss']
})
export class PagesComponent extends BaseComponent implements OnInit {

  private static readonly DELETE_CONFIRM_KEYS: string[] = [
    'COMPONENT.PAGES.DELETE.title',
    'COMPONENT.PAGES.DELETE.body',
    'COMPONENT.PAGES.DELETE.ok',
    'COMPONENT.PAGES.DELETE.close',
  ];

  public collection: PaginatedCollection = null;
  public currentPage: number = 1;

  public error: string = null;
  public success: string = null;

  public site: Site = null;

  private treeItem: TreeViewItem = null;

  constructor(private pageService: PageService, private translate: TranslateService,
    private modalService: BsModalService) {
    super();
  }

  ngOnInit() {

    EmitterService.of('pageError')
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((error: string) => {
        this.error = error;
        this.doneLoading();
      });
  }

  public onSiteSelected(site: Site): void {
    this.site = site;
  }

  public onTreeItemSelected(item: TreeViewItem): void {
    this.treeItem = item;
    this.setPage(1);
  }

  public setPage(page: number): void {
    if (this.site !== null && this.treeItem !== null) {
      this.loading();
      this.pageService.getPagesBySiteAndParent(this.site.id, page, this.treeItem.id, true )
        .pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((collection: PaginatedCollection) => {
          if (collection != null) {
            this.collection = collection;
            this.currentPage = page;
          }
          this.doneLoading();
        });
    }
  }

  public askForPageDeletion(site: Site, page: Page): void {
    const initialConfirmState = this.buildConfirmModalState(page.name + " from " + site.name);
    const modalOptions: ModalOptions = {
      initialState: initialConfirmState
    };
    const modalRef: BsModalRef = this.modalService.show(ConfirmModalComponent, modalOptions);

    modalRef.content.confirmed.pipe(take(1))
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.deletePage(site, page);
        }
      });
  }

  private buildConfirmModalState(pageName: string): any {
    const translations = this.translate.instant(PagesComponent.DELETE_CONFIRM_KEYS, { pageName: pageName });

    return {
      title: translations[PagesComponent.DELETE_CONFIRM_KEYS[0]],
      body: translations[PagesComponent.DELETE_CONFIRM_KEYS[1]],
      okBtn: translations[PagesComponent.DELETE_CONFIRM_KEYS[2]],
      closeBtn: translations[PagesComponent.DELETE_CONFIRM_KEYS[3]],
    };
  }

  private deletePage(site: Site, page: Page): void {
    this.loading();
    this.success = null;
    this.pageService.deletePage(site.id, page.id).pipe(
      takeUntil(this.ngUnsubscribe)
    ).subscribe((res: boolean) => {
      if (res) {
        this.success = 'COMPONENT.PAGES.DELETE.success';
        this.setPage(this.currentPage);
      }
      this.doneLoading();
    });
  }

}
