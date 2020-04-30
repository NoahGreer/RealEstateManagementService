import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPropertyTax } from 'app/shared/model/property-tax.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PropertyTaxService } from './property-tax.service';
import { PropertyTaxDeleteDialogComponent } from './property-tax-delete-dialog.component';

@Component({
  selector: 'jhi-property-tax',
  templateUrl: './property-tax.component.html'
})
export class PropertyTaxComponent implements OnInit, OnDestroy {
  propertyTaxes?: IPropertyTax[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected propertyTaxService: PropertyTaxService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.propertyTaxService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IPropertyTax[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInPropertyTaxes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPropertyTax): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPropertyTaxes(): void {
    this.eventSubscriber = this.eventManager.subscribe('propertyTaxListModification', () => this.loadPage());
  }

  delete(propertyTax: IPropertyTax): void {
    const modalRef = this.modalService.open(PropertyTaxDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.propertyTax = propertyTax;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IPropertyTax[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/property-tax'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.propertyTaxes = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
