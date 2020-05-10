import { Moment } from 'moment';

export interface IPropertyTax {
  id?: number;
  taxYear?: number;
  amount?: number;
  datePaid?: Moment;
  confirmationNumber?: string;
  buildingId?: number;
}

export class PropertyTax implements IPropertyTax {
  constructor(
    public id?: number,
    public taxYear?: number,
    public amount?: number,
    public datePaid?: Moment,
    public confirmationNumber?: string,
    public buildingId?: number
  ) {}
}
