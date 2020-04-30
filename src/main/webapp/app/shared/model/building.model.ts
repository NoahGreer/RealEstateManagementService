import { Moment } from 'moment';
import { IPropertyTax } from 'app/shared/model/property-tax.model';
import { IApartment } from 'app/shared/model/apartment.model';

export interface IBuilding {
  id?: number;
  name?: string;
  purchaseDate?: Moment;
  propertyNumber?: string;
  streetAddress?: string;
  city?: string;
  stateCode?: string;
  zipCode?: string;
  propertyTaxes?: IPropertyTax[];
  apartments?: IApartment[];
}

export class Building implements IBuilding {
  constructor(
    public id?: number,
    public name?: string,
    public purchaseDate?: Moment,
    public propertyNumber?: string,
    public streetAddress?: string,
    public city?: string,
    public stateCode?: string,
    public zipCode?: string,
    public propertyTaxes?: IPropertyTax[],
    public apartments?: IApartment[]
  ) {}
}
