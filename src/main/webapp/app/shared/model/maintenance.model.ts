import { Moment } from 'moment';

export interface IMaintenance {
  id?: number;
  description?: string;
  notificationDate?: Moment;
  dateComplete?: Moment;
  contractorJobNumber?: string;
  repairCost?: number;
  repairPaidOn?: Moment;
  receiptOfPayment?: string;
  apartmentId?: number;
  contractorId?: number;
}

export class Maintenance implements IMaintenance {
  constructor(
    public id?: number,
    public description?: string,
    public notificationDate?: Moment,
    public dateComplete?: Moment,
    public contractorJobNumber?: string,
    public repairCost?: number,
    public repairPaidOn?: Moment,
    public receiptOfPayment?: string,
    public apartmentId?: number,
    public contractorId?: number
  ) {}
}
