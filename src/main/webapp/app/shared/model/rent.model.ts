import { Moment } from 'moment';

export interface IRent {
  id?: number;
  dueDate?: Moment;
  receivedDate?: Moment;
  amount?: number;
  leaseId?: number;
}

export class Rent implements IRent {
  constructor(public id?: number, public dueDate?: Moment, public receivedDate?: Moment, public amount?: number, public leaseId?: number) {}
}
