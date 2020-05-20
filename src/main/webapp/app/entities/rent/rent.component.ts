import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRent } from 'app/shared/model/rent.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RentService } from './rent.service';
import { RentDeleteDialogComponent } from './rent-delete-dialog.component';
import { RentPayDialogComponent } from './rent-pay-dialog.component';

@Component({
  selector: 'jhi-rent',
  templateUrl: './rent.component.html'
})
export class RentComponent implements OnInit, OnDestroy {
  rents?: IRent[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected rentService: RentService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.rentService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IRent[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInRents();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRent): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRents(): void {
    this.eventSubscriber = this.eventManager.subscribe('rentListModification', () => this.loadPage());
  }

  delete(rent: IRent): void {
    const modalRef = this.modalService.open(RentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rent = rent;
  }

  pay(rent: IRent): void {
    const modelRef = this.modalService.open(RentPayDialogComponent, { size: 'lg', backdrop: 'static' });
    modelRef.componentInstance.rent = rent;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IRent[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/rent'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.rents = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
